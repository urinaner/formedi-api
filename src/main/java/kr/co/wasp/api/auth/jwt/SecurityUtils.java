package kr.co.wasp.api.auth.jwt;

import kr.co.wasp.api.user.domain.SimpleUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {
    public static Optional<SimpleUser> getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            SimpleUser simpleUser = ((JwtSimpleUserDetails) springSecurityUser).getSimpleUser();
            return Optional.of(simpleUser);
        }
        return Optional.empty();
    }
}
