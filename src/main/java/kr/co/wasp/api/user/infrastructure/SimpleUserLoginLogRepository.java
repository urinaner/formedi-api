package kr.co.wasp.api.user.infrastructure;

import kr.co.wasp.api.user.domain.SimpleUserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleUserLoginLogRepository extends JpaRepository<SimpleUserLoginLog, Long> {
    List<SimpleUserLoginLog> findByIpv4(String ipv4);
}
