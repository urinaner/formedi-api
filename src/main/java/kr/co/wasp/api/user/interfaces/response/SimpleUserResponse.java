package kr.co.wasp.api.user.interfaces.response;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString

public class SimpleUserResponse {
    private String token;

    public String getToken() {
        return token;
    }
}
