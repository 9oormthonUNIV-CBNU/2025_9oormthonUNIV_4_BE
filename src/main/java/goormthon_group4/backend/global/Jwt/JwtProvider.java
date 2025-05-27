package goormthon_group4.backend.global.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;
    private final int expiration;
    private final SecretKey SECRET_KEY;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email).build();

        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration * 60 * 1000L))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }
}