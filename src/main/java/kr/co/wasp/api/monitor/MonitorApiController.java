package kr.co.wasp.api.monitor;

import kr.co.wasp.api.auth.jwt.SecurityUtils;
import kr.co.wasp.api.user.domain.SimpleUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wasp/monitor")
public class MonitorApiController {
    @GetMapping("/health")
    public Boolean health() {
        return true;
    }
}
