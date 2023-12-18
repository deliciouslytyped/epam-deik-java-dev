package com.epam.training.ticketservice;

import com.epam.training.ticketservice.support.CustomJpaRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//TODO is it possible to override the base jparepository and use an extending interface to load it or is it absolutely necessary to override the base class in a configuration scope?
//(need to check the jpafactoryrepository source)
@Configuration
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepository.class)
public class Config {
}
