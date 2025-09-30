const express = require('express')
const cors = require('cors')
const bodyParser = require('body-parser')

const app = express()
const PORT = process.env.PORT || 5000

// 中间件
app.use(cors())
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))

// 奖品配置
const prizes = [
  { id: 1, name: '🎁 大奖', probability: 0.05, level: 'epic' },
  { id: 2, name: '🎮 游戏币', probability: 0.15, level: 'rare' },
  { id: 3, name: '🎪 再来一次', probability: 0.20, level: 'common' },
  { id: 4, name: '🎈 小奖品', probability: 0.25, level: 'common' },
  { id: 5, name: '🍭 糖果', probability: 0.15, level: 'common' },
  { id: 6, name: '🎨 贴纸', probability: 0.10, level: 'uncommon' },
  { id: 7, name: '🌟 积分', probability: 0.10, level: 'uncommon' }
]

// 用户抽奖记录（简单内存存储，实际项目应使用数据库）
const userRecords = []

// API路由

// 获取奖品列表
app.get('/api/prizes', (req, res) => {
  res.json({
    success: true,
    data: prizes.map(prize => ({
      id: prize.id,
      name: prize.name,
      level: prize.level
      // 不返回概率信息给前端
    }))
  })
})

// 抽奖API
app.post('/api/lottery', (req, res) => {
  try {
    const { userId = 'anonymous' } = req.body

    // 基于概率选择奖品
    const random = Math.random()
    let cumulativeProbability = 0
    let selectedPrize = prizes[0]

    for (let prize of prizes) {
      cumulativeProbability += prize.probability
      if (random <= cumulativeProbability) {
        selectedPrize = prize
        break
      }
    }

    // 记录抽奖结果
    const record = {
      id: Date.now().toString(),
      userId,
      prize: selectedPrize,
      timestamp: new Date().toISOString(),
      ip: req.ip
    }
    
    userRecords.push(record)

    // 返回抽奖结果
    res.json({
      success: true,
      data: {
        prize: {
          id: selectedPrize.id,
          name: selectedPrize.name,
          level: selectedPrize.level
        },
        recordId: record.id,
        timestamp: record.timestamp
      }
    })

  } catch (error) {
    console.error('抽奖错误:', error)
    res.status(500).json({
      success: false,
      message: '抽奖系统暂时不可用，请稍后再试'
    })
  }
})

// 获取抽奖记录
app.get('/api/records/:userId?', (req, res) => {
  try {
    const { userId } = req.params
    
    let records = userRecords
    if (userId && userId !== 'all') {
      records = userRecords.filter(record => record.userId === userId)
    }
    
    // 只返回最近50条记录
    const recentRecords = records
      .sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
      .slice(0, 50)
      .map(record => ({
        id: record.id,
        prize: record.prize.name,
        level: record.prize.level,
        timestamp: record.timestamp
      }))

    res.json({
      success: true,
      data: recentRecords
    })

  } catch (error) {
    console.error('获取记录错误:', error)
    res.status(500).json({
      success: false,
      message: '获取记录失败'
    })
  }
})

// 统计信息
app.get('/api/stats', (req, res) => {
  try {
    const totalDraws = userRecords.length
    const prizeStats = prizes.map(prize => ({
      name: prize.name,
      level: prize.level,
      count: userRecords.filter(record => record.prize.id === prize.id).length
    }))

    res.json({
      success: true,
      data: {
        totalDraws,
        prizeStats,
        lastUpdate: new Date().toISOString()
      }
    })

  } catch (error) {
    console.error('统计信息错误:', error)
    res.status(500).json({
      success: false,
      message: '获取统计信息失败'
    })
  }
})

// 健康检查
app.get('/api/health', (req, res) => {
  res.json({
    success: true,
    message: 'Eden抽奖服务运行正常',
    timestamp: new Date().toISOString()
  })
})

// 默认路由
app.get('/', (req, res) => {
  res.json({
    message: '🎪 欢迎来到Eden抽奖系统API服务 🎪',
    version: '1.0.0',
    endpoints: {
      prizes: 'GET /api/prizes',
      lottery: 'POST /api/lottery',
      records: 'GET /api/records/:userId?',
      stats: 'GET /api/stats',
      health: 'GET /api/health'
    }
  })
})

// 错误处理中间件
app.use((err, req, res, next) => {
  console.error(err.stack)
  res.status(500).json({
    success: false,
    message: '服务器内部错误'
  })
})

// 404处理
app.use('*', (req, res) => {
  res.status(404).json({
    success: false,
    message: '接口不存在'
  })
})

// 启动服务器
app.listen(PORT, () => {
  console.log(`🎪 Eden抽奖服务器已启动`)
  console.log(`🔗 服务地址: http://localhost:${PORT}`)
  console.log(`📚 API文档: http://localhost:${PORT}`)
})
