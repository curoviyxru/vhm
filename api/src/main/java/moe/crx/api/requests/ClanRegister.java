package moe.crx.api.requests;

import lombok.Data;

@Data
public final class ClanRegister {
    private final String clanName;
    private final int maxMembers;
    private final int maxModerators;
}
