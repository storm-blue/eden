import React, {useRef, useState} from 'react'
import {LuckyWheel} from '@lucky-canvas/react'
import './LuckyWheel.css'

const LotteryLuckyWheel = () => {
    const [prizes, setPrizes] = useState([
        {
            background: '#ffcdd2', // 浅粉红色 🌈
            fonts: [{
                text: '🍰',
                top: '15%',
                fontSize: '30px'
            }, {
                text: '吃的～',
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
                text: '喝的～',
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
                text: '爱',
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
                fontSize: '30px'
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
                fontSize: '30px'
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
            background: '#ff6ec7', // 明亮红色外圈 🌈
        },
        {
            radius: '50px',
            background: '#fff5ca' // 纯白中圈
        },
        {
            radius: '45px',
            background: '#f0caff' // 纯白中圈
        },
        {
            radius: '40px',
            background: '#fdeeff' // 纯白中圈
        },
        {
            radius: '35px',
            background: '#678cff', // 明亮蓝色内圈 🌈
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

     // 奖品名称映射（与后端保持一致）
     const prizeNames = [
         '🍰 吃的～',
         '🥤 喝的～',
         '❤️ 爱',
         '💸 空空如也',
         '🧧 红包',
         '🔄 再转一次',
         '🎁 随机礼物'
     ]

     const startSpin = async () => {
         if (isSpinning) return

         setIsSpinning(true)
         setResult('')

         try {
             // 开始转盘动画
             myLucky.current.play()

             // 调用后端抽奖接口
             const response = await fetch('http://localhost:5000/api/lottery', {
                 method: 'POST',
                 headers: {
                     'Content-Type': 'application/json',
                 },
                 body: JSON.stringify({
                     userId: 'web_user_' + Date.now()
                 })
             })

             const result = await response.json()

             if (result.success) {
                 const prizeName = result.data.prize.name
                 console.log('抽奖成功:', prizeName)

                 // 根据奖品名称找到对应的索引
                 let selectedIndex = prizeNames.findIndex(name => name === prizeName)
                 if (selectedIndex === -1) {
                     selectedIndex = 0 // 默认第一个
                 }

                 // 延迟停止转盘，让动画更自然
                 setTimeout(() => {
                     myLucky.current.stop(selectedIndex)
                 }, 1500)
             } else {
                 console.error('抽奖失败:', result.message)
                 setIsSpinning(false)
                 alert('抽奖失败，请稍后再试: ' + result.message)
             }
         } catch (error) {
             console.error('网络错误:', error)
             setIsSpinning(false)
             alert('网络连接失败，请检查后端服务是否启动')
         }
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
                    onStart={() => {
                    }} // 点击抽奖按钮会触发
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
