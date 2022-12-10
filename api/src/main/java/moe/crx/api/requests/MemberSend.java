package moe.crx.api.requests;

import lombok.Data;

@Data
public final class MemberSend {
    private final String userName;
    private final String message;
}
