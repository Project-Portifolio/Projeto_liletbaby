package br.com.liletbaby.back_end.security;

import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.providers.TokenCustomProvider;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * SecurityFilter class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenCustomProvider tokenService;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);
        if (token != null) {
            String userId = tokenService.validateToken(token);
            try {
                UserDetails user = usuarioRepositorio.findByUserId(Integer.valueOf(userId));

                if (user != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (TokenExpiredException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro, Token JWT expirado ou ausente. Msg retorno: " + userId);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}