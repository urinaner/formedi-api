package kr.co.wasp.api.user.infrastructure;

import kr.co.wasp.api.user.domain.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimpleUserRepository extends JpaRepository<SimpleUser, Long> {
//    List<SimpleUser> findByIpv4(String ipv4);

    Optional<SimpleUser> findByUserId(String userId);
}
