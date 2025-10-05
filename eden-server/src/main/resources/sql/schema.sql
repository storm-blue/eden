-- 创建数据库表结构
-- SQLite数据库初始化脚本

-- 奖品表
CREATE TABLE IF NOT EXISTS prizes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    probability DOUBLE NOT NULL,
    level VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 抽奖记录表
CREATE TABLE IF NOT EXISTS lottery_records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id VARCHAR(100),
    prize_id INTEGER NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prize_id) REFERENCES prizes(id)
);

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    remaining_draws INTEGER NOT NULL DEFAULT 0,
    daily_draws INTEGER NOT NULL DEFAULT 3,
    wish_count INTEGER NOT NULL DEFAULT 0,  -- 可用许愿次数
    residence VARCHAR(20) DEFAULT NULL,     -- 居住地点: 'castle', 'city_hall', 'palace', 'dove_house', 'park'
    avatar_path VARCHAR(255) DEFAULT NULL,  -- 头像文件路径
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_refresh_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户尝试记录表
CREATE TABLE IF NOT EXISTS user_attempts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    attempt_user_id VARCHAR(50) NOT NULL,
    user_exists INTEGER NOT NULL DEFAULT 0,  -- 0: 不存在, 1: 存在
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    attempt_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 许愿表
CREATE TABLE IF NOT EXISTS wishes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id VARCHAR(50) NOT NULL,
    wish_content TEXT NOT NULL,
    star_x DOUBLE NOT NULL,  -- 星星X坐标 (0-100%)
    star_y DOUBLE NOT NULL,  -- 星星Y坐标 (0-100%)
    star_size INTEGER DEFAULT 3,  -- 星星大小 (1-5)
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_lottery_records_user_id ON lottery_records(user_id);
CREATE INDEX IF NOT EXISTS idx_lottery_records_created_at ON lottery_records(created_at);
CREATE INDEX IF NOT EXISTS idx_lottery_records_prize_id ON lottery_records(prize_id);
CREATE INDEX IF NOT EXISTS idx_prizes_level ON prizes(level);
CREATE INDEX IF NOT EXISTS idx_prizes_probability ON prizes(probability);
CREATE INDEX IF NOT EXISTS idx_users_last_refresh_date ON users(last_refresh_date);
CREATE INDEX IF NOT EXISTS idx_user_attempts_user_id ON user_attempts(attempt_user_id);
CREATE INDEX IF NOT EXISTS idx_user_attempts_time ON user_attempts(attempt_time);
CREATE INDEX IF NOT EXISTS idx_user_attempts_exists ON user_attempts(user_exists);
CREATE INDEX IF NOT EXISTS idx_wishes_user_id ON wishes(user_id);
CREATE INDEX IF NOT EXISTS idx_wishes_create_time ON wishes(create_time);

-- 星星城数据表
CREATE TABLE IF NOT EXISTS star_city (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    population INTEGER NOT NULL DEFAULT 100000,        -- 人口数量
    food INTEGER NOT NULL DEFAULT 100000,              -- 食物数量
    happiness INTEGER NOT NULL DEFAULT 20,             -- 幸福指数
    level INTEGER NOT NULL DEFAULT 1,                  -- 当前等级
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 最后更新时间
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 星星城相关索引
CREATE INDEX IF NOT EXISTS idx_star_city_level ON star_city(level);
CREATE INDEX IF NOT EXISTS idx_star_city_last_update ON star_city(last_update_time);

-- 居住历史记录表
CREATE TABLE IF NOT EXISTS residence_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id VARCHAR(50) NOT NULL,                          -- 用户ID
    residence VARCHAR(20) NOT NULL,                        -- 居住地点: 'castle', 'city_hall', 'palace', 'dove_house', 'park'
    previous_residence VARCHAR(20) DEFAULT NULL,           -- 之前的居住地点
    change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,       -- 搬迁时间
    ip_address VARCHAR(45),                                -- 用户IP地址
    user_agent VARCHAR(500)                                -- 用户代理信息
);

-- 居住历史相关索引
CREATE INDEX IF NOT EXISTS idx_residence_history_user_id ON residence_history(user_id);
CREATE INDEX IF NOT EXISTS idx_residence_history_residence ON residence_history(residence);
CREATE INDEX IF NOT EXISTS idx_residence_history_change_time ON residence_history(change_time);
