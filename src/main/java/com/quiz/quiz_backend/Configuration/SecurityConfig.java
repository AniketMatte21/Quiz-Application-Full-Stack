package com.quiz.quiz_backend.Configuration;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "/api/doSignup",
                        "/api/validate",
                        "/doLogin",
                        "/js/**",
                        "/css/**",
                        "/authentication/**",
                        "/home-page/**",
                        "/doLogout",
                        "/quiz/**",
                        "/images/**"
//                        "/Quiz-questions-page/**",
//                                "/validateAns/**",
//                        ,"/validateAns/getScore",
//                        ,"/payment/**"
                        ,"/file/**",
//                        ,"/purchase/**",
                                "/check/**"
//                        ,"/quizPurchase/**"
//                        ,"performers/**"
                       )
                        .permitAll().anyRequest().authenticated())

                .formLogin(form->
                    form.loginPage("/authentication/auth.html?mode=login")
                            .usernameParameter("username")
                            .loginProcessingUrl("/doLogin")
                            .defaultSuccessUrl("/home-page/index.html",true)
                            .failureUrl("/authentication/auth.html?mode=login&error=true")
                            .permitAll()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // THIS IS IMPORTANT: redirect if accessed via browser
                            if (request.getHeader("Accept") != null && request.getHeader("Accept").contains("text/html")) {
                                response.sendRedirect("/authentication/auth.html?mode=login");
                            } else {
                                // If it's an API (e.g., Accept: application/json), don't redirect
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setHeader("WWW-Authenticate", ""); // prevent popup
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\":\"Unauthorized\"}");
                            }
                        })
                )



                .logout(lg->
                        lg.logoutUrl("/doLogout")
                                .logoutSuccessUrl("/authentication/auth.html?mode=login"));



        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http:// 192.168.137.177:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
