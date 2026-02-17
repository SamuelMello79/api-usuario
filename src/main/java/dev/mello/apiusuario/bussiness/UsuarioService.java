package dev.mello.apiusuario.bussiness;

import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.bussiness.mapper.UsuarioConverter;
import dev.mello.apiusuario.infrastructure.entity.Endereco;
import dev.mello.apiusuario.infrastructure.entity.Telefone;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.exception.BadRequestException;
import dev.mello.apiusuario.infrastructure.exception.ConflictException;
import dev.mello.apiusuario.infrastructure.exception.NotFoundException;
import dev.mello.apiusuario.infrastructure.exception.UnathorizedException;
import dev.mello.apiusuario.infrastructure.repository.EnderecoRepository;
import dev.mello.apiusuario.infrastructure.repository.TelefoneRepository;
import dev.mello.apiusuario.infrastructure.repository.UsuarioRepository;
import dev.mello.apiusuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.jsonwebtoken.lang.Assert.notNull;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Usuario salvaUsuario(Usuario usuario) {
        return usuarioRepository.saveAndFlush(usuario);
    }

    public Endereco salvaEndereco(Endereco endereco) {
        return enderecoRepository.saveAndFlush(endereco);
    }

    public Telefone salvaTelefone(Telefone telefone) {
        return telefoneRepository.saveAndFlush(telefone);
    }

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        try {
            notNull(usuarioRequestDTO, "Os dados do usuário são obrigatórios");
            verificaEmailExiste(usuarioRequestDTO.email());
            Usuario usuario = mapper.toEntity(usuarioRequestDTO);
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return mapper.toDto(salvaUsuario(usuario));
        } catch (Exception e) {
            throw new BadRequestException("Falha ao salvar dados de usuário", e);
        }

    }

    public String autenticarUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuarioRequestDTO.email(),
                            usuarioRequestDTO.senha()
                    )
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException ex) {
            throw new UnathorizedException("Usuário com email ou senha inválidos", ex);
        } catch (UsernameNotFoundException ex) {
            throw new UnathorizedException("Usuário com email não encontrado", ex);
        } catch (AuthorizationDeniedException ex) {
            throw new UnathorizedException("Usuário não possuí as permissões necessárias", ex);
        }
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        return mapper.toDto(usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " + id + " não encontrado!")));
    }

    public UsuarioResponseDTO findByEmail(String email) {
        return mapper.toDto(usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email: " + email + " não encontrado")));
    }

    public void deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
    }

    public void deleteByEmail(String email) {
        verificarSeEmailExiste(email);
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioResponseDTO atualizaDados(String token, UsuarioRequestDTO usuarioRequestDTO) {
        try {
            notNull(usuarioRequestDTO, "Os dados do usuário são obrigatórios");

            // Aqui buscamos o email do usuário através do token (tirar a obrigatoriedade do email)
            String email = jwtUtil.extractUsername(token.substring(7));

            // Cripografia de senha
            String senha = usuarioRequestDTO.senha() != null ? passwordEncoder.encode(usuarioRequestDTO.senha()) : null;

            // Busca os dados do usuário no db
            Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Email não localizado"));

            // Mesclou os dados do DTO com os dados do db
            Usuario usuario = mapper.updateUsuario(usuarioRequestDTO, usuarioEntity, senha);
            return mapper.toDto(salvaUsuario(usuario));
        } catch (Exception e) {
            throw new BadRequestException("Falha ao gravar dados de usuário", e);
        }

    }

    public EnderecoResponseDTO atualizaEndereco(Long idEndereco, EnderecoRequestDTO enderecoRequestDTO) {
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new NotFoundException("Id do endereço " + idEndereco + " não localizado"));

        Endereco endereco = mapper.updateEndereco(enderecoRequestDTO, entity);
        return mapper.toEnderecoDTO(salvaEndereco(endereco));
    }

    public TelefoneResponseDTO atualizaTelefone(Long idTelefone, TelefoneRequestDTO telefoneRequestDTO) {
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new NotFoundException("Id do telefone " + idTelefone + " não localizado"));

        Telefone telefone = mapper.updateTelefone(telefoneRequestDTO, entity);
        return mapper.toTelefoneDTO(salvaTelefone(telefone));
    }

    public EnderecoResponseDTO adicionaEndereco(String token, EnderecoRequestDTO enderecoRequestDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não localizado"));

        Endereco endereco = mapper.toEndereco(enderecoRequestDTO, usuario.getId());
        try {
            Endereco enderecoSaved = salvaEndereco(endereco);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar", e);
        }


        return mapper.toEnderecoDTO(salvaEndereco(endereco));
    }

    public TelefoneResponseDTO adicionaTelefone(String token, TelefoneRequestDTO telefoneRequestDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não localizado"));

        Telefone telefone = mapper.toTelefone(telefoneRequestDTO, usuario.getId());
        return mapper.toTelefoneDTO(salvaTelefone(telefone));
    }

    public TelefoneResponseDTO buscaTelefonePorId(Long idTelefone) {
        return mapper.toTelefoneDTO(telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new NotFoundException("Id do telefone " + idTelefone + " não localizado")));
    }

    public EnderecoResponseDTO buscaEndereceoPorId(Long idEndereco) {
        return mapper.toEnderecoDTO(enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new NotFoundException("Id do endereço " + idEndereco + " não localizado")));
    }

    private void verificaEmailExiste(String email) {
        if (verificarSeEmailExiste(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    private boolean verificarSeEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
