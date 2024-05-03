package kr.co.wasp.api.user.interfaces.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String privateKey;
    private String ipv4;
}
