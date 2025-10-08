package com.eden.lottery.event.scene;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.ResidenceEvent;
import com.eden.lottery.event.ResidenceEventItem;

import java.util.List;

public final class PalaceScenes {
    // 通用场景
    public static List<ResidenceEventItem> XG__NORMAL = List.of(
            new ResidenceEventItem("微风轻拂过行宫🏯", "normal"),
            new ResidenceEventItem("行宫🏯 平静如常...", "normal")
    );

    public static final List<Scene> scenes = List.of(

            // 默认
            new Scene(
                    List.of(),
                    ResidenceConstants.PALACE,
                    List.of(
                            new ResidenceEvent(XG__NORMAL)
                    )
            )
    );
}