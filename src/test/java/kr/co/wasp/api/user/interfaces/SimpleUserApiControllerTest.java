package kr.co.wasp.api.user.interfaces;

import kr.co.wasp.api.user.interfaces.request.LoginRequest;
import kr.co.wasp.api.user.interfaces.request.SimpleUserRequest;
import kr.co.wasp.api.user.interfaces.response.SimpleUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class SimpleUserApiControllerTest {
    @Autowired
    SimpleUserApiController simpleUserApiController;

    @Test
    void join_and_login_success() {
//        for (int i = 0; i < 10; i++) {
//            Random r = new Random();
//            String randomIpv4 = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
//            SimpleUserRequest simpleUserRequest = new SimpleUserRequest();
//            simpleUserRequest.setUserName(String.valueOf(i));
////            simpleUserRequest.setGuid(String.valueOf(i));
//            simpleUserRequest.setEmail("1234@naver.com");
//            simpleUserRequest.setIpv4(randomIpv4);
//            simpleUserRequest.setPrivateKey(String.valueOf(i));
//            SimpleUserResponse user = simpleUserApiController.join(simpleUserRequest);
//            log.info("{}", user);
//            assertNotNull(user);
//            LoginRequest loginRequest = new LoginRequest();
////            loginRequest.setToken(user.getToken());
//            loginRequest.setPrivateKey(String.valueOf(i));
//            SimpleUserResponse logined = simpleUserApiController.login(loginRequest);
//            assertNotNull(logined);
//        }
    }
}