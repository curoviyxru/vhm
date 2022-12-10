package moe.crx.api.responses;

import lombok.Data;

import java.util.ArrayList;

@Data
public final class ActiveClansResponse {
    private final ArrayList<String> clans;
}
