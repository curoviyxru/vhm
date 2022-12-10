package moe.crx.api.requests;

import lombok.Data;

@Data
public final class ClanJoin {

    public static final String MODERATOR = "moderator";
    public static final String MEMBER = "member";

    private final String userType;
    private final String userName;
}
