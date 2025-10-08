package com.eden.lottery.event.scene;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.ResidenceEvent;
import com.eden.lottery.event.ResidenceEventItem;

import java.util.List;

public final class PalaceScenes {

    // 秦小淮，李星斗
    public static List<ResidenceEventItem> TWO_XG__01 = List.of(
            new ResidenceEventItem("💕 行宫中 💕", "special"),
            new ResidenceEventItem("💕 “这辈子都不想上班了” 秦小淮躺在李星斗的胸口，想起从前的事 💕", "special"),
            new ResidenceEventItem("💕 “梦想成真了” 李星斗低下头，去找小淮的小嘴巴 💕", "special"),
            new ResidenceEventItem("💕 俩人的唇舌一下子缠在了一起 💕", "special")
    );

    // 秦小淮，李星斗
    public static List<ResidenceEventItem> TWO_XG__02 = List.of(
            new ResidenceEventItem("💕 睡梦中，秦小淮突然啜泣起来 💕", "special"),
            new ResidenceEventItem("💕 “宝宝怎么了” 她一动，李星斗就感觉到了 💕", "special"),
            new ResidenceEventItem("💕 “想家，想妈妈……” 秦小淮一抬头，准确无误叼住李星斗的乳头 💕", "special"),
            new ResidenceEventItem("💕 “嘶……” 李星斗又痒又麻，倒吸一口凉气 💕", "special"),
            new ResidenceEventItem("💕 秦小淮咂巴着嘴吮吸了好一阵子，一皱眉头，醒了！ 💕", "special"),
            new ResidenceEventItem("💕 “李星斗！你咋没奶！” 秦小淮握住小拳头，怒气冲冲地说 💕", "special"),
            new ResidenceEventItem("💕 李星斗傻了 💕", "special")
    );

    // 秦小淮，李星斗，存子
    public static List<ResidenceEventItem> THREE_XG__01 = List.of(
            new ResidenceEventItem("💕 行宫中，李星斗推开门 💕", "special"),
            new ResidenceEventItem("💕 秦小淮正趴在地上擦地，插着狐尾肛塞的屁股撅得高高的 💕", "special"),
            new ResidenceEventItem("💕 李星斗无声靠近，悄悄地抵在她雪白的屁股上正要插入，却发现触感不对 💕", "special"),
            new ResidenceEventItem("💕 \"李星斗，你就是想日存子！\" 身后传来秦小淮的怒喝： 💕", "special"),
            new ResidenceEventItem("💕 \"我今天只不过是和存子换装玩，你竟然！\" 💕", "special"),
            new ResidenceEventItem("💕 李星斗这才发现，面前撅着屁股的竟然是存子 💕", "special"),
            new ResidenceEventItem("💕 只见存子哆哆嗦嗦地呻吟一声，已经晕过去了…… 💕", "special")
    );

    // 通用场景
    public static List<ResidenceEventItem> XG__NORMAL = List.of(
            new ResidenceEventItem("微风轻拂过行宫🏯", "normal"),
            new ResidenceEventItem("行宫🏯 平静如常...", "normal")
    );

    public static final List<Scene> scenes = List.of(

            // 李星斗，秦小淮，存子
            new Scene(
                    List.of("李星斗", "秦小淮", "存子"),
                    ResidenceConstants.PALACE,
                    List.of(
                            new ResidenceEvent(THREE_XG__01)
                    )
            ),

            // 李星斗，秦小淮
            new Scene(
                    List.of("李星斗", "秦小淮"),
                    ResidenceConstants.PALACE,
                    List.of(
                            new ResidenceEvent(TWO_XG__01),
                            new ResidenceEvent(TWO_XG__02)
                    )
            ),

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