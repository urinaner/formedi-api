package kr.co.wasp.api.user.interfaces;

import kr.co.wasp.api.auth.jwt.SecurityUtils;
import kr.co.wasp.api.user.application.SimpleUserService;
import kr.co.wasp.api.user.domain.SimpleUser;
import kr.co.wasp.api.user.interfaces.request.LoginRequest;
import kr.co.wasp.api.user.interfaces.request.SimpleUserRequest;
import kr.co.wasp.api.user.interfaces.response.LoginSimpleUserResponse;
import kr.co.wasp.api.user.interfaces.response.SimpleUserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SimpleUserApiController {
    private final SimpleUserService simpleUserService;

    public SimpleUserApiController(SimpleUserService simpleUserService) {
        this.simpleUserService = simpleUserService;
    }

    @GetMapping
    public List<LoginSimpleUserResponse> getLoginUserList(@RequestParam("ipv4") String ipv4) {
        return simpleUserService.getLoginUserList(ipv4);
    }

    @GetMapping("/validate")
    public Boolean validate(@RequestParam("userId") String userId) {
        return simpleUserService.validate(userId);
    }

    @PostMapping("/join")
    public SimpleUserResponse join(@RequestBody SimpleUserRequest simpleUserRequest) {
        return simpleUserService.join(simpleUserRequest.getUserId(), simpleUserRequest.getUserName(), simpleUserRequest.getEmail(), simpleUserRequest.getIpv4(), simpleUserRequest.getPrivateKey());
    }

    @PostMapping("/login")
    public SimpleUserResponse login(@RequestBody LoginRequest loginRequest) {
            return simpleUserService.login(loginRequest.getUserId(), loginRequest.getPrivateKey(), loginRequest.getIpv4());
    }

    @PostMapping("/check")
    @PreAuthorize("hasRole('ROLE_GUEST')")
    public Boolean check() {
        Optional<SimpleUser> userName = SecurityUtils.getUserName();
        return true;
    }
}
