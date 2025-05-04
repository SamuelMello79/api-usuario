package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.bussiness.dto.EnderecoDTO;
import dev.mello.apiusuario.bussiness.dto.TelefoneDTO;
import dev.mello.apiusuario.bussiness.dto.UsuarioDTO;
import dev.mello.apiusuario.bussiness.mapper.UsuarioMapper;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.exception.ConflictException;
import dev.mello.apiusuario.infrastructure.exception.NotFoundException;
import dev.mello.apiusuario.infrastructure.repository.EnderecoRepository;
import dev.mello.apiusuario.infrastructure.repository.TelefoneRepository;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import dev.mello.apiusuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
        verificaEmailExiste(usuarioDTO.getEmail());
        Usuario usuario = mapper.toEntity(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return mapper.toDto(usuarioRepository.save(usuario));
    }

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public UsuarioDTO findById(Long id) {
        return mapper.toDto(usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " +  id + " não encontrado!")));
    }

    public UsuarioDTO findByEmail(String email) {
        return mapper.toDto(usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email: " +  email + " não encontrado")));
    }

    public void deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            deleteById(id);
        } else {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
    }

    public void deleteByEmail(String email) {
        verificarSeEmailExiste(email);
        usuarioRepository.deleteByEmail(email);
    }

    private void verificaEmailExiste(String email) {
        if (verificarSeEmailExiste(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    private boolean verificarSeEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO atualizaDados(String token, UsuarioDTO usuarioDTO) {
        // Aqui buscamos o email do usuário através do token (tirar a obrigatoriedade do email)
        String email = jwtUtil.extractUsername(token.substring(7));

        // Cripografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        // Busca os dados do usuário no db
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Email não localizado"));

        // Mesclou os dados do DTO com os dados do db
        Usuario usuario = mapper.updateUsuario(usuarioDTO, usuarioEntity);
        return mapper.toDto(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(()-> new NotFoundException("Id do endereço " + idEndereco + " não localizado"));

        Endereco endereco = mapper.updateEndereco(enderecoDTO, entity);
        return mapper.toEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new NotFoundException("Id do telefone " + idTelefone + " não localizado"));

        Telefone telefone = mapper.updateTelefone(telefoneDTO, entity);
        return mapper.toTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO adicionaEndereco(String token, EnderecoDTO enderecoDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não localizado"));

        Endereco endereco = mapper.toEndereco(enderecoDTO, usuario.getId());
        return mapper.toEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO adicionaTelefone(String token, TelefoneDTO telefoneDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não localizado"));

        Telefone telefone = mapper.toTelefone(telefoneDTO, usuario.getId());
        return mapper.toTelefoneDTO(telefoneRepository.save(telefone));
    }
}
