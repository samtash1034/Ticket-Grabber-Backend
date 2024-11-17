package com.project.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.common.enums.CommonCode;
import com.project.common.enums.InvalidMsg;
import com.project.common.exception.AbnormalDataException;
import com.project.common.exception.BaseException;
import com.project.orm.mapper.UserMapper;
import com.project.orm.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class TokenUtil {

    public static final String TOKEN = "token";
    public static final String ACCOUNT = "account";
    public static final String USERID = "userId";
    public static final String LOGIN_TIME = "loginTime";

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expire}")
    private Integer expire;

    @Autowired
    private RSAUtil rsaUtil;
    @Autowired
    private UserMapper userMapper;

    public String createToken(String account, String userId)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        Algorithm algorithm = getRSAPrivateKeyAlgorithm();

        Date expiresAt = Date.from(Instant.now().plus(expire, ChronoUnit.HOURS));

        return JWT.create()
                .withSubject(userId)
                .withIssuer(issuer)
                .withClaim(ACCOUNT, account)
                .withClaim(LOGIN_TIME, System.currentTimeMillis())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public Map<String, Object> decodedBearerToken(String bearerToken) {
        verifyBearerToken(bearerToken);
        try {
            String token = bearerToken.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) rsaUtil.getPublicKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodeJWT = verifier.verify(token);

            return setDecodedMap(decodeJWT, token);
        } catch (TokenExpiredException e) {
            throw new BaseException(CommonCode.N40102);
        } catch (Exception e) {
            throw new BaseException(CommonCode.N40101);
        }
    }

    private Algorithm getRSAPrivateKeyAlgorithm() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Algorithm.RSA256(
                (RSAPublicKey) rsaUtil.getPublicKey(),
                (RSAPrivateKey) rsaUtil.getPrivateKey()
        );
    }

    public void verifyBearerToken(String bearerToken) throws BaseException {
        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            throw new BaseException(CommonCode.N40101);
        }
    }

    private Map<String, Object> setDecodedMap(DecodedJWT decodeJWT, String token) throws BaseException {
        Map<String, Object> decodedMap = new HashMap<>();
        validateDecodeJWT(decodeJWT);

        decodedMap.put(ACCOUNT, decodeJWT.getClaim(ACCOUNT).asString());
        decodedMap.put(TOKEN, token);
        decodedMap.put(USERID, decodeJWT.getSubject());

        return decodedMap;
    }

    private void validateDecodeJWT(DecodedJWT decodeJWT) throws BaseException {
        String account = decodeJWT.getClaim(ACCOUNT).asString();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(decodeJWT.getSubject())) {
            throw new AbnormalDataException(InvalidMsg.ERROR6);
        }

        UserModel userModel = userMapper.selectByEmail(account);
        if (userModel == null) {
            throw new AbnormalDataException(InvalidMsg.ERROR6);
        }
    }
}
