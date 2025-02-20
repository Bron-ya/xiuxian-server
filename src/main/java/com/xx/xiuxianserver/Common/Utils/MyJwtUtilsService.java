package com.xx.xiuxianserver.Common.Utils;

import com.xx.xiuxianserver.Common.Exception.MyException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: JWT工具类
 * @Author jiangzk
 * @Date 2025/1/23 23:50
 */
@Service
public class MyJwtUtilsService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MyJwtUtilsService.class);

    private PrivateKey privateKey;

    private JwtParser jwtParser;

    public String createJwt(Object jwtPayload, long expiredAt) {
        //添加构成JWT的参数
        Map<String, Object> headMap = new HashMap();
        headMap.put("alg", SignatureAlgorithm.RS256.getValue());//使用RS256签名算法
        headMap.put("typ", "JWT");
        Map body = MyJsonUtils.parse(MyJsonUtils.stringify(jwtPayload), HashMap.class);
        return Jwts.builder()
                .setHeader(headMap)
                .setClaims(body)
                .setExpiration(new Date(expiredAt))
                .signWith(privateKey)
                .compact();
    }

    @Value("${login.jwt.private-key}")
    private String privateKeyBase64;
    //获取私钥，用于生成Jwt
    private PrivateKey getPrivateKey() {
        try {
            // 利用JDK自带的工具生成私钥
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyBase64));
            return kf.generatePrivate(ks);
        } catch (Exception e) {
            // 获取私钥失败
            logger.error(e.getMessage(), e);
            throw new MyException(HttpStatus.BAD_REQUEST.value(), "获取Jwt私钥失败");
        }
    }

    @Value("${login.jwt.public-Key}")
    private String publicKeyBase64;
    // 公钥，用于解析Jwt
    private JwtParser getJwtParser() {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyBase64));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pk = keyFactory.generatePublic(keySpec);
            return Jwts.parserBuilder().setSigningKey(pk).build();
        } catch (Exception e) {
            // 获取公钥失败
            throw new MyException(HttpStatus.BAD_REQUEST.value(), "获取Jwt私钥失败");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        privateKey = getPrivateKey();
        jwtParser = getJwtParser();
    }

    public <T> T verifyJwt(String jwt, Class<T> jwtPayloadClass) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }
        Jws<Claims> jws = this.jwtParser.parseClaimsJws(jwt); // 会校验签名，校验过期时间
        Claims jwtPayload = jws.getBody();
        if (jwtPayload == null) {
            return null;
        }
        return MyJsonUtils.convert(jwtPayload, jwtPayloadClass);
    }

    public static <T> T getPayload(String jwt, Class<T> jwtPayloadClass) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }

        try {
            // jwt字符串由3部分组成，用英文的点分割：herder.payload.sign
            // 可以直接取中间一段，进行Base64解码
            byte[] decodedBytes = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            return MyJsonUtils.parse(new String(decodedBytes), jwtPayloadClass);
        } catch (Exception e) {
            return null;
        }
    }
}
