package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("liletbaby/api")
public class LoginController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/login")
    @ResponseBody
    public List<Usuario> login(){
        return usuarioRepositorio.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload){
        String username = payload.get("username"), password = payload.get("password");
        Usuario novoUsuario = new Usuario(username, password);
        this.usuarioRepositorio.save(novoUsuario);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!");
    }
}
