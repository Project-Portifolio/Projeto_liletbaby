package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.WebSecurityConfig;
import br.com.liletbaby.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * LoginController class.
 *
 * @author Wender Couto
 * @author João Abreu
 * @since 0.0.1-SNAPSHOT
 */

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    WebSecurityConfig webSecurityConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username"), password = payload.get("password");
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Dados inválidos.");
        }
        //Stack overflow here, security calling infinite.
        return ResponseEntity.ok("Credenciais Válidas." + "\n" );
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload){
        String username = payload.get("username"), password = payload.get("password");

        if (username.isBlank() || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        String hashedPassword = webSecurityConfig.passwordEncoder().encode(password);
        Usuario novoUsuario = new Usuario(username, hashedPassword);
        this.usuarioRepositorio.save(novoUsuario);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!");
    }
}