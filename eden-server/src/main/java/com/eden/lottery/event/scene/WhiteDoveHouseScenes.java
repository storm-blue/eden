package com.eden.lottery.event.scene;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.Event;
import com.eden.lottery.event.EventItem;

import java.util.List;

public final class WhiteDoveHouseScenes {
    // 通用场景
    public static List<EventItem> XBG__NORMAL = List.of(
            new EventItem("微风轻拂过小白鸽家🕊", "normal"),
            new EventItem("小白鸽家🕊 平静如常...", "normal")
    );

    public static final List<Scene> scenes = List.of(

            // 默认
            new Scene(
                    List.of(),
                    ResidenceConstants.WHITE_DOVE_HOUSE,
                    List.of(
                            new Event(false, XBG__NORMAL)
                    )
            )
    );
}
