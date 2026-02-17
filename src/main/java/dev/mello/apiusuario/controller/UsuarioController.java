package dev.mello.apiusuario.controller;

import dev.mello.apiusuario.bussiness.UsuarioService;
import dev.mello.apiusuario.bussiness.ViaCepService;
import dev.mello.apiusuario.bussiness.dto.request.EnderecoRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.TelefoneRequestDTO;
import dev.mello.apiusuario.bussiness.dto.request.UsuarioRequestDTO;
import dev.mello.apiusuario.bussiness.dto.response.EnderecoResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.TelefoneResponseDTO;
import dev.mello.apiusuario.bussiness.dto.response.UsuarioResponseDTO;
import dev.mello.apiusuario.infrastructure.client.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ViaCepService viaCepService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(usuarioRequestDTO));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity.ok(usuarioService.autenticarUsuario(usuarioRequestDTO));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoResponseDTO> adicionaEndereco(@RequestBody EnderecoRequestDTO enderecoRequestDTO,
                                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.adicionaEndereco(token, enderecoRequestDTO));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneResponseDTO> adicionaTelefone(@RequestBody TelefoneRequestDTO telefoneRequestDTO,
                                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.adicionaTelefone(token, telefoneRequestDTO));
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> atualizaDadoUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO,
                                                                  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDados(token, usuarioRequestDTO));
    }

    // Atualiza um endereço do usuário pelo EndereçoID
    @PutMapping("/endereco")
    public ResponseEntity<EnderecoResponseDTO> atualizaEndereco(@RequestBody EnderecoRequestDTO enderecoRequestDTO,
                                                                @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoRequestDTO));
    }

    // Atualiza um telefone do usuário pelo TelefoneID
    @PutMapping("/telefone")
    public ResponseEntity<TelefoneResponseDTO> atualizaTelefone(@RequestBody TelefoneRequestDTO telefoneRequestDTO,
                                                                @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, telefoneRequestDTO));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deletarPorEmail(@PathVariable String email) {
        usuarioService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable("cep") String cep) {
        return ResponseEntity.ok(viaCepService.buscarDadosEnedereco(cep));
    }
}
