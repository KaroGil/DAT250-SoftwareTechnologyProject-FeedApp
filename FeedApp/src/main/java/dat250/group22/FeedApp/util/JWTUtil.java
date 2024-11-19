package dat250.group22.FeedApp.util;
import dat250.group22.FeedApp.controller.PollController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private static final String SECRET = "nMzOZLk5h5Rf9TcGtJmsvjQTU4bpKgTH07Fpht9XvHE";
    private static byte[] key = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret".getBytes();

    private static final Key key2 = new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());    private static final long EXPIRATION_TIME = 86400000; //1 day in ms

    public static String generateToken(UUID userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        logger.info(token);
        return token;
    }

    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /*public static String getUser(String token) {
        return extractClaims(token).getSubject();
    }*/
    public static String getUser(String token) {
        // Remove the "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        // Parse the token and return the subject (userId)
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Use the same key as in generateToken
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // Extract the subject (userId)
    }

    public static String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    public static void testJWT(){
        UUID uuid = UUID.randomUUID();
        logger.info("Generated UUID: {}",uuid);
        String token = JWTUtil.generateToken(uuid, "USER");
        logger.info("Generated Token: {}", token); // Use placeholders to log token value
        String userId = JWTUtil.getUser(token);
        logger.info("Extracted UserId: {}", userId); // Use placeholders to log userId value
    }

}
