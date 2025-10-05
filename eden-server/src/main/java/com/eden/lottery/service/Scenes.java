package com.eden.lottery.service;

import com.eden.lottery.dto.ResidenceEventItem;

import java.util.ArrayList;
import java.util.List;

public final class Scenes {

    private Scenes() {
    }

    // 双人场景
    public static List<ResidenceEventItem> TWO__CB__01 = List.of(
            new ResidenceEventItem("💕 神宫的餐厅中，秦小淮被李星斗按在了餐桌上 💕", "special"),
            new ResidenceEventItem("💕 白嫩的腿儿被打开到最大，李星斗把脑袋埋进去，卖力地吃了起来 💕", "special"),
            new ResidenceEventItem("💕 秦小淮浑身软绵绵，小猫一样哼哼，心里想：星斗哥哥好爱我呀 💕", "special")
    );

    public static List<ResidenceEventItem> TWO__CB__02 = List.of(
            new ResidenceEventItem("💕 \"啊，小骚猫要被星斗爸爸日死了……\" 💕", "special"),
            new ResidenceEventItem("💕 神宫顶层的走廊里，秦小淮撅着白嫩的屁股，承受着身后的暴风骤雨 💕", "special"),
            new ResidenceEventItem("💕 她腿软的站不住了，刚想再次耍赖往地上瘫倒，屁股上就吃了一记巴掌，有点疼 💕", "special"),
            new ResidenceEventItem("💕 她开始装哭，结果越来越委屈，哭着哭着哭成真的了！ 💕", "special"),
            new ResidenceEventItem("💕 \"呜呜，你以前都很爱护我的，从来不打我的！\" 💕", "special")
    );

    public static List<ResidenceEventItem> TWO__CB__03 = List.of(
            new ResidenceEventItem("💕 最近几天，秦小淮特别淫荡 💕", "special"),
            new ResidenceEventItem("💕 \"求求主人，来插小骚猫的小穴穴好不好……\" 💕", "special"),
            new ResidenceEventItem("💕 见李星斗不理她，她就用屁股上插的猫尾巴扫他眼睛 💕", "special"),
            new ResidenceEventItem("💕 李星斗一下子就把她按倒了 💕", "special")
    );

    public static List<List<ResidenceEventItem>> twoCoupleScenes = List.of(
            TWO__CB__01,
            TWO__CB__02,
            TWO__CB__03
    );

}
