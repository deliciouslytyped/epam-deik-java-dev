package com.epam.training.ticketservice.presentation.web.configuration;

import com.epam.training.ticketservice.lib.security.ApplicationAdminDetailsService;
import com.epam.training.ticketservice.lib.security.ApplicationUserDetailsService;
import com.epam.training.ticketservice.lib.security.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
//TODO fix logout url

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    protected final ApplicationUserDetailsService us;
    protected final ApplicationAdminDetailsService as;
    AuthenticationProvider commonAuth(UserDetailsService s){
        var authp = new DaoAuthenticationProvider();
        authp.setUserDetailsService(s);
        return authp;
    }

    @Primary //TODO we really shouldnt need to do this? workaround for now
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(new DaoAuthenticationProvider());
        //return null;
    }


    // 1. auth admin area
    // 2. auth user area
    // 3. allow everything else
    @Bean
    @Order(1)//TODO wrong?
    //TODO check if this even works / like i want it to
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception { //TODO does this override the default filter chain?
        http
            .antMatcher("/admin/**")
            //TODO damn thing precludes consistency, cant use the same enum in both places because it prepends the ROLE_ prefix (otherwise i wouldnt need to prepend the enum either...)
            // Roles.ROLE_ADMIN
            .authorizeRequests(auth -> auth.antMatchers("/admin/**","/static/**").hasRole("ADMIN")) //TODO static pointless here
            .authenticationProvider(commonAuth(as)) //TODO check and make sure these are scoped and not just adding both providers globally
            //.formLogin()
            .apply(new CustomFormLoginConfigurer())
                .failureUrl("/admin/login?failed")
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login/process")
            .and().logout(c -> c.logoutUrl("/user/logout"));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception { //TODO does this override the default filter chain?
        http
            .antMatcher("/user/**")
            .authorizeRequests(auth -> auth.antMatchers("/user/**", "/static/**").hasRole("USER")) //TODO static pointless here
            .authenticationProvider(commonAuth(us))
            //.formLogin()
            .apply(new CustomFormLoginConfigurer())
                .failureUrl("/user/login?failed")
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login/process")
            .and().logout(c -> c.logoutUrl("/user/logout"));
        return http.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception { //TODO does this override the default filter chain?
        http
            .authorizeRequests(auth -> auth.anyRequest().permitAll()); //TODO whats the difference between anonymous and permitall?
        return http.build();
    }

}

// move to securityconfiguration
    /*
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        return users;
    }
 */
    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        return http.build();
    }*/