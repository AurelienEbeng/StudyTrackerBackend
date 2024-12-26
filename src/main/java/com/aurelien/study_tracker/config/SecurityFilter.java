package com.aurelien.study_tracker.config;

import com.aurelien.study_tracker.user.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/registerUser").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/registerDemoUser").hasAuthority(Permission.DEMO_USER_CREATE.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/verify").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/resend").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/forgotPassword").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/resetPassword").permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET,"/user/profile").hasAuthority(Permission.USER_DETAILS_GET_FOR_ID.name());

                    authConfig.requestMatchers(HttpMethod.POST,"/task/create").hasAuthority(Permission.TASK_CREATE.name());
                    authConfig.requestMatchers(HttpMethod.POST,"/task/update").hasAuthority(Permission.TASK_UPDATE.name());
                    authConfig.requestMatchers(HttpMethod.GET,"/task/getAll").hasAuthority(Permission.TASK_GET_ALL_FOR_USER_ID.name());
                    authConfig.requestMatchers(HttpMethod.GET,"/task/getAllActive").hasAuthority(Permission.TASK_GET_ALL_FOR_USER_ID.name());


                    authConfig.requestMatchers(HttpMethod.POST,"/session/create").hasAuthority(Permission.SESSION_CREATE.name());

                    authConfig.anyRequest().denyAll();


                });

        return http.build();

    }
}