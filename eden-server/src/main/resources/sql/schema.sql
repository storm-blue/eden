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

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_lottery_records_user_id ON lottery_records(user_id);
CREATE INDEX IF NOT EXISTS idx_lottery_records_created_at ON lottery_records(created_at);
CREATE INDEX IF NOT EXISTS idx_lottery_records_prize_id ON lottery_records(prize_id);
CREATE INDEX IF NOT EXISTS idx_prizes_level ON prizes(level);
CREATE INDEX IF NOT EXISTS idx_prizes_probability ON prizes(probability);
