package com.automotive.mechanic.util;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.automotive.mechanic.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:I9OGIPNP5roQ+b1vdtwVDX9R59gjf9SF+teLOWXzduw=}")
    private String secret;

    /**
     * Get signing key from secret
     * 
     * @return Key for signing JWT tokens
     */
    private Key getSigningKey() {
        byte[] keyBytes;
        try {
            // Try to decode as Base64 first
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (Exception e) {
            // If Base64 decoding fails, use the secret directly as bytes
            // Note: For production, always use Base64-encoded secrets
            keyBytes = secret.getBytes();
        }
        // Ensure minimum key length for HS256 (256 bits = 32 bytes)
        if (keyBytes.length < 32) {
            // Pad or extend the key to meet minimum requirement
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            keyBytes = paddedKey;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract username (email) from token
     * 
     * @param token JWT token
     * @return User email
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     * 
     * @param token JWT token
     * @return Expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extract user ID from token
     * 
     * @param token JWT token
     * @return User ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extract role from token
     * 
     * @param token JWT token
     * @return User role
     */
    public Role getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String roleString = claims.get("role", String.class);
        return Role.valueOf(roleString);
    }

    /**
     * Extract a specific claim from token
     * 
     * @param token JWT token
     * @param claimsResolver Function to extract the claim
     * @return The extracted claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     * 
     * @param token JWT token
     * @return All claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if token is expired
     * 
     * @param token JWT token
     * @return true if token is expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Validate JWT token without username check (for general validation)
     * 
     * @param token JWT token
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}


