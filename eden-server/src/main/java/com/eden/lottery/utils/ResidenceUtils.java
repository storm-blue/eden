package com.eden.lottery.utils;

import com.eden.lottery.constants.ResidenceConstants;

/**
 * 居所工具类
 * 提供居所相关的统一工具方法
 */
public final class ResidenceUtils {

    // 私有构造函数，防止实例化
    private ResidenceUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 获取居所的显示名称
     *
     * @param residenceCode 居所代码
     * @return 居所显示名称，如果未找到则返回原代码
     */
    public static String getDisplayName(String residenceCode) {
        if (residenceCode == null) {
            return "未知居所";
        }

        return switch (residenceCode) {
            case ResidenceConstants.CASTLE -> ResidenceConstants.CASTLE_NAME;
            case ResidenceConstants.PARK -> ResidenceConstants.PARK_NAME;
            case ResidenceConstants.CITY_HALL -> ResidenceConstants.CITY_HALL_NAME;
            case ResidenceConstants.WHITE_DOVE_HOUSE -> ResidenceConstants.WHITE_DOVE_HOUSE_NAME;
            case ResidenceConstants.PALACE -> ResidenceConstants.PALACE_NAME;
            default -> residenceCode;
        };
    }

    /**
     * 验证居所代码是否有效
     *
     * @param residenceCode 居所代码
     * @return 是否有效
     */
    public static boolean isInvalidResidence(String residenceCode) {
        if (residenceCode == null) {
            return true;
        }

        return !switch (residenceCode) {
            case ResidenceConstants.CASTLE,
                 ResidenceConstants.PARK,
                 ResidenceConstants.CITY_HALL,
                 ResidenceConstants.WHITE_DOVE_HOUSE,
                 ResidenceConstants.PALACE -> true; // 兼容旧数据
            default -> false;
        };
    }

    /**
     * 获取所有有效的居所代码数组
     *
     * @return 所有居所代码数组
     */
    public static String[] getAllResidences() {
        return ResidenceConstants.ALL_RESIDENCES.clone();
    }

    /**
     * 将居所代码转换为简洁的显示名称（不带emoji）
     *
     * @param residenceCode 居所代码
     * @return 简洁的显示名称
     */
    public static String getSimpleName(String residenceCode) {
        if (residenceCode == null) {
            return "未知";
        }

        return switch (residenceCode) {
            case ResidenceConstants.CASTLE -> "城堡";
            case ResidenceConstants.PARK -> "公园";
            case ResidenceConstants.CITY_HALL -> "市政厅";
            case ResidenceConstants.WHITE_DOVE_HOUSE -> "小白鸽家";
            case ResidenceConstants.PALACE -> "行宫";
            default -> residenceCode;
        };
    }

    /**
     * 将居所代码转换为带图标的显示名称
     *
     * @param residenceCode 居所代码
     * @return 带图标的显示名称
     */
    public static String getIconName(String residenceCode) {
        if (residenceCode == null) {
            return "❓ 未知";
        }

        return switch (residenceCode) {
            case ResidenceConstants.CASTLE -> "🏰 城堡";
            case ResidenceConstants.PARK -> "🌳 公园";
            case ResidenceConstants.CITY_HALL -> "🏛️ 市政厅";
            case ResidenceConstants.WHITE_DOVE_HOUSE -> "🕊️ 小白鸽家";
            case ResidenceConstants.PALACE -> "🏯 行宫";
            default -> "❓ " + residenceCode;
        };
    }
}
