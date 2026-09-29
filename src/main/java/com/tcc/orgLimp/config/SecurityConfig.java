package com.tcc.orgLimp.config;

import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {

            String role = authentication.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority();

            // Busca o usuário autenticado no banco
            String email = authentication.getName();

            Usuario usuario = usuarioRepository
                    .findByEmail(email)
                    .orElse(null);

            // Salva o usuário na sessão
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);

            // Redirecionamento conforme o perfil
            if ("ROLE_GERENTE".equals(role)) {

                response.sendRedirect("/gerente/dashboard");

            } else if ("ROLE_SUPERVISOR".equals(role)) {

                response.sendRedirect("/supervisor/dashboard");

            } else {

                response.sendRedirect("/");

            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/error"
                ).permitAll()

                .requestMatchers(HttpMethod.POST, "/login").permitAll()

                .requestMatchers("/gerente/**")
                    .hasRole("GERENTE")

                .requestMatchers("/supervisor/**")
                    .hasRole("SUPERVISOR")

                .anyRequest()
                    .authenticated()
            )

            .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .usernameParameter("email")

                .passwordParameter("senha")

                .successHandler(authenticationSuccessHandler())

                .permitAll()
            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout")

                .permitAll()
            );

        return http.build();
    }
}