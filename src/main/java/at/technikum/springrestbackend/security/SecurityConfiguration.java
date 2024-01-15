package at.technikum.springrestbackend.security;

import at.technikum.springrestbackend.security.jwt.JwtAuthenticationFilter;
import at.technikum.springrestbackend.security.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .cors().configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");
                    return corsConfiguration;
                })
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/auth/token").permitAll()
                                .requestMatchers("/brands").permitAll()
                                .requestMatchers("/brand/{name}").permitAll()
                                .requestMatchers("/addbrand").hasRole("admin")
                                .requestMatchers("/deletebrand/{name}").hasRole("admin")
                                .requestMatchers("/updatebrand/{name}").hasRole("admin")
                                .requestMatchers("/phones").permitAll()
                                .requestMatchers("/phone{id}").permitAll()
                                .requestMatchers("/phones/*").permitAll()
                                .requestMatchers("/addPhones").hasRole("admin")
                                .requestMatchers("/deletePhone/{id}").hasRole("admin")
                                .requestMatchers("/updatePhone/{id}").hasRole("admin")
                                .requestMatchers("/orders").hasRole("admin")
                                .requestMatchers("/orders/{id}").hasAnyRole("admin", "user")
                                .requestMatchers("/orders/{user}").hasAnyRole("admin", "user")
                                .requestMatchers("/createOrder").hasAnyRole("admin", "user")
                                .requestMatchers("/deleteOrder/{orderId}").hasRole("admin")
                                .requestMatchers("/users").hasRole("admin")
                                .requestMatchers("/users/*").hasRole("admin")
                                .requestMatchers("user/role/{username}").permitAll() //do need for login or navbar in frontend?
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/deleteUser/{id}").hasAnyRole("admin", "user")
                                .requestMatchers("/updateUser/{name}").hasAnyRole("admin", "user")
                                .anyRequest().authenticated()
                );

        httpSecurity.httpBasic(basic -> {});

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }


}