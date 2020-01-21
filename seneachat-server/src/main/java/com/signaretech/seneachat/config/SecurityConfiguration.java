package com.signaretech.seneachat.config;

import com.signaretech.seneachat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/dashboard/**").hasAuthority("SELLER")
                .antMatchers("/web/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .and().formLogin().loginPage("/web/login")
                .defaultSuccessUrl("/web/dashboard")
                .and().logout().logoutUrl("/web/logout")
                .logoutSuccessUrl("/")
                .and().csrf().disable();
/*
        http.antMatcher("/web/dashboard/**")
                .authorizeRequests().anyRequest().hasRole("SELLER")
                .and().formLogin().loginPage("/web/loginn").permitAll().and().csrf().disable();*/
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager signareAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}