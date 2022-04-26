package link.tanxin.common.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class JwtUtilTest {

    @Test
    void createToken() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("name", "tanxin");
        map.put("id", "7");
        String token = JwtUtil.createToken(map);
        System.out.println(token);
    }

    @Test
    void getClaims() {
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidGFueGluIiwiaWQiOiI3IiwiZXhwIjoxNjQ5MzkzMDA2fQ.cVZpbW-EvyvdP8IzJ2ZkP1BM0ss-bJwGi3y8W9mZ5HY";
        Map<String,Object> result = JwtUtil.getClaims(token);
        assert result != null;
        System.out.println(result.toString());
    }
    @Test
    void verifyToken(){

    }
}