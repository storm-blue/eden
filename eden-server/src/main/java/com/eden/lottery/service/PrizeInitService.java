package com.eden.lottery.service;

import com.eden.lottery.entity.Prize;
import com.eden.lottery.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 奖品初始化服务
 * 每次应用启动时都会重新初始化奖品数据
 * 同时负责数据库表结构迁移
 */
@Service
@Order(1) // 确保最先执行
public class PrizeInitService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(PrizeInitService.class);

    @Autowired
    private PrizeMapper prizeMapper;
    
    @Autowired
    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 首先执行数据库迁移
            performDatabaseMigration();
            
            // 然后初始化奖品数据
            initializePrizes();
        } catch (Exception e) {
            logger.error("服务初始化失败", e);
            throw new RuntimeException("服务初始化失败", e);
        }
    }
    
    /**
     * 执行数据库迁移
     */
    private void performDatabaseMigration() {
        logger.info("开始数据库迁移检查...");
        
        try (Connection connection = dataSource.getConnection()) {
            // 检查并迁移users表
            checkAndMigrateUsersTable(connection);
            
            // 检查并创建居住历史表
            checkAndCreateResidenceHistoryTable(connection);
            
            logger.info("数据库迁移检查完成");
        } catch (Exception e) {
            logger.error("数据库迁移失败", e);
            throw new RuntimeException("数据库迁移失败", e);
        }
    }
    
    /**
     * 检查并迁移users表
     */
    private void checkAndMigrateUsersTable(Connection connection) throws Exception {
        List<String> columns = getTableColumns(connection, "users");
        
        // 检查wish_count列是否存在
        if (!columns.contains("wish_count")) {
            logger.info("users表缺少wish_count列，添加列...");
            addWishCountColumn(connection);
        }
        
        // 检查residence列是否存在
        if (!columns.contains("residence")) {
            logger.info("users表缺少residence列，添加列...");
            addResidenceColumn(connection);
        }
        
        logger.info("users表结构检查完成");
    }
    
    /**
     * 获取表的所有列名
     */
    private List<String> getTableColumns(Connection connection, String tableName) throws Exception {
        List<String> columns = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        
        try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
        }
        
        return columns;
    }
    
    /**
     * 添加wish_count列到users表
     */
    private void addWishCountColumn(Connection connection) throws Exception {
        String sql = "ALTER TABLE users ADD COLUMN wish_count INTEGER NOT NULL DEFAULT 0";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            logger.info("wish_count列添加成功");
        }
    }
    
    /**
     * 添加residence列到users表
     */
    private void addResidenceColumn(Connection connection) throws Exception {
        String sql = "ALTER TABLE users ADD COLUMN residence VARCHAR(20) DEFAULT NULL";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            logger.info("residence列添加成功");
        }
    }
    
    /**
     * 检查并创建居住历史表
     */
    private void checkAndCreateResidenceHistoryTable(Connection connection) throws Exception {
        // 检查表是否存在
        if (!tableExists(connection, "residence_history")) {
            logger.info("residence_history表不存在，创建表...");
            createResidenceHistoryTable(connection);
        } else {
            logger.info("residence_history表已存在");
        }
    }
    
    /**
     * 检查表是否存在
     */
    private boolean tableExists(Connection connection, String tableName) throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }
    
    /**
     * 创建居住历史表
     */
    private void createResidenceHistoryTable(Connection connection) throws Exception {
        String sql = """
            CREATE TABLE residence_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id VARCHAR(50) NOT NULL,
                residence VARCHAR(20) NOT NULL,
                previous_residence VARCHAR(20) DEFAULT NULL,
                change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                ip_address VARCHAR(45),
                user_agent VARCHAR(500)
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            logger.info("residence_history表创建成功");
            
            // 创建索引
            createResidenceHistoryIndexes(connection);
        }
    }
    
    /**
     * 创建居住历史表的索引
     */
    private void createResidenceHistoryIndexes(Connection connection) throws Exception {
        String[] indexSqls = {
            "CREATE INDEX IF NOT EXISTS idx_residence_history_user_id ON residence_history(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_residence_history_residence ON residence_history(residence)",
            "CREATE INDEX IF NOT EXISTS idx_residence_history_change_time ON residence_history(change_time)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String indexSql : indexSqls) {
                stmt.execute(indexSql);
            }
            logger.info("residence_history表索引创建成功");
        }
    }

    /**
     * 初始化奖品数据
     */
    private void initializePrizes() {
        reinitializePrizes();
    }

    /**
     * 重新初始化奖品数据
     * 使用UPSERT模式：存在则更新，不存在则插入
     */
    private void reinitializePrizes() {
        logger.info("开始重新初始化奖品数据...");

        // 查询现有奖品数量
        List<Prize> existingPrizes = prizeMapper.selectAll();
        logger.info("当前数据库中有 {} 个奖品", existingPrizes.size());

        // 创建默认奖品配置（带固定ID）
        // 注意：顺序必须与前端LuckyWheel.jsx中的prizes数组顺序一致！
        Prize[] defaultPrizes = {
                createPrizeWithId(1L, "🍰 吃的～", 0.07, "common"),      // 索引0: 8%
                createPrizeWithId(2L, "🥤 喝的～", 0.07, "common"),      // 索引1: 8%
                createPrizeWithId(3L, "❤️ 爱", 0.001, "epic"),           // 索引2: 0.2%
                createPrizeWithId(4L, "💸 空空如也", 0.30, "common"),    // 索引3: 40%
                createPrizeWithId(5L, "🧧 红包", 0.03, "uncommon"),      // 索引4: 5%
                createPrizeWithId(6L, "🔄 再转一次", 0.28, "special"),   // 索引5: 30%
                createPrizeWithId(7L, "🎁 随机礼物", 0.039, "rare"),     // 索引6: 2.8%
                createPrizeWithId(8L, "💬 陪聊服务", 0.06, "rare"),     // 索引7: 6%
                createPrizeWithId(9L, "✨ 许愿一次", 0.15, "rare")
        };

        // 验证概率总和
        double totalProbability = 0.0;
        for (Prize prize : defaultPrizes) {
            totalProbability += prize.getProbability();
        }

        if (Math.abs(totalProbability - 1.0) > 0.001) {
            logger.warn("⚠️ 警告：奖品概率总和为 {}, 不等于1.0，可能导致抽奖异常", totalProbability);
        } else {
            logger.info("✅ 概率验证通过：总和为 {}", totalProbability);
        }

        // 使用UPSERT插入或更新奖品数据
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            int result = prizeMapper.insertOrUpdate(prize);
            String action = existingPrizes.stream().anyMatch(p -> p.getId().equals(prize.getId())) ? "更新" : "插入";
            logger.info("{}奖品[{}]: {} - 概率: {}% - 级别: {}",
                    action, i, prize.getName(), prize.getProbability() * 100, prize.getLevel());
        }

        logger.info("🎉 奖品重新初始化完成！共配置 {} 个奖品，概率总和: {}%",
                defaultPrizes.length, totalProbability * 100);

        // 输出配置摘要
        logger.info("📋 奖品配置摘要:");
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            logger.info("   ID{}/索引{}: {} ({}%)", prize.getId(), i, prize.getName(), prize.getProbability() * 100);
        }
    }

    /**
     * 创建带ID的奖品对象
     */
    private Prize createPrizeWithId(Long id, String name, Double probability, String level) {
        Prize prize = new Prize(name, probability, level);
        prize.setId(id);
        return prize;
    }
}
