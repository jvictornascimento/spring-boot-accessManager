package com.jvictornascimento.accessmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jvictornascimento.accessmanager.security.user.AccessManagerUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtUtils {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateAccessTokenForUser(Authentication authentication) {
        var userPrincipal  = (AccessManagerUser) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("access-manager-api")
                .withSubject(userPrincipal.getEmail())
                .withClaim("id", userPrincipal.getId())
                .withIssuedAt(Instant.now())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);

    }
    public boolean verifyToken (String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("access-manager-api")
                    .build()
                    .verify(token)
                    .getSubject();
            return true;
        }catch (JWTVerificationException e){
            throw new  RuntimeException("Error while generating token",e);
        }
    }
    public String getUsernameFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("access-manager-api")
                .build()
                .verify(token)
                .getSubject();
    }
    public Instant getExpiredAtFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("access-manager-api")
                .build()
                .verify(token)
                .getExpiresAtAsInstant();
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusMinutes(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
