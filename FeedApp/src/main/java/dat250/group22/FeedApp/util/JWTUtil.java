package dat250.group22.FeedApp.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JWTUtil {

    private static final String SECRET_KEY = "Fs092ltGlTwyWyMtHqInF04xHt7zAh1qSmsW/nWeyFY=";

    public static String generateToken(UUID userId) {
        return Jwts.builder()
                .claim("userId", userId.toString()) // Add userId as a claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1-day expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Sign with secret key
                .compact();
    }
}
