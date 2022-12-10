package moe.crx.api.responses;

import lombok.Data;

import java.util.ArrayList;

@Data
public final class ClanMembersResponse {
    private final ArrayList<String> members;
}
