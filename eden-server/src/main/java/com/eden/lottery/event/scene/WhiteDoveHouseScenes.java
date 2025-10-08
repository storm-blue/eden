package com.eden.lottery.event.scene;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.ResidenceEvent;
import com.eden.lottery.event.ResidenceEventItem;

import java.util.List;

public final class WhiteDoveHouseScenes {
    // 通用场景
    public static List<ResidenceEventItem> XBG__NORMAL = List.of(
            new ResidenceEventItem("微风轻拂过小白鸽家🕊", "normal"),
            new ResidenceEventItem("小白鸽家🕊 平静如常...", "normal")
    );

    public static final List<Scene> scenes = List.of(

            // 默认
            new Scene(
                    List.of(),
                    ResidenceConstants.WHITE_DOVE_HOUSE,
                    List.of(
                            new ResidenceEvent(XBG__NORMAL)
                    )
            )
    );
}
