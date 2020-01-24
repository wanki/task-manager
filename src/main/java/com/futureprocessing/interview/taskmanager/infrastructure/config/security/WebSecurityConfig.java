package com.futureprocessing.interview.taskmanager.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryRole.EDIT_TASK;
import static com.futureprocessing.interview.taskmanager.infrastructure.config.security.InMemoryRole.VIEW_TASK;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(GET, "/task**").hasAuthority(VIEW_TASK.name())
            .antMatchers(GET, "/task/**").hasAuthority(VIEW_TASK.name())
            .antMatchers(POST, "/task**").hasAuthority(EDIT_TASK.name())
            .antMatchers(PUT, "/task/**").hasAuthority(EDIT_TASK.name())
            .and()
            .csrf().disable();
    }
}
