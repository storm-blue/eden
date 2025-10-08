package com.eden.lottery.event;

/**
 * 居所事件项DTO - 用于类型安全的JSON序列化和反序列化
 */
public class EventItem {
    
    private String description;  // 事件描述
    private String type;         // 事件类型：special, normal
    private String[] colors;     // 颜色数组（向后兼容，可选）
    
    // 默认构造函数
    public EventItem() {}
    
    // 主要构造函数
    public EventItem(String description, String type) {
        this.description = description;
        this.type = type;
        // 根据类型设置默认颜色
        if ("special".equals(type)) {
            this.colors = new String[]{"#ff69b4", "#ff1744"};
        } else {
            this.colors = new String[]{"#888888", "#aaaaaa"};
        }
    }
    
    // 完整构造函数（向后兼容）
    public EventItem(String description, String type, String[] colors) {
        this.description = description;
        this.type = type;
        this.colors = colors;
    }
    
    // Getter 和 Setter 方法
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String[] getColors() {
        return colors;
    }
    
    public void setColors(String[] colors) {
        this.colors = colors;
    }
    
    @Override
    public String toString() {
        return "ResidenceEventItem{" +
                "description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", colors=" + (colors != null ? String.join(",", colors) : "null") +
                '}';
    }
}
