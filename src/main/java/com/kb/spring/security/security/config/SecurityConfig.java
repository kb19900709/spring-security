package com.kb.spring.security.security.config;


import com.kb.spring.security.security.RestAuthenticationEntryPoint;
import com.kb.spring.security.security.UserAuthenticationProvider;
import com.kb.spring.security.security.filter.JwtAuthenticationFilter;
import com.kb.spring.security.security.filter.LoginFilter;
import com.kb.spring.security.security.handler.SessionExpiredHandler;
import com.kb.spring.security.security.handler.SessionLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/app-login";

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Autowired
    private SessionExpiredHandler sessionExpiredHandler;

    @Autowired
    private SessionLogoutSuccessHandler sessionLogoutSuccessHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, getWhitelist()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .logout()
                .logoutUrl("/app-logout")
                .logoutSuccessHandler(sessionLogoutSuccessHandler)
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(sessionExpiredHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        return new LoginFilter(LOGIN_URL, authenticationManager());
    }

    private String[] getWhitelist() {
        final String LOGIN_FORWARD_URL = "/auth/login/*";
        return new String[]{LOGIN_URL, LOGIN_FORWARD_URL};
    }
}
