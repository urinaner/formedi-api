package kr.co.wasp.api.user.application;

import kr.co.wasp.api.auth.jwt.JwtSimpleUserService;
import kr.co.wasp.api.user.domain.SimpleUser;
import kr.co.wasp.api.user.domain.SimpleUserLoginLog;
import kr.co.wasp.api.user.domain.UserRole;
import kr.co.wasp.api.user.infrastructure.SimpleUserLoginLogRepository;
import kr.co.wasp.api.user.infrastructure.SimpleUserRepository;
import kr.co.wasp.api.user.interfaces.response.LoginSimpleUserResponse;
import kr.co.wasp.api.user.interfaces.response.SimpleUserResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleUserService {
    private final SimpleUserRepository simpleUserRepository;
    private final SimpleUserLoginLogRepository simpleUserLoginLogRepository;
    private final JwtSimpleUserService jwtSimpleUserService;
    private final PasswordEncoder passwordEncoder;

    public SimpleUserService(SimpleUserRepository simpleUserRepository, SimpleUserLoginLogRepository simpleUserLoginLogRepository, JwtSimpleUserService jwtSimpleUserService, PasswordEncoder passwordEncoder) {
        this.simpleUserRepository = simpleUserRepository;
        this.simpleUserLoginLogRepository = simpleUserLoginLogRepository;
        this.jwtSimpleUserService = jwtSimpleUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public SimpleUserResponse join(String userId, String userName, String email, String ipv4, String privateKey) {
        Optional<SimpleUser> optionalSimpleUser = simpleUserRepository.findByUserId(userId);
        if (optionalSimpleUser.isPresent()) {
            throw new BadCredentialsException("");
        }
        SimpleUser simpleUser = simpleUserRepository.save(SimpleUser.builder().userRole(UserRole.GUEST).userId(userId).userName(userName).email(email).privateKey(passwordEncoder.encode(privateKey)).build());
        this.logging(simpleUser.getUserId(), ipv4);
        return SimpleUserResponse.builder().token(jwtSimpleUserService.createAccessToken(simpleUser)).build();
    }

    public SimpleUserResponse login(String userId, String privateKey, String ipv4) {
//        Optional<String> optionalUserId = jwtSimpleUserService.parseToken("userId", userId);
//        if (optionalUserId.isEmpty()) {
//            throw new UsernameNotFoundException("");
//        }
        Optional<SimpleUser> optionalSimpleUser = simpleUserRepository.findByUserId(userId);
        if (optionalSimpleUser.isEmpty()) {
            throw new UsernameNotFoundException("");
        }
        SimpleUser simpleUser = optionalSimpleUser.get();
        if (!passwordEncoder.matches(privateKey, simpleUser.getPrivateKey())) {
            throw new BadCredentialsException("");
        }
        this.logging(simpleUser.getUserId(), ipv4);
        return SimpleUserResponse.builder().token(jwtSimpleUserService.createAccessToken(simpleUser)).build();
    }

    public List<LoginSimpleUserResponse> getLoginUserList(String ipv4) {
        return simpleUserLoginLogRepository.findByIpv4(ipv4).stream().sorted(Comparator.comparing(SimpleUserLoginLog::getUpdatedAt).reversed()).map(this::transform).collect(Collectors.toList());
    }

    private void logging(String userId, String ipv4) {
        Optional<SimpleUser> optionalSimpleUser = simpleUserRepository.findByUserId(userId);
        optionalSimpleUser.ifPresent(simpleUser -> {
            SimpleUserLoginLog simpleUserLoginLog = new SimpleUserLoginLog();
            simpleUserLoginLog.setUserId(simpleUser.getUserId());
            simpleUserLoginLog.setIpv4(ipv4);
            simpleUserLoginLogRepository.save(simpleUserLoginLog);
        });
    }

    private LoginSimpleUserResponse transform(SimpleUserLoginLog simpleUserLoginLog) {
        LoginSimpleUserResponse loginSimpleUserResponse = new LoginSimpleUserResponse();
        loginSimpleUserResponse.setUserId(simpleUserLoginLog.getUserId());
        return loginSimpleUserResponse;
    }

    public Boolean validate(String userId) {
        return !simpleUserRepository.findByUserId(userId).isPresent();
    }
}
