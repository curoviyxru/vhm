package moe.crx.api.responses;

import lombok.Data;

@Data
public final class ClanJoinResponse {
    private final String message;
    private final int maxMembers;
}
