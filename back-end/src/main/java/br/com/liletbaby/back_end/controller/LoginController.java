package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Carrinho;
import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.WebSecurityConfig;
import br.com.liletbaby.back_end.services.DataValidatorService;
import br.com.liletbaby.back_end.services.TokenService;
import br.com.liletbaby.back_end.utils.NumericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        String username = payload.get("username");
        String password = payload.get("password");

        String parsedData = DataValidatorService.validator(username, password);
        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        List<Usuario> usuarios = usuarioRepositorio.findByName(username);
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }

        Usuario usuarioAutenticado = null;
        for (Usuario usuario : usuarios) {
            if (webSecurityConfig.passwordEncoder().matches(password, usuario.getPassword())) {
                usuarioAutenticado = usuario;
                break;
            }
        }

        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }

        String token = tokenService.generateToken(usuarioAutenticado);

        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Credenciais Válidas.");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) { // controle de usuário se já foi cadastrado antes faltando
        String username = payload.get("username"), password = payload.get("password");

        String parsedData = DataValidatorService.validator(username, password);

        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        String hashedPassword = webSecurityConfig.passwordEncoder().encode(password);
        Usuario novoUsuario = new Usuario(username, hashedPassword);

        novoUsuario.setRole("USER,USER");
        this.usuarioRepositorio.save(novoUsuario);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!");
    }

    @PutMapping("/user/update")
    @ResponseBody
    public ResponseEntity<?> userUpdate(@RequestBody Map<String, String> payload) {
        String username = payload.get("username"), fullName = payload.get("fullname"), email = payload.get("mail");
        Integer userID = NumericConverter.safeParseInteger(payload.get("userID"));

        String parsedData = DataValidatorService.validator(username, fullName, email);

        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        Usuario updatingUser = usuarioRepositorio.findByUserId(userID);

        updatingUser.setName(username);
        updatingUser.setNameCompleto(fullName);
        updatingUser.setEmail(email);
        this.usuarioRepositorio.save(updatingUser);
        return ResponseEntity.ok().body("Dados cadastrais atualizados com sucesso!");
    }
}