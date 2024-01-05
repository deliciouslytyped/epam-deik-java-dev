package com.epam.training.ticketservice.lib.security;

import com.epam.training.ticketservice.lib.security.ApplicationAdminDetailsService;
import com.epam.training.ticketservice.lib.security.ApplicationUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

//TODO implement sessions and have the service layer authorize each operation on them
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    protected final ApplicationUserDetailsService us;
    protected final ApplicationAdminDetailsService as;

    AuthenticationManager commonAuth(UserDetailsService s){
        var authp = new DaoAuthenticationProvider();
        authp.setUserDetailsService(s);
        return new ProviderManager(authp);
    }
    @Bean
    AuthenticationManager userAuthenticationManager(){
        return commonAuth(us);
    }

    @Bean
    //@Primary    //TODO figure out how to not need to do this
    AuthenticationManager adminAuthenticationManager(){
        return commonAuth(as);
    }
}
