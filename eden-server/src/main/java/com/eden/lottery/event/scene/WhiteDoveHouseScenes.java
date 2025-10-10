package com.eden.lottery.event.scene;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.Event;
import com.eden.lottery.event.EventItem;

import java.util.List;

public final class WhiteDoveHouseScenes {
    // 通用场景
    public static List<EventItem> NORMAL = List.of(
            new EventItem("微风轻拂过小白鸽家🕊", "normal"),
            new EventItem("小白鸽家🕊 平静如常...", "normal")
    );

    // 小白鸽
    public static List<EventItem> XBG__01 = List.of(
            new EventItem("小白鸽放下书，才发现自己肚子咕咕叫了", "normal"),
            new EventItem("奶奶还没回来，下了楼，神宫送来的饭菜已经放在了桌上", "normal"),
            new EventItem("打开木盒，精致的四菜一汤，清香扑鼻，是存子的手艺", "normal"),
            new EventItem("一如既往的美味。小白鸽默默地吃着，突然轻叹一声", "normal"),
            new EventItem("抬起头来，窗外一只飞鸟悠然掠过", "normal"),
            new EventItem("那是神宫的方向", "normal")
    );

    public static final List<Scene> scenes = List.of(

            // 小白鸽
            new Scene(
                    List.of("小白鸽"),
                    ResidenceConstants.WHITE_DOVE_HOUSE,
                    List.of(
                            new Event(false, XBG__01)
                    )
            ),

            // 默认
            new Scene(
                    List.of(),
                    ResidenceConstants.WHITE_DOVE_HOUSE,
                    List.of(
                            new Event(false, NORMAL)
                    )
            )
    );
}
