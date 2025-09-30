package com.eden.lottery.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化器
 * 在应用启动时创建表结构
 */
@Component
@Order(1) // 确保在奖品初始化之前执行
public class DatabaseInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            initializeDatabase();
        } catch (Exception e) {
            logger.error("初始化数据库失败", e);
            throw new RuntimeException("数据库初始化失败", e);
        }
    }

    /**
     * 初始化数据库表结构
     */
    private void initializeDatabase() {
        try {
            logger.info("开始初始化数据库表结构...");

            // 读取SQL脚本
            ClassPathResource resource = new ClassPathResource("sql/schema.sql");
            String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            // 执行SQL脚本（分割多个语句）
            String[] statements = sql.split(";");
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            logger.info("数据库表结构初始化完成");
        } catch (Exception e) {
            logger.error("初始化数据库表结构失败", e);
        }
    }
}
