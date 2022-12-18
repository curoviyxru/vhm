package moe.crx.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import moe.crx.api.Jsonable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public final class ClanJoinResponse extends Jsonable<ClanJoinResponse> {
    private String message;
    private int maxMembers;
}
