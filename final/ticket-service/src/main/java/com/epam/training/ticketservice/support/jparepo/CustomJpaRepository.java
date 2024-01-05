package com.epam.training.ticketservice.support.jparepo;

import org.hibernate.Session;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.util.*;
import java.util.stream.Collectors;

// Lacking a more thorough implementation, we override individual methods we use to expose db constraint violations to reduce unnecessary db calls for initial lookup, etc (should we just be using raw-er db access at that point?)
//TODO replace the injection stuff here with a custom repositoryfactorybean class? https://stackoverflow.com/questions/45462613/how-to-inject-configuration-in-a-custom-spring-data-jpa-repository
// https://stackoverflow.com/questions/70070940/is-it-possible-to-read-property-file-values-inside-repository
public class CustomJpaRepository<T,ID> extends SimpleJpaRepository<T,ID> {
    //TODO doesnt work, as you would expect
    //@Autowired
    //private ApplicationContext ctx; // to get prop
    private final EntityManager em;
    //TODO hmm
    public final JpaEntityInformation<T,ID> entityInformation;

    public CustomJpaRepository(JpaEntityInformation<T, ID> entityInformation,
                               EntityManager entityManager) {
        super(entityInformation, entityManager);
        em = entityManager;
        this.entityInformation = entityInformation;

        attachUpdateFragmentStoredProcedure(); //TODO these probably need to be moved out of the constructor lifetime somehow?
        attachCheckConstraints();
    }

    // Not doing merge will will probably break stuff that expects save() to handle updates but it might just work for us.
    // (So don't do this in a larger application.)
    //  ... except we dont do it for delete because hibernate seems to do a bunch of object management work, making it ineffective to override, and doesnt propagate the db exception afaict?
    @Override
    @Transactional //Is this needed given that the parent has it? presumably yes
    public <S extends T> S save(S entity) {
        this.em.persist(entity);
        this.em.flush(); //TODO does this need to be here?
        return entity;
    }

    void attachUpdateFragmentStoredProcedure(){
        var entityClass = entityInformation.getJavaType();
        if (entityClass.getAnnotation(GenUpdateByEntityFragment.class) != null) {
            createNamedStoredProcedure(entityInformation.getJavaType());
        }
    }

    //TODO hibernate does have some largely unused mechanism (besides column min and max bean constraints) for
    // applying check constraits, but IIRC only to columns? //TODO recheck tables
    // Thats why I'm implementing my own
    void attachCheckConstraints(){
        var entityClass = entityInformation.getJavaType();
        var bundleAnn = entityClass.getAnnotation(CheckConstraints.class);
        var singleAnn = entityClass.getAnnotation(CheckConstraint.class);
        if (bundleAnn != null) {
            var constraints = Arrays.stream(bundleAnn.value())
                    .filter(constr -> {
                        var target = constr.driverFilter();
                        //TODO its *hibernate* dependent not h2 dependent, i keep mixing up the names
                        // TODO this is H2 dependent and needs to be refactored https://stackoverflow.com/questions/70070940/is-it-possible-to-read-property-file-values-inside-repository
                        // the problem is apparently the jparepository doesnt (TODO test again) get stuff injected normally, so
                        // we cant get the application.yaml setting without implementing a repository factory (?)
                        // so we get it from the jdbc session
                        var dbDriver = em.unwrap(Session.class).getSessionFactory().getSessionFactory().getJdbcServices().getDialect().getClass().getCanonicalName();
                        return target.equals(dbDriver) || target.isEmpty();


                    }) //also covers null
                    .collect(Collectors.toMap(CheckConstraint::name, CheckConstraint::check));
            applyCheckConstraints(constraints);
        } else if (singleAnn != null){
            applyCheckConstraints(Map.of(singleAnn.name(), singleAnn.check()));
        }
    }

