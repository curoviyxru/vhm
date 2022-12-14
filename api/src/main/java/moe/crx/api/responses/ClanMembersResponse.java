package moe.crx.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import moe.crx.api.Jsonable;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public final class ClanMembersResponse extends Jsonable<ClanMembersResponse> {
    private ArrayList<String> members;
}
