package com.eden.lottery.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 用户状态更新服务
 */
@Service
public class UserStatusService {

    private static final Logger logger = LoggerFactory.getLogger(UserStatusService.class);

    private final Random random = new Random();

    // 为每个用户定义状态列表
    private static final Map<String, String[]> USER_STATUS_MAP = new HashMap<>();

    static {
        // 李星斗的状态列表
        USER_STATUS_MAP.put("李星斗", new String[]{
                "兽性大发",
                "装酷中",
                "沉吟中",
        });

        // 秦小淮的状态列表
        USER_STATUS_MAP.put("秦小淮", new String[]{
                "发情中",
                "幻想中",
                "偷懒中",
        });

        // 存子的状态列表
        USER_STATUS_MAP.put("存子", new String[]{
                "发情中",
                "高潮中",
                "流水中",
                "自慰中",
        });

        // 小白鸽的状态列表
        USER_STATUS_MAP.put("小白鸽", new String[]{
                "思考中",
                "学习中",
                "休息中",
        });

        // 邓炎博升的状态列表
        USER_STATUS_MAP.put("严伯升", new String[]{
                "工作中",
                "思考中",
        });

        // 默认状态列表（用于未特别指定的用户）
        USER_STATUS_MAP.put("default", new String[]{
                "忙碌中",
                "休息中",
                "散步中",
        });
    }

    /**
     * 根据用户名和当前居所决定用户状态
     * 从该用户的状态列表中随机选择一个
     *
     * @param userId    用户名
     * @param residence 当前居所
     * @return 更新后的用户状态
     */
    public String determineUserStatus(String userId, String residence) {
        logger.debug("决定用户状态 - 用户: {}, 居所: {}", userId, residence);

        // 获取该用户的状态列表
        String[] statusList = USER_STATUS_MAP.getOrDefault(userId, USER_STATUS_MAP.get("default"));

        // 从列表中随机选择一个状态
        String newStatus = statusList[random.nextInt(statusList.length)];

        logger.debug("为用户 {} 选择状态: {}", userId, newStatus);

        return newStatus;
    }
}

