package br.com.liletbaby.back_end.security;

import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.providers.TokenCustomProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class GoogleAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TokenCustomProvider tokenCustomProvider;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SECRET_LENGTH = 32;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal(); //OIDC
        String email = oidcUser.getEmail();
        String nameCompleto = oidcUser.getFullName();
        String name = oidcUser.getGivenName();

        Usuario usuario = usuarioRepositorio.findByEmail(email); // Página google já trata a validade do e-mail
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setName(name);
            usuario.setNameCompleto(nameCompleto);
            usuario.setRoles("USER");
            usuario.setTokenValid(true);
            usuario.setOauth2Bind(true);
            usuario.setPassword(generateRandomPassword());
            usuario = usuarioRepositorio.save(usuario);
        }

        String jwtToken = tokenCustomProvider.generateToken(usuario); // Conferir

        response.setContentType("application/json");
        response.getWriter().write("{\"OAuth2 Google Token JWT\": \"" + jwtToken + "\"}");
    }

    // Não pude reutilizar o método generateSecret da classe TokenCustomProvider pelo cumprimento da senha.
    private String generateRandomPassword() {
        byte[] randomBytes = new byte[SECRET_LENGTH];
        RANDOM.nextBytes(randomBytes);
        WebSecurityConfig bcryptPassword = new WebSecurityConfig();
        return bcryptPassword.passwordEncoder().encode(Arrays.toString(randomBytes));
    }
}