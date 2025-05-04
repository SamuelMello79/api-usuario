package dev.mello.apiusuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtil {
    // Chave secreta usada para assinar e verificar tokens JWT
    private final SecretKey secretKey;


    public JwtUtil() {
        // Gera chave secreta para o algorit de assinatura HS256
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o nome de usuário como assunto
                .setIssuedAt(new Date()) // Define a data e hora de emissão
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + 1000 * 60 * 60))
                // Define a data e hora de expiração
                .signWith(secretKey) // Assina o token com a chave
                .compact(); // constrói o token JWT
    }

    // Extrai as claims do token JWT (informação adicionais)
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                // Define a chave para validar a assinatura
                .build()
                .parseClaimsJws(token) // Analisa o token e obtém as claims
                .getBody(); // Retorna o corpo das claims
    }

    // Extrai o nome de usuário do token JWT
    public String extractUsername(String token) {
        // Obtém o assunto (nome de usuário) das claims do token
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        // Extrai o nome de usuário
        final String extractedUsename = extractUsername(token);
        // Verifica se o nome de usuário do token corresponde ao fornecido
        // e se o token não foi expirado
        return (extractedUsename.equals(username) && !isTokenExpired(token));
    }
}
