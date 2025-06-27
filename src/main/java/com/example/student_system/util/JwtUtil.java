package com.example.student_system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.student_system.config.JwtConfig;
import com.example.student_system.domain.entity.account.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private JwtConfig config;

    @Autowired
    public JwtUtil(JwtConfig config) {
        this.config = config;
    }

    /**
     * 生成用户令牌
     * @param user 用户实体对象
     * @return 包含用户ID的JWT令牌字符串
     */
    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        // 如果未来想添加更多信息，只需加到这个map里即可
        // claims.put("role", user.getRole());
        return createToken(claims);
    }

    /**
     * 验证并解析JWT令牌
     * @param token 待验证的令牌字符串
     * @return 包含声明信息的Map集合
     */
    public Map<String, Claim> verifyToken(String token){
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(config.getSecretKey())).build().verify(token);
        return jwt.getClaims();
    }

    /**
     * 核心令牌创建方法
     * @param claims 包含用户信息的声明集合
     * @return 签名后的JWT字符串
     * 包含以下声明：
     * - 头部：指定算法HS256
     * - 生效时间（iat）
     * - 过期时间（exp）
     */
    public String createToken(Map<String, Object> claims){
        return JWT.create()
                .withPayload(claims)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + config.getExpiration()))
                .sign(Algorithm.HMAC256(config.getSecretKey()));
    }
}
