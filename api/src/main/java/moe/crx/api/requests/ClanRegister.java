package moe.crx.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import moe.crx.api.Jsonable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public final class ClanRegister extends Jsonable<ClanRegister> {
    private String clanName;
    private int maxMembers;
    private int maxModerators;
}
