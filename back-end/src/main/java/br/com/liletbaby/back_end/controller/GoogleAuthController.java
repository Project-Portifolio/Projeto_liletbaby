package br.com.liletbaby.back_end.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * GoogleAuthController class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Controller //Para resposta do google como MVC
@RequestMapping("/auth")
public class GoogleAuthController {

    @GetMapping("/OAuth2/google")
    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}