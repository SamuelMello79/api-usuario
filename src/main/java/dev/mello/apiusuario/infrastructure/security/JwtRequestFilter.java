package dev.mello.apiusuario.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Método chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) throws
            ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        // verifica se o cabeçalho existe e começa com "Bearer "
        if (authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ")) {
            // Extrai o token do JWT do cabeçalho
            final String token = authorizationHeader.substring(7);
            // Extrai o nome de usuário do token JWT
            final String username = jwtUtil.extractUsername(token);

            // Se o nome não for nulo e se não estiver autenticado ainda
            if(username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário a partir do nome
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);
                // Valida o token JWT
                if(jwtUtil.validateToken(token, username)) {
                    // Cria um objeto de autenticação com as informações
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    // Define a autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(
                            authentication);
                }
            }
        }

        // Continua a cadaeia de filtros, permitindo que a requisição prossiga
        chain.doFilter(request, response);
    }
}
