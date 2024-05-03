package kr.co.wasp.api.user.interfaces.request;

import lombok.Data;

@Data
public class SimpleUserRequest {
    private String userId;
    private String userName;
    private String email;
    private String ipv4;
    private String privateKey;
}
