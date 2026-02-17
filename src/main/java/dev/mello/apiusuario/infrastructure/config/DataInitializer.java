package dev.mello.apiusuario.infrastructure.config;

import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            try {
                log.info("Inserindo carga de dados iniciais...");

                Usuario admin = Usuario.builder()
                        .nome("Administrador")
                        .email("admin@email.com")
                        .senha(passwordEncoder.encode("admin"))
                        .build();

                if (!repository.existsByEmail(admin.getEmail())) {
                    repository.save(admin);
                }

                log.info("Carga de dados iniciais concluída com sucesso.");
            }  catch (Exception e) {
                log.error("Falha critíca ao carregar dados iniciais");
                throw new RuntimeException("Erro na inicialização dos dados");
            }
        };
    }
}
