package com.epam.training.ticketservice.lib.security.aspects;

import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;

@Configuration
public class Config2 {
    @Bean
    public BeanPostProcessor annPostProcessorThing(){
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                var clss = bean.getClass();
                var annAbove = AnnotatedElementUtils.findMergedAnnotation(clss, DefaultPrivilegedBase.class);
                var objAnn = AnnotationUtils.findAnnotation(clss, DefaultPrivilegedBase.class);
                Annotation ann;
                if(objAnn.isPresent()) {
                    ann = objAnn.get();
                } else if (annAbove != null){
                    ann = annAbove;
                } else {
                    return bean;
                }

                return bean;
            }
        };
    }
}
