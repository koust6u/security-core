package com.example.securitycore.security.configs;

import com.example.securitycore.handler.CustomAccessDeniedHandler;
import com.example.securitycore.handler.CustomAuthenticationSuccessHandler;
import com.example.securitycore.security.provider.CustomAuthenticationProvider;
import com.example.securitycore.security.service.AccountContext;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.security.cert.Extension;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    private final AuthenticationDetailsSource authenticationDetailsSource;
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
          http
                  .authorizeHttpRequests((auth) ->
                          auth
                                  .requestMatchers("/", "/users", "/login*", "/login/**").permitAll()
                                  .requestMatchers("/mypage").hasRole("USER")
                                  .requestMatchers("/messages").hasRole("MANAGER")
                                  .requestMatchers("/config").hasRole("ADMIN")
                                  .anyRequest().authenticated())
                  .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login_proc")
                          .authenticationDetailsSource(authenticationDetailsSource)
                          .successHandler(authenticationSuccessHandler)
                          .failureHandler(authenticationFailureHandler)
                          .defaultSuccessUrl("/").permitAll())
                  .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()));

          return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();
        customAccessDeniedHandler.setErrorPage("/denied");

        return customAccessDeniedHandler;
    }


/*
    @Bean
    public UserDetailsService user(){
        String password = passwordEncoder().encode("1111");
        UserDetails user = User.builder()
                .username("user")
                .password(password)
                .roles("USER").build();
        UserDetails manager = User.builder()
                .username("manager")
                .password(password)
                .roles("MANAGER", "USER").build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(password)
                .roles("ADMIN", "MANAGER", "USER").build();
        return new InMemoryUserDetailsManager(user, manager, admin);
    }
*/

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

