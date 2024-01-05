package com.epam.training.ticketservice.lib.security.aspects;

//TODO this is a mess

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.util.MethodInvocationUtils;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * This aspect provides a mechanism to set a required PreAuthorize() authentication check
 * for a given subset of class methods, which can be overridden in a subclass.
 * Spring Security annotations do not support such overriding.
 *
 * This is used make CRUD operations of CustomCrudService @IsAuthedAdmin by default and
 * overridable to @IsAuthedU    ser in subclasses.
 */
//@Aspect
//@Component
//TODO check operation with trace
public class DefaultPrivilegedAspect implements Ordered {
    private Set<String> checkedNames;//TODO

    // TODO spring docs say argnames isnt needed but intellij complains?
    // https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/advice.html#aop-ataspectj-advice-params-names-explicit
    //TODO problem here is preauthorize might run before we do, what to do? TODO it might not use aspects so maybe its fine
    // Note, we cant use @annotation(preAuthorize) because we need to check meta annotations too

    // we search for all classes and subclasses of a @DefaultPrivileged annotated class with @within,
    // and then we run our own security check if it is not annotated with @PreAuthorize
    // otherwise if it is, we do no processing and spring will do the preauthorize processing
    //
    // this way anything annotated with preauthorize should run the default preauthorize behavior

    //TODO there's gotta be some standard way for dealing with overriding annotations
    // Do these get called multiple times for everythng in the hierarchy?

    // TODO ok this whole thing is iffy, since aop is implemented with proxying (which is a relatively comprehensible model?)
    //   that should mean classes being extended should have aop activate on superclass methods? (Well, maybe for unrelated reasons)
    //   in other words the security annotations should be additive? (by default machanistically at least)
    //   Solving this would require keeping some sort of state regarding the execution path and whether the method call has been authenticated?
    //   this would lead to possible security issues with accidentally permitting more permissive permissions traversals?
    //  But then how do I do my overriding crud repository permissions thing?

    //TODO the next easiest approach where annotations desugar to the same annotation, and just find the next-one-up will probably break with super calls?

    // manually travers the hier
    // if method or any ancestor has preauth before any of my annotations igore it. preauth cannot be overridden by mine because id have to somehow disable it (?)
    //traversing the hier use the first of my annots that we run into. assume no tie breaking is necessary for implementation simplicity. (upwards dfs left-to-right tree search? - should be in java subclass/interface resolution order actually)
    // The problem is i dont see downwards and idk what spring aop / aspectj do with that --- super calls dont know theyre being called from a context that already knows its authorized?
    //  - maybe this is solved by the "proxying doesnt wrap internal calls" thing though? - that would probably explain why i didnt run into it already and i think this simplifis everything
    @Around(value = "@within(classCheck)", argNames = "jp,classCheck")
    public Object applySecurityCheck(ProceedingJoinPoint jp, DefaultUserPrivileged classCheck) throws Throwable {
        System.out.println("userauthentication");
        return withRole(jp, (Object)classCheck, "USER");
    }

    @Around(value = "@within(classCheck)", argNames = "jp,classCheck")
    public Object applySecurityCheck(ProceedingJoinPoint jp, DefaultPrivileged classCheck) throws Throwable {
        System.out.println("adminauthentication");
        return withRole(jp, (Object)classCheck, "ADMIN");
    }

    public Object withRole(ProceedingJoinPoint jp, Object classCheck, String role) throws Throwable {
        var method = ((MethodSignature)(jp.getSignature())).getMethod(); //TODO probably wrong
        //Check for method level PreAuthorize annotation, or a meta-annotation that uses PreAuthorize
        // If a PreAuthorize annotation exists (a subclass override) do nothing
        if (isOverridden(method)) {
            System.out.println("is overridden by default behavior");
            return jp.proceed();
        }

        //TODO can I just use AuthorizationManagerBeforeMethodInterceptor.preAuthorize()? somehow?
        // If it doesn't exist, (our class level annotation exists) do our own checks (preferably as close to the default preauthorize behaviro as possible
        if (classCheck != null) { //&& isCheckedMethod(method, null)// classCheck.checkedMethods())){ // currently unused because annotations dont have inheritance
            System.out.println("classecuredcheckmethod");
            var invocation = MethodInvocationUtils.create(jp.getTarget(), method.getName(), jp.getArgs()); //TODO it seems a waste to get all this information when *we already have the method*
            var preInvocationAdvice = new ExpressionBasedPreInvocationAdvice(); //TODO not sure this is the one I want but it looks reasonable
            var attribute = (new ExpressionBasedAnnotationAttributeFactory(new DefaultMethodSecurityExpressionHandler()))
                    .createPreInvocationAttribute(null, null, "hasAuthority(T(com.epam.training.ticketservice.lib.security.Roles)." + role + ")");

            //TODO probably shouldnt need the invocation but i dont think i can pass null?
            if (!preInvocationAdvice.before(
                    SecurityContextHolder.getContext().getAuthentication(),
                    invocation,//TODO is this correct?
                    attribute)) {
                throw new SecurityException("Access denied"); //TODO is this correct?
            }
        }

        return jp.proceed();
    }


    public boolean isOverridden(Method method){
        return AnnotatedElementUtils.findMergedAnnotation(method, PreAuthorize.class) != null;
    }
    public boolean isCheckedMethod(Method method, String[] names){
        if (names.length != 0){
            return Arrays.stream(names).anyMatch(name -> method.getName().equals(name));
        } else {
            return true;
        }
    }

    /**
     * TODO I'm not sure this approach is correct.
     * TODO wait do I even need this?
     *
     * We need to run before any existing preAuthorize annotations / the authorizationaspect for it.
     *
     * see probably AuthorizationManagerBeforeMethodInterceptor, AuthorizationInterceptorsOrder, AopConfigUtils, AspectJAwareAdvisorAutoProxyCreator
     * @return
     */
    @Override
    public int getOrder() {
        //get the ordering right by deriving the vector :P
        // note the interval in the enum is set to 100 so there is some space between
        return AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() + Integer.signum(AuthorizationInterceptorsOrder.PRE_FILTER.getOrder() - AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder()) ;
    }
}
