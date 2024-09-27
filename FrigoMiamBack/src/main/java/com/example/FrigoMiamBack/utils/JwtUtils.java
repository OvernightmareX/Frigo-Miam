package com.example.FrigoMiamBack.utils;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.exceptions.NullParameterException;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtUtils {

    private static final String ISSUER = "FrigoMiam";
    private static final SecretKey SECRET = Keys.hmacShaKeyFor(Decoders.BASE64.decode("jeSuisUnMotDePasseSuperArchiMegaCompliqueeDePlusDe256bits!"));
    private static final long EXPIRATION_TIME = 1;

    private JwtUtils() {
        // Private constructor to prevent instantiation
    }

    public static String generateToken(Account account, Role role){

        if (account == null) {
            throw new NullParameterException(ExceptionsMessages.ACCOUNT_NULL_CANNOT_CREATE_TOKEN, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (role == null) {
            throw new NullParameterException(ExceptionsMessages.ROLE_NULL_CANNOT_CREATE_TOKEN, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(account.getEmail())
                .claim("email", account.getEmail())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(LocalDateTime.now().plusDays(EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static boolean validateToken(String token, Account account) {

        if (account == null) {
            throw new NullParameterException(ExceptionsMessages.ACCOUNT_NULL_CANNOT_VALIDATE_TOKEN, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (token == null) {
            throw new NullParameterException(ExceptionsMessages.TOKEN_NULL_CANNOT_VALIDATE_TOKEN, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        String email = extractEmail(token);
        String issuer = extractIssuer(token);
        return (issuer.equals(ISSUER) && email.equals(account.getEmail()) && !isTokenExpired(token));
    }

    public static String extractIssuer(String token) {
        return extractAllClaims(token).getIssuer();
    }

    public static String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public static String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private static Claims extractAllClaims(String token) {
        if (token == null) {
            throw new NullParameterException("exception.token.parameter.null", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        return Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload();
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}