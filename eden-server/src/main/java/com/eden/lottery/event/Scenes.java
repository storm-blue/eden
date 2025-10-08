package com.eden.lottery.event;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.scene.*;

import java.util.List;
import java.util.Map;

public final class Scenes {

    private Scenes() {
    }

    public static final Map<String, List<Scene>> scenes = Map.of(
            ResidenceConstants.CASTLE, CastleScenes.scenes,
            ResidenceConstants.PARK, ParkScenes.scenes,
            ResidenceConstants.CITY_HALL, CityHallScenes.scenes,
            ResidenceConstants.WHITE_DOVE_HOUSE, WhiteDoveHouseScenes.scenes,
            ResidenceConstants.PALACE, ParkScenes.scenes
    );

    public static List<ResidenceEventItem> getEvent(String residence, List<String> users) {

        // TODO
        //  1. 根据居所code 获取对应的场景列表
        //  2. 根据用户列表按照顺序匹配符合用户名要求的场景
        //  3. 随机选取场景中的事件
        //  4. 返回事件的items
        return null;
    }
}
