package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 命令实体 - 秦小淮颁布的命令
 */
public class Decree {
    
    /**
     * 命令ID
     */
    private Long id;
    
    /**
     * 命令代码：NO_CASTLE_ACCESS（不得靠近城堡）
     */
    private String code;
    
    /**
     * 命令名称
     */
    private String name;
    
    /**
     * 命令描述
     */
    private String description;
    
    /**
     * 是否已颁布
     */
    private Boolean active;
    
    /**
     * 颁布时间
     */
    private LocalDateTime issuedAt;
    
    /**
     * 取消时间
     */
    private LocalDateTime cancelledAt;
    
    /**
     * 颁布者（默认秦小淮）
     */
    private String issuedBy;
    
    public Decree() {}
    
    public Decree(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.active = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    
    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
    
    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
    
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
    
    public String getIssuedBy() {
        return issuedBy;
    }
    
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
    
    @Override
    public String toString() {
        return "Decree{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", issuedAt=" + issuedAt +
                ", cancelledAt=" + cancelledAt +
                ", issuedBy='" + issuedBy + '\'' +
                '}';
    }
}

