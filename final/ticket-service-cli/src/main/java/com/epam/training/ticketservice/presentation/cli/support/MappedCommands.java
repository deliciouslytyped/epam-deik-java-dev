package com.epam.training.ticketservice.presentation.cli.support;

import com.epam.training.ticketservice.support.CustomCrudService;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This automatically generates commands for interfacing with the CrudRepositories on the CLI.
 *
 * This is a development tool to provide quick interactive cli interfacing to CrudRepository,
 * user facing commands should be implemented separately and provided with tests.
 */
@RequiredArgsConstructor
@Configuration
public class MappedCommands implements InitializingBean {
    protected final ApplicationContext ctx;
    protected final CommandCatalog cc;
//    //TODO how does this even work TODO does this work?
//    protected final Shell s;
    @Qualifier("mappedCommandConversionService")
    protected final DefaultConversionService cs; //TODO Is it wise to put everything in the global default conversion service object?

    @Bean
    public static DefaultConversionService mappedCommandConversionService(){
        return new DefaultConversionService();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //setConversionService(); // This approach doesnt work because the resolvers in ParameterResolverAutoConfiguration only return supportsParameter true for annotated stuff. The workable approach seems to be wrapping everything in a CommandContext consuming lambda from which the arguments can be handled manually. TBH I prefer this over the autoconversion approach anyway it was just not initially clear tha this is how thi sshould be used.
        addRegistrations();
    }

    /*
    //Why cant I just use the one already in spring??
    static class ObjectToStringConverter implements Converter<Object, String> {

        @Override
        public String convert(Object source) {
            return source.toString();
        }

    }

    public void setConversionService(){ //TODO this is kind of bad design because it destructively mutates a scope we dont own?
        var cs = new DefaultConversionService();
        // Without this, the argument is "converted" to null?
        cs.addConverter(String.class, String.class, new ObjectToStringConverter()); // This feels kind of stupid
        s.setConversionService(cs);
    }
    */

    public void registerSubcommand(String cmdName, String serviceName, Function<CommandRegistration.TargetSpec, ?> invoker){
        var builder = CommandRegistration.builder();
        // bleh
        //TODO no nested groups? :/
        var entityName = serviceName.replace("CrudServiceImpl","");
        Object result = invoker.apply(builder.group("map " + entityName) //The sorting sucks for some reason so group by entity instead
                .command("map " + cmdName + " " + entityName)
                .withTarget()
        );
        var reg = (switch (result) {
            case CommandRegistration.TargetSpec t -> t.and();
            case CommandRegistration.OptionSpec o -> o.and();
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }).build();
        cc.register(reg);
    }
    public void addRegistrations(){
        Map<String, CustomCrudServiceImpl> services = ctx.getBeansOfType(CustomCrudServiceImpl.class);
        services.forEach((name, service) -> {
            try {
                // list
                registerSubcommand("list", name,
                        (ts) -> ts.function(commandContext ->
                                service.list().stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining("\n"))));
                // get
                registerSubcommand("get", name, targetSpec -> {
                    var method = getMethod(service, "get");
                    return targetSpec.function(commandContext -> {
                                try {
                                    return method.invoke(service,
                                            service.keyFromStrings(commandContext.getRawArgs())
                                    );
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                        .and().withOption() //TODO this looks wrong
                        .label("key")
                        //TODO junk?
// doesnt work because of generic erasure (?)
//                        .type(method.getParameterTypes()[0])
                        .position(0)
                        .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                        .required();
                });
                // create
                registerSubcommand("create", name, ts -> {
                    var method = getMethod(service, "create");
                    var types = method.getParameterTypes(); //TODO due to the dto destructuring this needs to be subsumed into something like callwithconversion
                    for (int i = 0; i < types.length; i++) {
                        if (!cs.canConvert(String.class, types[i])){
                            throw new UnsupportedOperationException("Mapped command generator cant convert from String to " + types[i].getTypeName());
                        }
                    }
                    ts = ts.function(commandContext -> callWithConversion(service, commandContext, method));
                    /*for (int i = 0; i < types.length; i++){
                        ts = ts
                            .and().withOption()
                                .label(types[i].getTypeName())
                                .type(types[i])
                                .position(i)
                                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                                .required()
                                .and().withTarget(); //TODO does this work how I think it does?
                    }*/
                    return ts;
                });
                // update
                // delete
            } catch (Exception e) { //TODO
                if(e instanceof UnsupportedOperationException){
                    e.printStackTrace();
                }
                else{
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Object callWithConversion(CustomCrudServiceImpl service, CommandContext commandContext, Method method) {
        // TODO this is pretty redundant with what spring shell does internally but they dont expose it to us with .method because of the annotation filtering...
        var types = method.getParameterTypes();
        var rawArgs = commandContext.getRawArgs();
        var targetArgs = new Object[types.length];
        int currentArg = 0;
        for (int i = 0; i < types.length; i++){
            //TODO generic type information is erased so we cant tell the type like this.
            //if(types[i].getTypeName().endsWith("Dto")){ //TODO ehh...
            if(method.getName().equals("create") || method.getName().equals("update")){ //TODO ehh...
                var mapper = service.mapper;
                var subargs = new String[mapper.getStringsCount()];
                for(int j = 0; j < subargs.length; j++, currentArg++){
                    subargs[j] = rawArgs[currentArg];
                }
                var dto = mapper.dtoFromStrings(subargs);
                targetArgs[i] = dto;
            } else { //TODO dead code currently?
                targetArgs[i] = Objects.requireNonNull(cs.convert(rawArgs[currentArg++], types[i]));
            }
        }
        try {
            return method.invoke(service, targetArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getMethod(CustomCrudService service, String name) {
        return Arrays.stream(service.getClass().getMethods())
                .filter(m -> m.getName().equals(name)).toList().get(0);
    }
}
