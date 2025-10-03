import React, {useRef, useState, useEffect, useMemo} from 'react'
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
        {
            background: '#bec3e7', // 浅紫色 🌈
            fonts: [{
                text: '💬',
                top: '15%',
                fontSize: '30px'
            }, {
                text: '陪聊服务',
                top: '55%',
                fontSize: '14px',
                fontColor: '#333',
                fontWeight: 'bold'
            }]
        },
        {
            background: '#e7bebe', // 浅紫色 🌈
            fonts: [{
                text: '✨',
                top: '15%',
                fontSize: '30px'
            }, {
                text: '许愿一次',
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
    const [welcomeEffectFinished, setWelcomeEffectFinished] = useState(true) // 欢迎特效是否播放完成，默认为true
    const [showLoveEffect, setShowLoveEffect] = useState(false)
    const [showWishPage, setShowWishPage] = useState(false)
    const [wishes, setWishes] = useState([]) // 所有许愿列表
    const [showWishInput, setShowWishInput] = useState(false) // 是否显示许愿输入框
    const [wishContent, setWishContent] = useState('') // 许愿内容
    const [selectedWish, setSelectedWish] = useState(null) // 选中的许愿
    const [showWishAnimation, setShowWishAnimation] = useState(false) // 是否显示许愿变星星动画
    const [animatingWish, setAnimatingWish] = useState(null) // 正在动画的许愿数据
    const [showPrizeStats, setShowPrizeStats] = useState(false) // 是否显示奖品统计
    const [prizeStats, setPrizeStats] = useState([]) // 奖品统计数据

    // 奖品名称映射（与后端保持一致）
  const prizeNames = [
        '🍰 吃的～',
        '🥤 喝的～',
        '❤️ 爱',
    '💸 空空如也',
    '🧧 红包',
    '🔄 再转一次',
        '🎁 随机礼物',
        '💬 陪聊服务',
        '✨ 许愿一次'
    ]

    // 奖品说明映射（支持多个说明，随机展示）
    const prizeDescriptions = {
        '🍰 吃的～': [
            '请联系猫咪主人领取',
            '天上飞的，地上跑的，水里游的',
            '叔叔你怎么跑我肚子里了',
        ],
        '🥤 喝的～': [
            '饮下月亮，撒出月光',
            '我不能喝，一斤的量',
        ],
        '❤️ 爱': [
            '满满的爱意，温暖你的心田💕',
            '爱是世界上最美好的语言',
            '心有所属，便是幸福',
            '爱让生活变得更有意义'
        ],
        '💸 空空如也': [
            '向后转，齐步走',
            '天地无垠，惟余枯草，恰如我心',
            '尿上一处，也算人间'
        ],
        '🧧 红包': [
            '红包来喽～',
            '叫声爸爸！',
        ],
        '🔄 再转一次': [
            '大风车，吱呀吱呀呦滴转',
            '再再转一次',
        ],
        '🎁 随机礼物': [
            '转盘中的转盘，命运中的命运'
        ],
        '💬 陪聊服务': [
            '把大象装冰箱，拢共分几步？',
            '话说伊朗这个导弹……',
            '嘘！听说了吗？',
        ],
        '✨ 许愿一次': [
            '次数多了就成真的了！',
        ]
    }

    // 随机获取奖品说明
    const getRandomPrizeDescription = (prizeName) => {
        const descriptions = prizeDescriptions[prizeName]
        if (!descriptions || descriptions.length === 0) {
            return '恭喜获得奖品！'
        }
        if (Array.isArray(descriptions)) {
            const randomIndex = Math.floor(Math.random() * descriptions.length)
            return descriptions[randomIndex]
        }
        return descriptions
    }

    // 获取所有许愿
    const fetchWishes = async () => {
        try {
            const response = await fetch('/api/wishes')
            const data = await response.json()
            if (data.success) {
                setWishes(data.data)
            }
        } catch (error) {
            console.error('获取许愿列表失败:', error)
        }
    }

    // 创建许愿
    const createWish = async () => {
        if (!wishContent.trim()) {
            alert('请输入许愿内容')
            return
        }
        
        if (wishContent.length > 30) {
            alert('许愿内容不能超过30个字符')
            return
        }

        try {
            const response = await fetch('/api/wishes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: userName,
                    wishContent: wishContent.trim()
                })
            })
            
            const data = await response.json()
            if (data.success) {
                // 准备动画数据
                const newWish = {
                    id: data.data.id,
                    userId: data.data.userId,
                    wishContent: data.data.wishContent,
                    starX: data.data.starX,
                    starY: data.data.starY,
                    starSize: data.data.starSize,
                    createTime: data.data.createTime
                }
                
                // 设置动画状态
                setAnimatingWish(newWish)
                setShowWishAnimation(true)
                
                // 隐藏输入框但保持内容显示
                setShowWishInput(false)
                
                // 3秒后完成动画
                setTimeout(async () => {
                    setShowWishAnimation(false)
                    setAnimatingWish(null)
                    setWishContent('')
                    await fetchWishes() // 刷新许愿列表
                    await fetchUserInfo(userName) // 刷新用户信息（包含许愿次数）
                    
                    // 显示成功提示
                    const successMsg = document.createElement('div')
                    successMsg.textContent = '✨ 你的愿望已化作星光，在夜空中闪耀！'
                    successMsg.style.cssText = `
                        position: fixed;
                        top: 50%;
                        left: 50%;
                        transform: translate(-50%, -50%);
                        background: rgba(255, 215, 0, 0.95);
                        color: #333;
                        padding: 15px 25px;
                        border-radius: 25px;
                        font-size: 16px;
                        font-weight: 600;
                        z-index: 10000;
                        box-shadow: 0 8px 32px rgba(255, 215, 0, 0.5);
                        backdrop-filter: blur(10px);
                        animation: successFadeInOut 2s ease-in-out forwards;
                    `
                    document.body.appendChild(successMsg)
                    
                    // 让新创建的星星继续闪烁几次以突出显示
                    setTimeout(() => {
                        const newStar = document.querySelector(`[data-wish-id="${newWish.id}"]`)
                        if (newStar) {
                            newStar.style.animation = 'newStarHighlight 2s ease-in-out'
                        }
                    }, 100)
                    
                    // 2秒后移除提示
                    setTimeout(() => {
                        if (successMsg.parentNode) {
                            successMsg.parentNode.removeChild(successMsg)
                        }
                    }, 2000)
                }, 3000)
            } else {
                alert('许愿失败: ' + data.message)
            }
        } catch (error) {
            console.error('创建许愿失败:', error)
            alert('许愿失败，请稍后重试')
        }
    }

    // 点击星星显示许愿内容
    const handleStarClick = (wish, event) => {
        // 如果点击的是同一个星星，则关闭弹框
        if (selectedWish && selectedWish.id === wish.id) {
            setSelectedWish(null)
            return
        }
        
        // 获取点击位置相对于页面的坐标
        const rect = event.currentTarget.getBoundingClientRect()
        const wishWithPosition = {
            ...wish,
            clickX: rect.left + rect.width / 2,
            clickY: rect.top + rect.height / 2
        }
        setSelectedWish(wishWithPosition)
    }

    // 关闭许愿详情
    const closeWishDetail = () => {
        setSelectedWish(null)
    }

    // 使用useMemo生成固定的背景星星，只计算一次
    const backgroundStars = useMemo(() => {
        const stars = []
        for (let i = 0; i < 50; i++) {
            stars.push({
                id: i,
                left: Math.random() * 100,
                top: Math.random() * 100,
                animationDelay: Math.random() * 3,
                fontSize: 8 + Math.random() * 4
            })
        }
        return stars
    }, []) // 空依赖数组，只计算一次

    // 当用户名改变时，重新获取用户信息
    useEffect(() => {
        if (userName && !showWishPage) {
            fetchUserInfo(userName)
        }
    }, [userName, showWishPage])

    // 打开许愿页面时获取所有许愿
    useEffect(() => {
        if (showWishPage) {
            fetchWishes()
        }
    }, [showWishPage])

    // 获取奖品统计
    const fetchPrizeStats = async () => {
        if (!userName) return
        
        try {
            const response = await fetch(`/api/lottery/history/${userName}`)
            const result = await response.json()
            
            if (result.success) {
                // 统计每种奖品的获得次数
                const stats = {}
                result.data.forEach(record => {
                    const prizeName = record.prizeName
                    // 排除"空空如也"和"再转一次"
                    if (prizeName !== '💸 空空如也' && prizeName !== '🔄 再转一次') {
                        stats[prizeName] = (stats[prizeName] || 0) + 1
                    }
                })
                
                // 转换为数组格式，按获得次数排序
                const statsArray = Object.entries(stats)
                    .map(([name, count]) => ({ name, count }))
                    .sort((a, b) => b.count - a.count)
                
                setPrizeStats(statsArray)
            } else {
                console.error('获取奖品统计失败:', result.message)
                setPrizeStats([])
            }
        } catch (error) {
            console.error('获取奖品统计网络错误:', error)
            setPrizeStats([])
        }
    }
    const fetchUserInfo = async (userId) => {
        try {
            const response = await fetch(`/api/user/${userId}`)
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
        if (showWelcomeEffect || !welcomeEffectFinished) {
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
            const response = await fetch('/api/lottery', {
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
            // 检查是否抽到"爱"，如果是则显示爱心特效
            if (currentPrize === '❤️ 爱') {
                setShowLoveEffect(true)
                // 不自动隐藏，等待用户点击
            } else if (currentPrize === '✨ 许愿一次') {
                // 抽到许愿一次，显示特殊弹窗
                setResult(currentPrize)
            } else {
                setResult(currentPrize)
            }

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

            // 检查是否抽到"爱"，如果是则显示爱心特效
            if (prizeText === '❤️ 爱') {
                setShowLoveEffect(true)
                // 不自动隐藏，等待用户点击
            } else {
    setResult(prizeText)
            }

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

        // 先获取用户信息，判断用户是否存在
        const response = await fetch(`/api/user/${newUserName}`)
        const userData = await response.json()

        if (userData.data && userData.data.message === "用户不存在") {
            // 用户不存在，直接设置为特效已完成状态，并更新用户信息
            setWelcomeEffectFinished(true)
            setUserInfo(userData.data)
        } else {
            // 用户存在，显示欢迎特效
            setShowWelcomeEffect(true)
            setWelcomeEffectFinished(false)
            setUserInfo(userData.data)
        }
    }

    // 处理键盘回车
    const handleNameKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleNameConfirm()
        }
    }

    // 处理欢迎特效继续按钮
    const handleWelcomeContinue = () => {
        setShowWelcomeEffect(false)
        setWelcomeEffectFinished(true)
        // 用户信息已经在handleNameConfirm中获取，不需要重复获取
    }

    // 处理爱心特效继续
    const handleLoveContinue = () => {
        setShowLoveEffect(false)
        // 爱心特效结束后不需要额外操作
  }

  return (
    <div className="lucky-lottery-container">
            {/* 用户姓名输入模态框 */}
            {showNameInput && (
                <div className="name-input-modal">
                    <div className="name-input-content">
                        <h2 className="name-input-title">🎪 欢迎来到Eden抽奖</h2>
                        <p className="name-input-subtitle"><u>❤命运一旦来临，就必须接受❤</u></p>
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
      
      {/* 帮助按钮 - 右上角 */}
      {userName && (
          <button 
              className="help-button"
              onClick={() => {
                  fetchPrizeStats()
                  setShowPrizeStats(true)
              }}
              title="查看我的奖品"
          >
              ?
          </button>
      )}

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
                        
                        {/* 许愿入口按钮 - 用户姓名右侧 */}
                        <button 
                            className="wish-entrance-button-inline"
                            onClick={() => setShowWishPage(true)}
                            title={`进入许愿页面 ${userInfo && userInfo.wishCount > 0 ? `(${userInfo.wishCount}次许愿机会)` : '(暂无许愿机会)'}`}
                        >
                            <span className="wish-entrance-text">许愿</span>
                            {userInfo && userInfo.wishCount > 0 && (
                                <span className="wish-count-badge">{userInfo.wishCount}</span>
                            )}
                        </button>
                    </div>
                )}

                {/* 开始抽奖按钮 */}
        <button 
                    className={`spin-button ${isSpinning || !userName || !userInfo || showWelcomeEffect || !welcomeEffectFinished || userInfo.remainingDraws <= 0 ? 'disabled' : ''}`}
                    onClick={startSpin}
                    disabled={isSpinning || !userName || !userInfo || showWelcomeEffect || !welcomeEffectFinished || userInfo.remainingDraws <= 0}
                >
                    {isSpinning ? '🎯 转动中...' :
                        showWelcomeEffect ? '🎪 欢迎特效中...' :
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
                        <div className="result-description">
                            {getRandomPrizeDescription(result)}
                        </div>
                        {result === '✨ 许愿一次' ? (
                            <div className="wish-buttons">
                                <button
                                    className="wish-button"
                                    onClick={() => {
                                        setResult('')
                                        setCurrentPrize('')
                                        setShowWishPage(true)
                                    }}
                                >
                                    ✨ 许愿
                                </button>
                                <button
                                    className="give-up-button"
                                    onClick={() => {
                                        setResult('')
                                        setCurrentPrize('')
                                    }}
                                >
                                    以后
                                </button>
                            </div>
                        ) : (
            <button 
              className="continue-button"
                                onClick={() => {
                                    setResult('')
                                    setCurrentPrize('')
                                }}
            >
              继续游戏
            </button>
                        )}
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

            {/* 爱心特效 */}
            {showLoveEffect && (
                <div className="love-effect-overlay" onClick={handleLoveContinue}>
                    <div className="love-effect-container">
                        {/* 背景爱心雨 */}
                        <div className="love-rain">
                            <div className="love-drop love-drop-1">💕</div>
                            <div className="love-drop love-drop-2">💖</div>
                            <div className="love-drop love-drop-3">💗</div>
                            <div className="love-drop love-drop-4">💝</div>
                            <div className="love-drop love-drop-5">💞</div>
                            <div className="love-drop love-drop-6">💕</div>
                            <div className="love-drop love-drop-7">💖</div>
                            <div className="love-drop love-drop-8">💗</div>
                        </div>

                        {/* 中心立体爱心 */}
                        <div className="love-heart-container">
                            <div className="love-heart-3d">
                                <div className="css-heart">
                                    <div className="heart-left"></div>
                                    <div className="heart-right"></div>
                                </div>
                            </div>

                            {/* 爱心光环 */}
                            <div className="love-halo love-halo-1"></div>
                            <div className="love-halo love-halo-2"></div>
                            <div className="love-halo love-halo-3"></div>
                        </div>

                        {/* 爱心文字 */}
                        <div className="love-text">
                            <h1 className="love-title">亲爱的小猫咪</h1>
                            <p className="love-subtitle">✨ 你就是我的心脏 ✨</p>
                            <p className="love-continue-hint">点击继续 💕</p>
                        </div>
                    </div>
                </div>
            )}
            
            {/* 奖品统计弹窗 */}
            {showPrizeStats && (
                <div className="prize-stats-overlay">
                    <div className="prize-stats-modal">
                        <div className="prize-stats-header">
                            <h3>🏆 我的奖品</h3>
                            <button 
                                className="prize-stats-close"
                                onClick={() => setShowPrizeStats(false)}
                            >
                                ✕
                            </button>
                        </div>
                        <div className="prize-stats-content">
                            {prizeStats.length > 0 ? (
                                <div className="prize-stats-list">
                                    {prizeStats.map((stat, index) => (
                                        <div key={stat.name} className="prize-stat-item">
                                            <div className="prize-stat-rank">#{index + 1}</div>
                                            <div className="prize-stat-name">{stat.name}</div>
                                            <div className="prize-stat-count">×{stat.count}</div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="prize-stats-empty">
                                    <div className="empty-icon">🎁</div>
                                    <p>还没有获得任何奖品</p>
                                    <p className="empty-hint">快去转动转盘试试运气吧！</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {/* 许愿页面 */}
            {showWishPage && (
                <div className="wish-page-overlay">
                    <div className="wish-page-container">
                        {/* 夜空背景 */}
                        <div 
                            className="night-sky"
                            onClick={() => setSelectedWish(null)}
                        >
                            {/* 渲染所有许愿星星 */}
                            {wishes.map((wish) => (
                                <div
                                    key={wish.id}
                                    className={`wish-star wish-star-size-${wish.starSize}`}
                                    data-wish-id={wish.id}
                                    style={{
                                        left: `${wish.starX}%`,
                                        top: `${wish.starY}%`,
                                    }}
                                    onClick={(e) => {
                                        e.stopPropagation()
                                        handleStarClick(wish, e)
                                    }}
                                    title={`${wish.userId}的愿望`}
                                >
                                    ✨
                                </div>
                            ))}
                            
                            {/* 背景装饰星星 */}
                            <div className="background-stars">
                                {backgroundStars.map((star) => (
                                    <div
                                        key={star.id}
                                        className="bg-star"
                                        style={{
                                            left: `${star.left}%`,
                                            top: `${star.top}%`,
                                            animationDelay: `${star.animationDelay}s`,
                                            fontSize: `${star.fontSize}px`
                                        }}
                                    >
                                        ⭐
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* 许愿页面标题 */}
                        <div className="wish-page-title">
                            <h2>愿望星空</h2>
                            <p>每颗星星都承载着一个美好的愿望</p>
                        </div>

                        {/* 关闭按钮 */}
                        <button 
                            className="wish-close-button"
                            onClick={() => setShowWishPage(false)}
                        >
                            ✕
                        </button>

                        {/* 开始许愿按钮 */}
                        <button
                            className={`start-wish-button ${!userInfo || userInfo.wishCount <= 0 ? 'disabled' : ''}`}
                            onClick={() => {
                                if (!userInfo || userInfo.wishCount <= 0) {
                                    alert('您没有可用的许愿次数，请先抽中"许愿一次"奖品！')
                                } else {
                                    setShowWishInput(true)
                                }
                            }}
                        >
                            {userInfo && userInfo.wishCount > 0 ? `✨ 开始许愿 (${userInfo.wishCount}次)` : '✨ 暂无许愿机会'}
                        </button>

                        {/* 许愿变星星动画 */}
                        {showWishAnimation && animatingWish && (
                            <div className="wish-to-star-animation">
                                {/* 从输入框位置开始的文字 */}
                                <div className="animating-wish-text">
                                    {animatingWish.wishContent}
                                </div>
                                {/* 变成星星并飞到目标位置 */}
                                <div 
                                    className="animating-star"
                                    style={{
                                        '--target-x': `${animatingWish.starX}%`,
                                        '--target-y': `${animatingWish.starY}%`,
                                        '--star-size': `${animatingWish.starSize * 6 + 12}px`
                                    }}
                                >
                                    ⭐
                                </div>
                                {/* 魔法粒子效果 */}
                                <div className="magic-particles">
                                    {[...Array(8)].map((_, i) => (
                                        <div key={i} className={`particle particle-${i}`}>✨</div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* 许愿输入框 */}
                        {showWishInput && (
                            <div className="wish-input-modal">
                                <div className="wish-input-content">
                                    <h3>✨ 许下你的愿望 ✨</h3>
                                    <textarea
                                        className="wish-textarea"
                                        placeholder="请输入你的愿望... (最多30字)"
                                        value={wishContent}
                                        onChange={(e) => setWishContent(e.target.value)}
                                        maxLength={30}
                                    />
                                    <div className="wish-input-buttons">
                                        <button 
                                            className="wish-confirm-button"
                                            onClick={createWish}
                                        >
                                            许愿
                                        </button>
                                        <button 
                                            className="wish-cancel-button"
                                            onClick={() => {
                                                setShowWishInput(false)
                                                setWishContent('')
                                            }}
                                        >
                                            取消
                                        </button>
                                    </div>
                                    <div className="wish-char-count">
                                        {wishContent.length}/30
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* 许愿详情小弹框 */}
                        {selectedWish && (
                            <div 
                                className="wish-tooltip"
                                style={{
                                    position: 'fixed',
                                    left: `${selectedWish.clickX}px`,
                                    top: `${selectedWish.clickY}px`,
                                    transform: 'translate(-50%, calc(-100% - 30px))',
                                    zIndex: 400
                                }}
                                onClick={(e) => e.stopPropagation()}
                            >
                                <div className="wish-tooltip-content">
                                    <div className="wish-tooltip-header">
                                        <span className="wish-tooltip-user">✨ {selectedWish.userId}</span>
                                        <button 
                                            className="wish-tooltip-close"
                                            onClick={closeWishDetail}
                                        >
                                            ×
                                        </button>
                                    </div>
                                    <div className="wish-tooltip-text">
                                        {selectedWish.wishContent}
                                    </div>
                                    <div className="wish-tooltip-time">
                                        {new Date(selectedWish.createTime).toLocaleDateString()}
                                    </div>
                                </div>
                                {/* 指向星星的小三角 */}
                                <div className="wish-tooltip-arrow"></div>
                            </div>
                        )}
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

