package dat250.group22.FeedApp.util;

import dat250.group22.FeedApp.controller.UserController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String SECRET_KEY = "Fs092ltGlTwyWyMtHqInF04xHt7zAh1qSmsW/nWeyFY="; // Generated with openssl

    public static UUID parseToken(String jwtToken) {
        logger.info("Token as bytes: {} in jwtService", Arrays.toString(jwtToken.getBytes()));

        try {
            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes()) // Use the secret key
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Extract the userId field (assume it's stored as a claim)
            String userIdStr = claims.get("userId", String.class);

            // Convert to UUID and return
            return UUID.fromString(userIdStr);

        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JWT token");
        }
    }
}
