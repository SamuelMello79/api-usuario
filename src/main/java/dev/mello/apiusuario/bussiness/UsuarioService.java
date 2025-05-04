package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.exception.ConflictException;
import dev.mello.apiusuario.infrastructure.exception.NotFoundException;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        verificaEmailExiste(usuario.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Usuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " +  id + " não encontrado!"));
    }

    public Usuario findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email: " +  email + " não encontrado"));
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            deleteById(id);
        } else {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
    }

    public void deleteByEmail(String email) {
        verificarSeEmailExiste(email);
        repository.deleteByEmail(email);
    }

    private void verificaEmailExiste(String email) {
        if (verificarSeEmailExiste(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    private boolean verificarSeEmailExiste(String email) {
        return repository.existsByEmail(email);
    }
}
