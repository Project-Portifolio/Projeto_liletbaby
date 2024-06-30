package br.com.liletbaby.back_end.security.providers;

import br.com.liletbaby.back_end.models.Usuario;
import br.com.liletbaby.back_end.repository.UsuarioRepositorio;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TokenService class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Service
public class TokenCustomProvider {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private String TOKEN_ISSUER = "auth0-api-liletbaby";

    private static final Random RANDOM = new SecureRandom();
    private static final int SECRET_LENGTH = 256;
    private Map<Integer, String> userSecrets = new HashMap<>();
    private HashMap<Integer, String> tempUserSecrets = new HashMap<>();

    public String generateSecret(Usuario usuario){
        byte[] tokenSecret = new byte[SECRET_LENGTH];
        RANDOM.nextBytes(tokenSecret);
        String secret = Base64.getEncoder().encodeToString(tokenSecret);
        userSecrets.put(usuario.getUserId(), secret);
        return secret;
    }

    public String generateToken(Usuario usuario) {
        String userId = String.valueOf(usuario.getUserId());
        String existingToken = getExistingToken(userId);

        if (existingToken != null) {
            return existingToken;
        }

        String secret = generateSecret(usuario);
        tempUserSecrets.put(usuario.getUserId(), secret);
        Algorithm algorithm = Algorithm.HMAC512(secret);
        String token = JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withSubject(userId)
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);

        validateToken(token);
        storeToken(userId, token);
        return "Bearer " + token;
    }

    private String getExistingToken(String userId) {
        return userSecrets.get(Integer.valueOf(userId));
    }

    private void storeToken(String userId, String token) {
        userSecrets.put(Integer.valueOf(userId), token);
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String userId = jwt.getSubject();
        String issuer = jwt.getIssuer();

        Usuario user = usuarioRepositorio.findByUserId(Integer.valueOf(userId));

        if (user.isTokenValid() != false && issuer.equals(TOKEN_ISSUER)) {
            return userId;
        } else {

            if (token == null || token.isBlank()) {
                return "Token JWT não está presente para validação.";
            }

            String expires = String.valueOf(jwt.getExpiresAt());
            String secret = tempUserSecrets.get(Integer.parseInt(userId));

            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(userId)
                    .build();

            verifier.verify(token);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            LocalDateTime dateTime = LocalDateTime.parse(expires, formatter);
            Instant parsedInstant = dateTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant();

            if (Instant.now().isAfter(parsedInstant)) {
                user.setTokenValid(false);
                return "Token JWT expirado.";
            } else {
                user.setTokenValid(true);
                usuarioRepositorio.save(user);
                tempUserSecrets.remove(Integer.valueOf(userId));
                return userId;
            }
        }
    }
}