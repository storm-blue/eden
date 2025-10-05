package com.eden.lottery.service;

import com.eden.lottery.entity.ResidenceHistory;
import com.eden.lottery.mapper.ResidenceHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 居住历史记录服务
 */
@Service
public class ResidenceHistoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(ResidenceHistoryService.class);
    
    @Autowired
    private ResidenceHistoryMapper residenceHistoryMapper;
    
    // 居住地点名称映射
    private static final Map<String, String> RESIDENCE_NAMES = new HashMap<>();
    static {
        RESIDENCE_NAMES.put("castle", "🏰 城堡");
        RESIDENCE_NAMES.put("city_hall", "🏛️ 市政厅");
        RESIDENCE_NAMES.put("palace", "🏯 行宫");
        RESIDENCE_NAMES.put("dove_house", "🕊️ 小白鸽家");
        RESIDENCE_NAMES.put("park", "🌳 公园");
    }
    
    /**
     * 记录居住历史
     * @param userId 用户ID
     * @param newResidence 新居住地点
     * @param previousResidence 之前的居住地点
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 是否记录成功
     */
    @Transactional
    public boolean recordResidenceChange(String userId, String newResidence, String previousResidence, 
                                       String ipAddress, String userAgent) {
        try {
            ResidenceHistory history = new ResidenceHistory(userId, newResidence, previousResidence, ipAddress, userAgent);
            int result = residenceHistoryMapper.insert(history);
            
            if (result > 0) {
                logger.info("用户 {} 居住历史记录成功：从 {} 搬到 {}", 
                           userId, 
                           getResidenceName(previousResidence), 
                           getResidenceName(newResidence));
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("记录居住历史失败：用户={}, 新居住地={}, 之前居住地={}", userId, newResidence, previousResidence, e);
            return false;
        }
    }
    
    /**
     * 获取用户的居住历史
     * @param userId 用户ID
     * @return 居住历史列表
     */
    public List<ResidenceHistory> getUserResidenceHistory(String userId) {
        try {
            return residenceHistoryMapper.selectByUserId(userId);
        } catch (Exception e) {
            logger.error("获取用户居住历史失败：用户={}", userId, e);
            return List.of();
        }
    }
    
    /**
     * 获取指定居住地点的历史记录
     * @param residence 居住地点
     * @return 居住历史列表
     */
    public List<ResidenceHistory> getResidenceHistory(String residence) {
        try {
            return residenceHistoryMapper.selectByResidence(residence);
        } catch (Exception e) {
            logger.error("获取居住地点历史失败：居住地={}", residence, e);
            return List.of();
        }
    }
    
    /**
     * 获取所有居住历史记录（分页）
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 居住历史记录和分页信息
     */
    public Map<String, Object> getAllResidenceHistory(int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<ResidenceHistory> records = residenceHistoryMapper.selectAll(offset, size);
            int total = residenceHistoryMapper.countAll();
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (int) Math.ceil((double) total / size));
            
            return result;
        } catch (Exception e) {
            logger.error("获取所有居住历史失败：page={}, size={}", page, size, e);
            return Map.of("records", List.of(), "total", 0, "page", page, "size", size, "totalPages", 0);
        }
    }
    
    /**
     * 获取用户的最新居住记录
     * @param userId 用户ID
     * @return 最新居住记录
     */
    public ResidenceHistory getLatestResidenceHistory(String userId) {
        try {
            return residenceHistoryMapper.selectLatestByUserId(userId);
        } catch (Exception e) {
            logger.error("获取用户最新居住记录失败：用户={}", userId, e);
            return null;
        }
    }
    
    /**
     * 获取居住历史统计信息
     * @return 统计信息
     */
    public Map<String, Object> getResidenceStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 总搬迁次数
            int totalMoves = residenceHistoryMapper.countAll();
            stats.put("totalMoves", totalMoves);
            
            // 各居住地点的搬入次数
            Map<String, Integer> residenceStats = new HashMap<>();
            for (String residence : RESIDENCE_NAMES.keySet()) {
                int count = residenceHistoryMapper.countByResidence(residence);
                residenceStats.put(getResidenceName(residence), count);
            }
            stats.put("residenceStats", residenceStats);
            
            // 最近搬迁记录
            List<ResidenceHistory> recentMoves = residenceHistoryMapper.selectAll(0, 5);
            stats.put("recentMoves", recentMoves);
            
            return stats;
        } catch (Exception e) {
            logger.error("获取居住统计信息失败", e);
            return Map.of("totalMoves", 0, "residenceStats", Map.of(), "recentMoves", List.of());
        }
    }
    
    /**
     * 删除居住历史记录（管理员功能）
     * @param id 记录ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteResidenceHistory(Long id) {
        try {
            int result = residenceHistoryMapper.deleteById(id);
            if (result > 0) {
                logger.info("管理员删除了居住历史记录：ID={}", id);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("删除居住历史记录失败：ID={}", id, e);
            return false;
        }
    }
    
    /**
     * 获取居住地点的中文名称
     * @param residence 居住地点代码
     * @return 中文名称
     */
    private String getResidenceName(String residence) {
        return RESIDENCE_NAMES.getOrDefault(residence, residence);
    }
}
