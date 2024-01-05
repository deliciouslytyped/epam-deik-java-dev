//Cannibalized from the spring source and https://stackoverflow.com/questions/68082468/spring-security-change-login-default-path/68218300#68218300
// https://stackoverflow.com/questions/40319907/spring-boot-2-different-login-pages-for-2-urls
package com.epam.training.ticketservice.presentation.web.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class CustomFormLoginConfigurer extends AbstractAuthenticationFilterConfigurer<HttpSecurity, CustomFormLoginConfigurer, UsernamePasswordAuthenticationFilter> {
    public CustomFormLoginConfigurer() {
        super(new UsernamePasswordAuthenticationFilter(), null);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        super.init(http);
        initDefaultLoginFilter(http);
    }

    private String getUsernameParameter() {
        return getAuthenticationFilter().getUsernameParameter();
    }

    private String getPasswordParameter() {
        return getAuthenticationFilter().getPasswordParameter();
    }

    @Override
    protected CustomFormLoginConfigurer loginPage(String loginPage) {
        return super.loginPage(loginPage);
    }

    private void initDefaultLoginFilter(HttpSecurity http) {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = http
                .getSharedObject(DefaultLoginPageGeneratingFilter.class);
        loginPageGeneratingFilter.setFormLoginEnabled(true);
        loginPageGeneratingFilter.setUsernameParameter(getUsernameParameter());
        loginPageGeneratingFilter.setPasswordParameter(getPasswordParameter());
        loginPageGeneratingFilter.setLoginPageUrl(getLoginPage());
        loginPageGeneratingFilter.setFailureUrl(getFailureUrl());
        loginPageGeneratingFilter.setAuthenticationUrl(getLoginProcessingUrl());
    }
}