    void applyCheckConstraints(Map<String, String> constraints){
        var emf = em.getEntityManagerFactory();
        var em = emf.createEntityManager(); //TODO this is also a mess, what is the correct way to do this?
        var ts = em.getTransaction();
        var tablename = entityInformation.getEntityName(); //TODO probably broken
        ts.begin();
        try {
            for(var pair : constraints.entrySet()){
                var constraintName = pair.getKey();
                var constraint = pair.getValue();
                //var prop = ctx.getEnvironment().getProperty("spring.jpa.hibernate.ddl-auto");
                //var prop = "create-drop"; //TODO
                var prop = System.getProperty("startuptype");
                if(prop != null && prop.equals("update")){
                    var sql = "alter table " + tablename + " drop constraint " + constraintName;
                    em.createNativeQuery(sql)
                            .executeUpdate(); // whee string interpolation
                    sql = "alter table " + tablename + " add constraint " + constraintName + " check (" + constraint + ")";
                    em.createNativeQuery(sql)
                            .executeUpdate(); // whee string interpolation
                }
                else {
                    var sql = "alter table " + tablename + " add constraint " + constraintName + " check (" + constraint + ")";
                    em.createNativeQuery(sql)
                            .executeUpdate(); // whee string interpolation
                }

            }
            ts.commit();
        } catch (Exception e) {
            ts.rollback();
        }
    }

    // doesnt work @Transactional //Need this to have an open hibernate session?
    public void createNamedStoredProcedure(Class entityClass){
        var emf = em.getEntityManagerFactory();
        var em = emf.createEntityManager(); //TODO this is also a mess, what is the correct way to do this?
        var ts = em.getTransaction();
        ts.begin();
        try {
            //TODO I think this has to be an implementation specific api
            var sess = em.unwrap(Session.class).getSession();
            var pc = sess.createStoredProcedureCall("nonempty_update_by_key");
            //TODO not sure what repositories do here
            //TODO this is probably going to break on composite keys
            /*
            //We have to use named parameters because otherwise we dont know how to match parameters to fields in UpdateByEntityFragmentImpl.update(T e)
            //TODO the thing is, I don't know if these are reliably mapped to positional arguments
            pc.registerStoredProcedureParameter("tablename", String.class, ParameterMode.IN);
            pc.setParameter("tablename", entityClass.getName());
            */
            pc.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            pc.setParameter(1, entityClass.getName());

            /*for (var f : entityClass.getDeclaredFields()) {
                var name = f.getName();
                pc.registerStoredProcedureParameter(name + "colname", String.class, ParameterMode.IN);
                pc.setParameter(name + "colname", name);
                pc.registerStoredProcedureParameter(name, Object.class, ParameterMode.IN);
            }*/
            var fields = entityClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                //TODO use explicit naming on the entity instead of dealign with emulating the implicit naming strategy?
                var name = getDbName(fields[i].getName());
                pc.registerStoredProcedureParameter(2*i+1+1, String.class, ParameterMode.IN);
                pc.setParameter(2*i+1+1, name);
                pc.registerStoredProcedureParameter(2*i+1+1+1, fields[i].getType(), ParameterMode.IN); //This is going to break
            }


            // TODO / note / hack the hibernate procedure call parametermemento implemetation forces positional argumens because it takes an int for position??
            /*final ArrayList<ProcedureParameterImplementor<?>> params = new ArrayList<>();
            var procMetadata = ((ProcedureCallImpl<?>)pc).getParameterMetadata();
            procMetadata.visitRegistrations(qp -> params.add((ProcedureParameterImplementor<?>) qp)); //TODO relies on iteration order being consistent
            for(int i = 0; i < params.size(); i++){
                var field = ProcedureParameterImpl.class.getDeclaredField("position");
                field.setAccessible(true);
                field.set(params.get(i), i + 1);
            }*/

            sess.getSessionFactory().addNamedQuery(entityClass.getSimpleName() + ".update", pc);
            ts.commit(); // Does this even make sense for DDL stuff in hibernate?
        } catch (Throwable e) {
            ts.rollback();
            throw new RuntimeException(e);
        }
    }

    private static String getDbName(String name) {
        return (new CamelCaseToUnderscoresNamingStrategy())
                .toPhysicalColumnName(new Identifier(name, false), null)
                .getCanonicalName(); //Probably the wrong method but close enough
    }
}