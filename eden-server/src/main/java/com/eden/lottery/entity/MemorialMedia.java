package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 纪念堂媒体文件实体
 */
public class MemorialMedia {
    private Long id;
    private String fileName;
    private String originalName;
    private String filePath;
    private String fileType; // image 或 video
    private String mimeType;
    private Long fileSize;
    private String url;
    private LocalDateTime uploadTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public MemorialMedia() {}

    public MemorialMedia(String fileName, String originalName, String filePath, 
                        String fileType, String mimeType, Long fileSize, String url) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.url = url;
        this.uploadTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MemorialMedia{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", originalName='" + originalName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fileSize=" + fileSize +
                ", url='" + url + '\'' +
                ", uploadTime=" + uploadTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
