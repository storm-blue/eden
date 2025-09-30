import React, { useState, useRef } from 'react'
import { LuckyWheel } from '@lucky-canvas/react'
import './LuckyWheel.css'

const LotteryLuckyWheel = () => {
  const [prizes, setPrizes] = useState([
    { 
      background: '#ffcdd2', // 浅粉红色 🌈
      fonts: [{ 
        text: '🍰', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '好吃的', 
        top: '55%', 
        fontSize: '16px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#ffe0b2', // 浅橙色 🌈
      fonts: [{ 
        text: '🥤', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '好喝的', 
        top: '55%', 
        fontSize: '16px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#fff9c4', // 浅黄色 🌈
      fonts: [{ 
        text: '❤️', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '爱心', 
        top: '55%', 
        fontSize: '18px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#c8e6c9', // 浅绿色 🌈
      fonts: [{ 
        text: '💸', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '空空如也', 
        top: '55%', 
        fontSize: '14px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#b3e5fc', // 浅青色 🌈
      fonts: [{ 
        text: '🧧', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '红包', 
        top: '55%', 
        fontSize: '18px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#bbdefb', // 浅蓝色 🌈
      fonts: [{ 
        text: '🔄', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '再转一次', 
        top: '55%', 
        fontSize: '14px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
    { 
      background: '#e1bee7', // 浅紫色 🌈
      fonts: [{ 
        text: '🎁', 
        top: '15%', 
        fontSize: '35px'
      }, { 
        text: '随机礼物', 
        top: '55%', 
        fontSize: '14px',
        fontColor: '#333',
        fontWeight: 'bold'
      }] 
    },
  ])

  const [blocks, setBlocks] = useState([
    { 
      padding: '10px', 
      background: '#ff6ec7', // 亮粉色外圈 🌈
      paddingColor: '#ffea00'
    },
    { 
      padding: '10px', 
      background: '#ffffff', // 纯白色内圈
      paddingColor: '#00e5ff'
    }
  ])

  const [buttons, setButtons] = useState([
    { 
      radius: '55px', 
      background: '#ff1744', // 明亮红色外圈 🌈
    },
    { 
      radius: '45px', 
      background: '#ffffff' // 纯白中圈
    },
    { 
      radius: '35px', 
      background: '#2979ff', // 明亮蓝色内圈 🌈
      pointer: true, // 官方指针配置
      fonts: []
    }
  ])

  // 转盘配置
  const [defaultConfig, setDefaultConfig] = useState({
    gutter: 8,
    offsetDegree: 0,
    speed: 20,
    accelerationTime: 3000,
    decelerationTime: 3000
  })

  // 指针样式配置 - 让指针更细一些
  const [defaultStyle, setDefaultStyle] = useState({
    pointer: {
      style: 'triangle',
      background: '#ff1744',
      borderColor: '#ffffff', 
      borderWidth: 0.5, // 更细的边框
      width: 10, // 指针宽度
      height: 20 // 保持长度
    }
  })

  const myLucky = useRef()
  const [isSpinning, setIsSpinning] = useState(false)
  const [result, setResult] = useState('')

  // 奖品概率配置（对应prizes数组的索引）
  const prizeProbabilities = [0.15, 0.20, 0.25, 0.15, 0.10, 0.10, 0.05]
  // 好吃的15%, 好喝的20%, 爱心25%, 空空如也15%, 红包10%, 再转一次10%, 随机礼物5%
  
  // 奖品名称映射
  const prizeNames = [
    '🍰 好吃的',
    '🥤 好喝的', 
    '❤️ 爱心',
    '💸 空空如也',
    '🧧 红包',
    '🔄 再转一次',
    '🎁 随机礼物'
  ]

  const startSpin = () => {
    if (isSpinning) return
    
    setIsSpinning(true)
    setResult('')

    // 基于概率选择奖品
    const random = Math.random()
    let cumulativeProbability = 0
    let selectedIndex = 0

    for (let i = 0; i < prizeProbabilities.length; i++) {
      cumulativeProbability += prizeProbabilities[i]
      if (random <= cumulativeProbability) {
        selectedIndex = i
        break
      }
    }

    console.log(`随机数: ${random}, 选中索引: ${selectedIndex}, 奖品: ${prizeNames[selectedIndex]}`)

    // 开始抽奖动画，LuckyCanvas会自动处理角度计算
    myLucky.current.play()
    
    // 设置停止位置
    setTimeout(() => {
      myLucky.current.stop(selectedIndex)
    }, 2000)
  }

  const onEnd = (prize) => {
    setIsSpinning(false)
    const prizeText = prizeNames[prize]
    setResult(prizeText)
    console.log(`抽奖结果: ${prizeText}`)
  }

  const resetWheel = () => {
    setResult('')
    setIsSpinning(false)
  }

  return (
    <div className="lucky-lottery-container">
      {/* 标题 */}
      <div className="header">
        <h1 className="title">🎪 Eden欢乐抽奖 🎪</h1>
        <p className="subtitle">转动转盘，好运连连！</p>
      </div>

      {/* 转盘区域 */}
      <div className="wheel-container">
        <LuckyWheel
          ref={myLucky}
          width="380px"
          height="380px"
          prizes={prizes}
          blocks={blocks}
          buttons={buttons}
          defaultConfig={defaultConfig}
          defaultStyle={defaultStyle}
          onStart={() => {}} // 点击抽奖按钮会触发
          onEnd={onEnd}
        />
      </div>

      {/* 控制按钮 */}
      <div className="controls">
        <button 
          className={`spin-button ${isSpinning ? 'disabled' : ''}`}
          onClick={startSpin}
          disabled={isSpinning}
        >
          {isSpinning ? '🎯 转动中...' : '🎲 开始抽奖'}
        </button>
        
        <button 
          className="reset-button"
          onClick={resetWheel}
          disabled={isSpinning}
        >
          🔄 重置
        </button>
      </div>

      {/* 结果显示 */}
      {result && (
        <div className="result-modal">
          <div className="result-content">
            <h2 className="result-title">🎉 恭喜你获得 🎉</h2>
            <div className="result-prize">{result}</div>
            <button 
              className="continue-button"
              onClick={() => setResult('')}
            >
              继续游戏
            </button>
          </div>
        </div>
      )}

      {/* 装饰元素 */}
      <div className="decorations">
        <div className="star star-1">⭐</div>
        <div className="star star-2">🌟</div>
        <div className="star star-3">✨</div>
        <div className="star star-4">💫</div>
      </div>
    </div>
  )
}

export default LotteryLuckyWheel
