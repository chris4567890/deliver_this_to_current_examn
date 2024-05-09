package org.util;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TokenUtil {

    /**
     * Creates a token with claims provided in a Collection of map entries
     * @param issuer app name or company name.
     * @param token_expire_time how long does it take for the token to expire?
     * @param secret_key secret key if you don't have one you can generate one with secretKey.generateSecretKey(32)
     * @param subject Subject of the token should return a String
     * @param claims Claims of the token
     * @return Token String
     * @throws JOSEException
     */
    public static String createToken(String issuer, String token_expire_time, String secret_key, String subject, Map<String, String> claims) throws JOSEException {
        return createToken(issuer, () -> new Date(new Date().getTime() + Integer.parseInt(token_expire_time)), secret_key, subject, claims);
    }
    /**
     * Creates a token with claims provided in a Collection of map entries
     * @param issuer app name or company name.
     * @param token_expire_time how long does it take for the token to expire?
     * @param secret_key secret key if you don't have one you can generate one with secretKey.generateSecretKey(32)
     * @param subject Subject of the token
     * @param claims Claims of the token
     * @return Token String
     * @throws JOSEException
     */
    public static String createToken(String issuer, int token_expire_time, String secret_key, String subject, Map<String, String> claims) throws JOSEException {
        return createToken(issuer, () -> new Date(new Date().getTime() + token_expire_time), secret_key, subject, claims);
    }
    /**
     * Creates a token with claims provided in a Collection of map entries
     * @param issuer app name or company name.
     * @param token_expire_time Supllier to create an expiration date
     * @param secret_key secret key if you don't have one you can generate one with secretKey.generateSecretKey(32)
     * @param subject Subject of the token should return a String
     * @param claims Claims of the token
     * @return Token String
     * @throws JOSEException
     */
    public static String createToken(String issuer, Supplier<Date> token_expire_time, String secret_key, String subject, Map<String, String> claims) throws JOSEException {
        // Build commons
        JWTClaimsSet.Builder builderJWT = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .expirationTime(token_expire_time.get());

        // Attach claims
        claims.entrySet().forEach(x -> builderJWT.claim(x.getKey(), x.getValue()));

        // Finalise set
        JWTClaimsSet claimsSet = builderJWT.build();

        // Make it a payload
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSSigner signer = new MACSigner(secret_key);
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWSObject jwsobject = new JWSObject(jwsHeader, payload);
        jwsobject.sign(signer);
        return jwsobject.serialize();
    }

    public static boolean tokenIsValid(String token, String secret) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        if (jwt.verify(new MACVerifier(secret))) {
            return tokenNotExpired(token);
        } else {
            return false;
        }
    }

    public static boolean tokenNotExpired(String token) throws ParseException {
        return timeToExpire(token) > 0;
    }

    public static int timeToExpire(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime());
    }

    public static <T> T extractFromToken(String token, Function<JWTClaimsSet, T> extractor) {
        try {
            JWTClaimsSet claims = SignedJWT.parse(token).getJWTClaimsSet();
            return extractor.apply(claims);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

