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
     const [currentPrize, setCurrentPrize] = useState('') // 存储后端返回的奖品名称
     const [userName, setUserName] = useState('') // 用户姓名
     const [showNameInput, setShowNameInput] = useState(true) // 是否显示姓名输入框
     const [tempName, setTempName] = useState('') // 临时存储输入的姓名

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
         
         // 检查是否已填写用户姓名
         if (!userName) {
             alert('请先填写用户姓名！')
             setShowNameInput(true)
             return
         }

         setIsSpinning(true)
         setResult('')
         setCurrentPrize('') // 清空之前的奖品缓存

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
                     userId: userName || 'anonymous'
                 })
             })

             const result = await response.json()

             if (result.success) {
                 const prizeName = result.data.prize.name
                 
                 // 保存后端返回的奖品名称
                 setCurrentPrize(prizeName)

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
        
        // 优先使用后端返回的奖品名称，如果没有则尝试解析转盘返回的索引
        if (currentPrize) {
            setResult(currentPrize)
        } else {
            // 备用方案：尝试从转盘回调解析索引
            let prizeIndex;
            if (typeof prize === 'number') {
                prizeIndex = prize;
            } else if (prize && typeof prize === 'object') {
                prizeIndex = prize.index || 0;
            } else {
                prizeIndex = 0;
            }
            
            const prizeText = prizeNames[prizeIndex]
            setResult(prizeText)
        }
    }

    // 处理姓名确认
    const handleNameConfirm = () => {
        if (!tempName.trim()) {
            alert('请输入您的姓名！')
            return
        }
        setUserName(tempName.trim())
        setShowNameInput(false)
    }

    // 处理键盘回车
    const handleNameKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleNameConfirm()
        }
    }

    return (
        <div className="lucky-lottery-container">
            {/* 用户姓名输入模态框 */}
            {showNameInput && (
                <div className="name-input-modal">
                    <div className="name-input-content">
                        <h2 className="name-input-title">🎪 欢迎来到Eden抽奖</h2>
                        <p className="name-input-subtitle">命运是一件奇妙的事。它一旦来临，就必须接受。</p>
                        <p className="name-input-subtitle">请输入您的姓名❤️：</p>
                        <div className="name-input-field">
                            <input
                                type="text"
                                placeholder="请输入您的姓名"
                                value={tempName}
                                onChange={(e) => setTempName(e.target.value)}
                                onKeyPress={handleNameKeyPress}
                                className="name-input"
                                autoFocus
                                maxLength={20}
                            />
                        </div>
                        <button
                            className="name-confirm-button"
                            onClick={handleNameConfirm}
                            disabled={!tempName.trim()}
                        >
                            🎯 开始游戏
                        </button>
                    </div>
                </div>
            )}

            {/* 标题 */}
            <div className="header">
                <h1 className="title">🎪 Eden欢乐抽奖 🎪</h1>
                <p className="subtitle">
                    {userName ? `${userName}，转动转盘，好运连连！` : '转动转盘，好运连连！'}
                </p>
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
                {/* 用户信息行 */}
                {userName && (
                    <div className="user-info-row">
                        <div 
                            className={`current-user ${isSpinning ? 'disabled' : 'clickable'}`}
                            onClick={() => {
                                if (!isSpinning) {
                                    setShowNameInput(true)
                                    setTempName(userName)
                                }
                            }}
                            title="点击修改姓名"
                        >
                            👤 {userName}
                        </div>
                    </div>
                )}
                
                {/* 开始抽奖按钮 */}
                <button
                    className={`spin-button ${isSpinning || !userName ? 'disabled' : ''}`}
                    onClick={startSpin}
                    disabled={isSpinning || !userName}
                >
                    {isSpinning ? '🎯 转动中...' : '🎲 开始抽奖'}
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
                            onClick={() => {
                                setResult('')
                                setCurrentPrize('')
                            }}
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

