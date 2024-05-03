package kr.co.wasp.api.auth.jwt;

import kr.co.wasp.api.user.domain.SimpleUser;
import kr.co.wasp.api.user.infrastructure.SimpleUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtSimpleUserDetailsService implements UserDetailsService {
    private final SimpleUserRepository simpleUserRepository;

    public JwtSimpleUserDetailsService(SimpleUserRepository simpleUserRepository) {
        this.simpleUserRepository = simpleUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        SimpleUser simpleUser = simpleUserRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException(""));

        return new JwtSimpleUserDetails(simpleUser);
    }
}
