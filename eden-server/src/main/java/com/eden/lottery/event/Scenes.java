package com.eden.lottery.event;

import com.eden.lottery.constants.ResidenceConstants;
import com.eden.lottery.event.scene.*;

import java.util.HashSet;
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

        // 1. 根据居所code 获取对应的场景列表
        List<Scene> residenceScenes = scenes.get(residence);
        if (residenceScenes == null || residenceScenes.isEmpty()) {
            // 如果没有找到对应居所的场景，返回默认事件
            return List.of(new ResidenceEventItem("暂无事件", "normal"));
        }

        // 2. 根据用户列表按照顺序匹配符合用户名要求的场景
        Scene matchedScene = null;
        for (Scene scene : residenceScenes) {
            List<String> sceneUsers = scene.getUsers();

            // 检查场景要求的用户是否都在当前用户列表中
            // 并且用户列表的数量要匹配（确保精确匹配）
            if (users.size() == sceneUsers.size() && new HashSet<>(users).containsAll(sceneUsers)) {
                matchedScene = scene;
                break; // 找到精确匹配的场景，立即使用
            }

            // 默认场景
            if (sceneUsers.isEmpty()) {
                matchedScene = scene;
                break;
            }
        }

        // 如果还是没有找到场景，返回默认事件
        if (matchedScene == null) {
            return List.of(new ResidenceEventItem("暂无事件", "normal"));
        }

        // 3. 随机选取场景中的事件
        List<ResidenceEvent> events = matchedScene.getEvents();
        if (events == null || events.isEmpty()) {
            return List.of(new ResidenceEventItem("暂无事件", "normal"));
        }

        // 随机选择一个事件
        int randomIndex = (int) (Math.random() * events.size());
        ResidenceEvent selectedEvent = events.get(randomIndex);

        // 4. 返回事件的items
        return selectedEvent.getItems();
    }
}
