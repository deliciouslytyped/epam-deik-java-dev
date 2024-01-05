package com.epam.training.ticketservice;

import com.epam.training.ticketservice.support.jparepo.CustomJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import javax.persistence.EntityManagerFactory;

//TODO is it possible to override the base jparepository and use an extending interface to load it or is it absolutely necessary to override the base class in a configuration scope?
//(need to check the jpafactoryrepository source)
@Configuration
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepository.class)
@EnableAspectJAutoProxy(proxyTargetClass = true) //TODO is this doing anything for me
@EnableMethodSecurity
public class Config {
}
