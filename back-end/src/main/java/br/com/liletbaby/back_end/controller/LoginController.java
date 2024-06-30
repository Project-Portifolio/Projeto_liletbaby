package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import br.com.liletbaby.back_end.security.WebSecurityConfig;
import br.com.liletbaby.back_end.security.providers.TokenCustomProvider;
import br.com.liletbaby.back_end.services.DataValidatorService;
import br.com.liletbaby.back_end.utils.NumericConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    TokenCustomProvider tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        String parsedData = DataValidatorService.validator(username, password);

        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        UserDetails userDetails = usuarioRepositorio.findByName(username);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }

        String token = tokenService.generateToken((Usuario) userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Credenciais Válidas.");
        response.put("token", token); //Preferi seguir o padrão de retorno com o Baerer na frente.
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String username = payload.get("username"), password = payload.get("password");
        String parsedData = DataValidatorService.validator(username, password);

        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        Usuario newUser = usuarioRepositorio.findByName(username);

        if (newUser != null) {
            if (passwordEncoder.matches(password, newUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos. Usuário já cadastrado.");
            }
        }

        String hashedPassword = webSecurityConfig.passwordEncoder().encode(password);
        Usuario novoUsuario = new Usuario(username, hashedPassword);
        novoUsuario.setRole("USER");
        usuarioRepositorio.save(novoUsuario);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!");
    }

    @PutMapping("/user/update")
    @ResponseBody
    public ResponseEntity<?> userUpdate(@RequestBody Map<String, String> payload) {
        String username = payload.get("username"), fullName = payload.get("fullname"), email = payload.get("mail");
        Integer userID = NumericConverter.safeParseInteger(payload.get("userID"));
        String parsedData = DataValidatorService.validator(username, fullName, email);
        String message = "";

        if (parsedData == null || parsedData.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        Usuario updatingUser = usuarioRepositorio.findByUserId(userID);
        Usuario existingUser = usuarioRepositorio.findByName(username);

        if (existingUser != null && !existingUser.getUserId().equals(updatingUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome ou Nickname já está em uso.");
        }
        if (updatingUser.isOauth2Binded() == true) {
            if (!updatingUser.getEmail().equals(email)) {
                message = "\n" + "Não se pode alterar E-Mail vinculado ao Google.";
            }
            updatingUser.setName(username);
            updatingUser.setNameCompleto(fullName);
        } else {
            if (!updatingUser.getEmail().equals(email)) {
                Usuario usedMail = usuarioRepositorio.findByEmail(email);
                if (usedMail != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Divergência de E-Mail.");
                }
                updatingUser.setEmail(email);
            }
            updatingUser.setName(username);
            updatingUser.setNameCompleto(fullName);
        }
        this.usuarioRepositorio.save(updatingUser);
        return ResponseEntity.ok().body("Dados cadastrais atualizados com sucesso!" + message);
    }
}