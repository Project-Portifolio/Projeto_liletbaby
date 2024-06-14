package br.com.liletbaby.back_end.services;

import br.com.liletbaby.back_end.models.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * TokenService class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Service
public class TokenService {

    private static final Random RANDOM = new SecureRandom();
    private static final int SECRET_LENGTH = 256;
    private Map<Integer, String> userSecrets = new HashMap<>();

    public String generateSecret(Usuario usuario){
        byte[] tokenSecret = new byte[SECRET_LENGTH];
        RANDOM.nextBytes(tokenSecret);
        String secret = Base64.getEncoder().encodeToString(tokenSecret);
        userSecrets.put(usuario.getUserId(), secret); // KEY and VALUE
        return secret;
    }

    public String generateToken(Usuario usuario) { //Sempre que o usuário logar de novo um novo token é gerado.
        try{
            String secret = userSecrets.get(usuario.getUserId());
            if(secret == null || secret.isBlank()){
                secret = generateSecret(usuario);
            }
            Algorithm algorithm = Algorithm.HMAC512(secret);
            String token = JWT.create()
                    .withIssuer("auth0-api-liletbaby")
                    .withSubject(String.valueOf(usuario.getUserId()))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            return null;
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String userId = jwt.getSubject();
        String secret = userSecrets.get(Integer.parseInt(userId));

        if (secret == null || secret.isBlank()) {
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0-api-liletbaby")
                    .build();
            verifier.verify(token);
            return userId;
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}