package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Produto;
import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.ProdutoRepositorio;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.providers.TokenCustomProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProdutosController class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@RestController
@RequestMapping
public class HomeController {

    @Autowired
    ProdutoRepositorio produtoRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    TokenCustomProvider tokenService;

    @GetMapping("/home")
    public ResponseEntity<?> home() {
        List<Produto> produtos = produtoRepositorio.findAll();
        return ResponseEntity.ok().body("Resposta: " + produtos);
    }
}