package dev.mello.apiusuario.controller;

import dev.mello.apiusuario.bussiness.UsuarioService;
import dev.mello.apiusuario.bussiness.dto.UsuarioDTO;
import dev.mello.apiusuario.infrastructure.entity.Usuario;
import dev.mello.apiusuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.salvarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.email(),
                        usuarioDTO.senha()
                )
        );
        String token = "Bearer " + jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(token);
    }

    @GetMapping
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarPorEmail(@PathVariable String email) {
        service.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
