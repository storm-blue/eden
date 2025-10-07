package com.eden.lottery.constants;

/**
 * 居所常量定义
 * 统一管理所有居所的代码和名称
 */
public final class ResidenceConstants {

    // 私有构造函数，防止实例化
    private ResidenceConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== 居所代码常量 ====================

    /**
     * 城堡
     */
    public static final String CASTLE = "castle";

    /**
     * 公园
     */
    public static final String PARK = "park";

    /**
     * 市政厅
     */
    public static final String CITY_HALL = "city_hall";

    /**
     * 小白鸽家
     */
    public static final String WHITE_DOVE_HOUSE = "white_dove_house";

    /**
     * 行宫
     */
    public static final String PALACE = "palace";

    // ==================== 居所名称常量 ====================

    /**
     * 城堡显示名称
     */
    public static final String CASTLE_NAME = "城堡🏰";

    /**
     * 公园显示名称
     */
    public static final String PARK_NAME = "公园🌳";

    /**
     * 市政厅显示名称
     */
    public static final String CITY_HALL_NAME = "市政厅🏛️";

    /**
     * 小白鸽家显示名称
     */
    public static final String WHITE_DOVE_HOUSE_NAME = "小白鸽家🕊️";

    /**
     * 行宫显示名称
     */
    public static final String PALACE_NAME = "行宫🏯";

    // ==================== 居所数组常量 ====================

    /**
     * 所有居所代码数组
     */
    public static final String[] ALL_RESIDENCES = {CASTLE, PARK, CITY_HALL, WHITE_DOVE_HOUSE, PALACE};
}
