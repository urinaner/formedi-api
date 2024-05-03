package kr.co.wasp.api.auth.jwt;

import com.google.common.collect.Lists;
import kr.co.wasp.api.user.domain.SimpleUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JwtSimpleUserDetails implements UserDetails {
    private final SimpleUser simpleUser;

    public JwtSimpleUserDetails(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Lists.newArrayList("ROLE_" + simpleUser.getUserRole().name());
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return simpleUser.getPrivateKey();
    }

    @Override
    public String getUsername() {
        return simpleUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
