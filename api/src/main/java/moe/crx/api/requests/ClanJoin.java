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
public final class ClanJoin extends Jsonable<ClanJoin> {

    public static final String MODERATOR = "moderator";
    public static final String MEMBER = "member";

    private String userType;
    private String userName;
}
