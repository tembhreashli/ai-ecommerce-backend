package com.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token Provider for generating and validating JWT tokens.
 * Handles token creation with claims and token validation with expiration checks.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret:your-secret-key-min-256-bits-long-for-hs256-algorithm}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    /**
     * Generate a JWT token for the given username with additional claims.
     *
     * @param username the username to include in the token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        return generateToken(username, new HashMap<>());
    }

    /**
     * Generate a JWT token for the given username with custom claims.
     *
     * @param username the username to include in the token
     * @param claims   additional claims to include in the token
     * @return the generated JWT token
     */
    public String generateToken(String username, Map<String, Object> claims) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", username, e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Extract the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String getUsernameFromToken(String token) {
        try {
            return getClaimsFromToken(token).getSubject();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired: {}", e.getMessage());
            return e.getClaims().getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            return null;
        }
    }

    /**
     * Extract all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims from the token
     */
    public Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
            throw new JwtException("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtException("JWT claims string is empty", e);
        }
    }

    /**
     * Validate the JWT token and check if it's valid and not expired.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error validating JWT token: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Check if the JWT token has expired.
     *
     * @param token the JWT token to check
     * @return true if the token has expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Get the expiration date of the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            return getClaimsFromToken(token).getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token", e);
            return null;
        }
    }

    /**
     * Get the issued-at date of the JWT token.
     *
     * @param token the JWT token
     * @return the issued-at date of the token
     */
    public Date getIssuedAtFromToken(String token) {
        try {
            return getClaimsFromToken(token).getIssuedAt();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getIssuedAt();
        } catch (Exception e) {
            logger.error("Error extracting issued-at date from token", e);
            return null;
        }
    }

    /**
     * Add a custom claim to the token.
     *
     * @param username the username for the token
     * @param claimKey the claim key
     * @param claimValue the claim value
     * @return the generated JWT token with the custom claim
     */
    public String generateTokenWithClaim(String username, String claimKey, Object claimValue) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(claimKey, claimValue);
        return generateToken(username, claims);
    }

    /**
     * Extract a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimKey the claim key to extract
     * @return the claim value, or null if not found
     */
    public Object getClaimFromToken(String token, String claimKey) {
        try {
            return getClaimsFromToken(token).get(claimKey);
        } catch (Exception e) {
            logger.error("Error extracting claim '{}' from token: {}", claimKey, e.getMessage());
            return null;
        }
    }
}
