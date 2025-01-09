package org.skillfactory.sf_bank_system.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-validity}")
    private long accessValidity;
    @Value("${jwt.refresh-validity}")
    private long refreshValidity;

    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessValidity))
                .withClaim("id", user.getId())
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshValidity))
                .sign(algorithm);
    }

    public UsernamePasswordAuthenticationToken getAuthTokenFromJwt(String jwtToken) {
        DecodedJWT decodedJWT = decodeJWT(jwtToken);
        String username = decodedJWT.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }

    public String getUsernameFromJwt(String jwtToken) {
        DecodedJWT decodedJWT = decodeJWT(jwtToken);
        return decodedJWT.getSubject();
    }

    public Long getIdFromAuthorizationHeader(String authorizationHeader) {
        String accessToken = extractAccessToken(authorizationHeader);
        DecodedJWT decodedJWT = decodeJWT(accessToken);
        return decodedJWT.getClaim("id").asLong();
    }

    private DecodedJWT decodeJWT(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwtToken);
    }

    private String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
