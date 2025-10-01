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
     const [userInfo, setUserInfo] = useState(null) // 用户信息（包含剩余抽奖次数）
     const [showWelcomeEffect, setShowWelcomeEffect] = useState(false) // 是否显示欢迎特效
     const [welcomeEffectFinished, setWelcomeEffectFinished] = useState(false) // 欢迎特效是否播放完成

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

    // 获取用户信息
    const fetchUserInfo = async (userId) => {
        try {
            const response = await fetch(`http://localhost:5000/api/user/${userId}`)
            const result = await response.json()
            
            if (result.success) {
                setUserInfo(result.data)
                console.log('获取用户信息成功:', result.data)
            } else {
                console.error('获取用户信息失败:', result.message)
            }
        } catch (error) {
            console.error('获取用户信息网络错误:', error)
        }
    }

     const startSpin = async () => {
         if (isSpinning) return
         
         // 检查是否已填写用户姓名
         if (!userName) {
             alert('请先填写用户姓名！')
             setShowNameInput(true)
             return
         }

         // 检查欢迎特效是否播放完成
         if (!welcomeEffectFinished) {
             alert('请等待欢迎特效播放完成！')
             return
         }

         // 检查用户是否存在和剩余抽奖次数
         if (!userInfo || userInfo.remainingDraws <= 0) {
             if (!userInfo || userInfo.message === "用户不存在") {
                 alert('用户不存在，请联系管理员创建账户！')
             } else {
                 alert('您的抽奖次数已用完，请明天再来！')
             }
             return
         }

         setIsSpinning(true)
         setResult('')
         setCurrentPrize('') // 清空之前的奖品缓存

         try {
             // 先调用后端抽奖接口，成功后再开始转盘动画
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

                 // 后端抽奖成功，开始转盘动画
                 myLucky.current.play()
                 
                 // 延迟停止转盘，让动画更自然
                 setTimeout(() => {
                     myLucky.current.stop(selectedIndex)
                 }, 1500)
                 
                 // 刷新用户信息以显示最新的剩余次数
                 await fetchUserInfo(userName)
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

    const onEnd = async (prize) => {
        setIsSpinning(false)
        
        // 优先使用后端返回的奖品名称，如果没有则尝试解析转盘返回的索引
        if (currentPrize) {
            setResult(currentPrize)
            
            // 检查是否抽到"再转一次"，如果是则刷新用户信息显示额外获得的次数
            if (currentPrize === '🔄 再转一次') {
                // 延迟刷新用户信息，确保后端处理完毕
                setTimeout(async () => {
                    await fetchUserInfo(userName)
                    console.log('抽到再转一次，已刷新剩余次数')
                }, 500)
            }
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
            
            // 检查是否抽到"再转一次"
            if (prizeText === '🔄 再转一次') {
                setTimeout(async () => {
                    await fetchUserInfo(userName)
                    console.log('抽到再转一次，已刷新剩余次数')
                }, 500)
            }
        }
    }

    // 处理姓名确认
    const handleNameConfirm = async () => {
        if (!tempName.trim()) {
            alert('请输入您的姓名！')
            return
        }
        const newUserName = tempName.trim()
        setUserName(newUserName)
        setShowNameInput(false)
        
        // 显示欢迎特效
        setShowWelcomeEffect(true)
        setWelcomeEffectFinished(false)
    }

    // 处理键盘回车
    const handleNameKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleNameConfirm()
        }
    }

    // 处理欢迎特效继续按钮
    const handleWelcomeContinue = async () => {
        setShowWelcomeEffect(false)
        setWelcomeEffectFinished(true)
        // 获取用户信息
        await fetchUserInfo(userName)
    }

    return (
        <div className="lucky-lottery-container">
            {/* 用户姓名输入模态框 */}
            {showNameInput && (
                <div className="name-input-modal">
                    <div className="name-input-content">
                        <h2 className="name-input-title">🎪 欢迎来到Eden抽奖</h2>
                        <p className="name-input-subtitle"><u>❤命运一旦来临，就必须接受它❤</u></p>
                        <p className="name-input-subtitle">请输入您的姓名：</p>
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
                {/* 转盘中心显示剩余次数 */}
                {userName && (
                    <div className="wheel-center-info">
                        <div className="center-remaining-draws">
                            剩余
                        </div>
                        <div className="center-remaining-number">
                            {userInfo ? userInfo.remainingDraws : 0}
                        </div>
                        <div className="center-remaining-unit">
                            次
                        </div>
                    </div>
                )}
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
                    className={`spin-button ${isSpinning || !userName || !userInfo || !welcomeEffectFinished || userInfo.remainingDraws <= 0 ? 'disabled' : ''}`}
                    onClick={startSpin}
                    disabled={isSpinning || !userName || !userInfo || !welcomeEffectFinished || userInfo.remainingDraws <= 0}
                >
                    {isSpinning ? '🎯 转动中...' : 
                     !welcomeEffectFinished ? '🎪 欢迎特效中...' :
                     (!userInfo || userInfo.message === "用户不存在") ? '👤 用户不存在' :
                     (userInfo.remainingDraws <= 0) ? '🚫 次数已用完' : 
                     '🎲 转动命运'}
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

            {/* 欢迎特效 */}
            {showWelcomeEffect && (
                <div className="welcome-effect-overlay" onClick={handleWelcomeContinue}>
                    <div className="welcome-effect-container">
                        {/* 礼花特效 */}
                        <div className="fireworks">
                            <div className="firework firework-1">
                                <div className="explosion explosion-1"></div>
                            </div>
                            <div className="firework firework-2">
                                <div className="explosion explosion-2"></div>
                            </div>
                            <div className="firework firework-3">
                                <div className="explosion explosion-3"></div>
                            </div>
                            <div className="firework firework-4">
                                <div className="explosion explosion-4"></div>
                            </div>
                            <div className="firework firework-5">
                                <div className="explosion explosion-5"></div>
                            </div>
                        </div>
                        
                        {/* 闪烁星星 */}
                        <div className="sparkles">
                            <div className="sparkle sparkle-1">⭐</div>
                            <div className="sparkle sparkle-2">🌟</div>
                            <div className="sparkle sparkle-3">✨</div>
                            <div className="sparkle sparkle-4">💫</div>
                            <div className="sparkle sparkle-5">⭐</div>
                            <div className="sparkle sparkle-6">🌟</div>
                            <div className="sparkle sparkle-7">✨</div>
                            <div className="sparkle sparkle-8">💫</div>
                        </div>

                          {/* 欢迎文字 */}
                          <div className="welcome-text">
                              <h1 className="welcome-title">欢迎宝宝大人</h1>
                              <p className="welcome-subtitle">✨ {userName} ✨</p>
                              <p className="welcome-message">准备好接受命运的眷顾了吗？</p>
                              <p className="welcome-continue-hint">点击继续 🎯</p>
                          </div>

                        {/* 彩带效果 */}
                        <div className="confetti">
                            <div className="confetti-piece confetti-1"></div>
                            <div className="confetti-piece confetti-2"></div>
                            <div className="confetti-piece confetti-3"></div>
                            <div className="confetti-piece confetti-4"></div>
                            <div className="confetti-piece confetti-5"></div>
                            <div className="confetti-piece confetti-6"></div>
                            <div className="confetti-piece confetti-7"></div>
                            <div className="confetti-piece confetti-8"></div>
                        </div>
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

