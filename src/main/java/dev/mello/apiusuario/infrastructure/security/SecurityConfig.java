package dev.mello.apiusuario.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String SECURITY_SCHEME = "bearerAuth";

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Configuração do filtro de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                // Desativa proteção CRSF para APIs REST
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/usuario/endereco/**").permitAll()
                        // Permite acesso ao endpoint de login sem autentic.
                        .requestMatchers("/usuario/login").permitAll()
                        // Permite acesso ao endpoint POST /usuario sem autentic.
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        // Requer autentic. para qualquer endpoint /usuario/**
                        .requestMatchers("/usuario/**").authenticated()
                        // Requer autentic. para todas as outras requisições
                        .anyRequest().authenticated()
                )
                // Configura a política de sessão para STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Adiciona o filtro JWT antes do filtro
                .addFilterBefore(jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter.class);
        // Retorna a configuração do filtro de segurança contruída
        return http.build();
    }

    // Configura o passwordEncoder para criptografar as senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura o AuthenticationManager usando o AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Obtém e retorna o AuthenticationManager
        // de configuração de autenticação
        return authenticationConfiguration.getAuthenticationManager();
    }
}
