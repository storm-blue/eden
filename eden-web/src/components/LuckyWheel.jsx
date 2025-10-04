import React, {useEffect, useMemo, useRef, useState} from 'react'
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
    const starCityAudioRef = useRef() // 星星城背景音乐引用
  const [isSpinning, setIsSpinning] = useState(false)
  const [result, setResult] = useState('')
    const [currentPrize, setCurrentPrize] = useState('') // 存储后端返回的奖品名称
    const [isMusicPlaying, setIsMusicPlaying] = useState(false) // 音乐播放状态
    const [userName, setUserName] = useState('') // 用户姓名
    const [showNameInput, setShowNameInput] = useState(true) // 是否显示姓名输入框
    const [tempName, setTempName] = useState('') // 临时存储输入的姓名
    const [userInfo, setUserInfo] = useState(null) // 用户信息（包含剩余抽奖次数）
    const [showWelcomeEffect, setShowWelcomeEffect] = useState(false) // 是否显示欢迎特效
    const [welcomeEffectFinished, setWelcomeEffectFinished] = useState(true) // 欢迎特效是否播放完成，默认为true
    const [showLoveEffect, setShowLoveEffect] = useState(false)
    const [showWishPage, setShowWishPage] = useState(false)
    const [showStarCity, setShowStarCity] = useState(false)
    const [starCityClosing, setStarCityClosing] = useState(false)
    const [isMobileDevice, setIsMobileDevice] = useState(false) // 是否为移动设备（需要强制横屏）
    const [starCityData, setStarCityData] = useState(null) // 星星城数据
    const [showDonationModal, setShowDonationModal] = useState(false) // 显示捐献弹窗
    const [userDonationPrizes, setUserDonationPrizes] = useState([]) // 用户可捐献的奖品
    const [donationEffect, setDonationEffect] = useState('') // 捐献效果提示
    const [showResidenceModal, setShowResidenceModal] = useState(false) // 显示居住选择弹窗
    const [selectedBuilding, setSelectedBuilding] = useState(null) // 选中的建筑
    const [buildingResidents, setBuildingResidents] = useState([]) // 建筑的居住人员
    const [loadingResidents, setLoadingResidents] = useState(false) // 加载居住人员状态 // 星星城关闭动画状态 // 星星城页面状态
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

    // 监听屏幕尺寸变化（仅在星星城页面时）
    useEffect(() => {
        if (!showStarCity) return

        const checkScreenSize = () => {
            // 移动端设备始终强制横屏显示
            const isMobile = window.innerWidth <= 768 || window.screen.width <= 768
            setIsMobileDevice(isMobile)
        }

        // 初始检查
        checkScreenSize()

        // 监听窗口大小变化
        window.addEventListener('resize', checkScreenSize)
        window.addEventListener('orientationchange', () => {
            setTimeout(checkScreenSize, 200)
        })

        return () => {
            window.removeEventListener('resize', checkScreenSize)
            window.removeEventListener('orientationchange', checkScreenSize)
        }
    }, [showStarCity])

    // 获取星星城数据
    const fetchStarCityData = async () => {
        try {
            const response = await fetch('/api/star-city/info')
            const data = await response.json()
            if (data.success) {
                console.log('获取星星城数据成功:', data.data)
                console.log('初始幸福度数据:', data.data.happiness)
                setStarCityData(data.data)
            } else {
                console.error('获取星星城数据失败:', data.message)
            }
        } catch (error) {
            console.error('获取星星城数据失败:', error)
        }
    }

    // 获取用户可捐献的奖品
    const fetchUserDonationPrizes = async (userId) => {
        try {
            const response = await fetch(`/api/star-city/donation-prizes/${userId}`)
            const data = await response.json()
            if (data.success) {
                setUserDonationPrizes(data.data || [])
            } else {
                console.error('获取可捐献奖品失败:', data.message)
                setUserDonationPrizes([])
            }
        } catch (error) {
            console.error('获取可捐献奖品失败:', error)
            setUserDonationPrizes([])
        }
    }

    // 处理捐献
    const handleDonation = async (prizeType) => {
        if (!userName) {
            alert('请先输入用户名')
            return
        }

        try {
            const response = await fetch('/api/star-city/donate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userName,
                    prizeType: prizeType
                })
            })

            const data = await response.json()
            if (data.success) {
                console.log('捐献成功，返回的数据:', data.data)

                // 处理两种可能的数据格式
                let updatedStarCityData;
                if (data.data.starCity) {
                    // 旧格式：数据在 starCity 嵌套对象中
                    console.log('使用旧格式数据结构')
                    updatedStarCityData = {
                        ...data.data.starCity,
                        populationFormatted: data.data.populationFormatted,
                        foodFormatted: data.data.foodFormatted,
                        // 如果有其他格式化字段也需要合并
                    }
                } else {
                    // 新格式：数据直接在 data.data 中
                    console.log('使用新格式数据结构')
                    updatedStarCityData = data.data
                }

                console.log('最终星星城数据:', updatedStarCityData)
                console.log('幸福度数据:', updatedStarCityData.happiness)

                // 根据奖品类型显示捐献效果消息
                let effectMessage = ''
                switch (prizeType) {
                    case '🍰 吃的～':
                        effectMessage = '食物 +1万'
        break
                    case '🥤 喝的～':
                        effectMessage = '食物 +0.5万，幸福 +1'
                        break
                    case '🎁 随机礼物':
                        effectMessage = '幸福 +2'
                        break
                    default:
                        effectMessage = '捐献成功'
                }

                // 显示捐献效果，持续2秒
                setDonationEffect(effectMessage)
                setTimeout(() => {
                    setDonationEffect('')
                }, 2000)

                // 更新星星城数据
                setStarCityData(updatedStarCityData)
                // 重新获取用户可捐献的奖品
                fetchUserDonationPrizes(userName)
            } else {
                alert(data.message)
            }
        } catch (error) {
            console.error('捐献失败:', error)
            alert('捐献失败，请稍后重试')
        }
    }

    // 打开城堡捐献弹窗
    const openDonationModal = () => {
        if (!userName) {
            alert('请先输入用户名')
            return
        }
        fetchUserDonationPrizes(userName)
        setShowDonationModal(true)
    }

    // 播放星星城背景音乐
    const playStarCityMusic = () => {
        if (starCityAudioRef.current && !isMusicPlaying) {
            starCityAudioRef.current.currentTime = 0
            starCityAudioRef.current.play().then(() => {
                setIsMusicPlaying(true)
                console.log('星星城背景音乐开始播放')
            }).catch(error => {
                console.log('背景音乐播放失败:', error)
            })
        }
    }

    // 停止星星城背景音乐
    const stopStarCityMusic = () => {
        if (starCityAudioRef.current && isMusicPlaying) {
            starCityAudioRef.current.pause()
            starCityAudioRef.current.currentTime = 0
            setIsMusicPlaying(false)
            console.log('星星城背景音乐已停止')
        }
    }

    // 建筑信息映射
    const buildingInfo = {
        castle: {name: '城堡', emoji: '🏰', key: 'castle'},
        city_hall: {name: '市政厅', emoji: '🏛️', key: 'city_hall'},
        palace: {name: '行宫', emoji: '🏯', key: 'palace'},
        dove_house: {name: '小白鸽家', emoji: '🕊️', key: 'dove_house'},
        park: {name: '公园', emoji: '🌳', key: 'park'}
    }

    // 处理建筑点击
    const handleBuildingClick = async (buildingType) => {
        if (!userName) {
            alert('请先输入用户名')
            return
        }

        setSelectedBuilding(buildingInfo[buildingType])
        setLoadingResidents(true)
        setBuildingResidents([])
        setShowResidenceModal(true)

        // 获取该建筑的居住人员
        try {
            const response = await fetch(`/api/residence/residents/${buildingType}`)
            const data = await response.json()

            if (data.success) {
                setBuildingResidents(data.data.residents || [])
            } else {
                console.error('获取居住人员失败:', data.message)
                setBuildingResidents([])
            }
        } catch (error) {
            console.error('获取居住人员失败:', error)
            setBuildingResidents([])
        } finally {
            setLoadingResidents(false)
        }
    }

    // 确认居住选择
    const confirmResidence = async () => {
        if (!selectedBuilding || !userName) {
            return
        }

        try {
            const response = await fetch('/api/residence/set', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userName,
                    residence: selectedBuilding.key
                })
            })

            const data = await response.json()
            if (data.success) {
                alert(data.data.message)
                // 重新获取用户信息以更新居住地点
                fetchUserInfo(userName)
            } else {
                alert(data.message)
            }
        } catch (error) {
            console.error('设置居住地点失败:', error)
            alert('设置居住地点失败，请稍后重试')
        }

        setShowResidenceModal(false)
        setSelectedBuilding(null)
    }

    // 关闭星星城并恢复屏幕方向的函数
    const closeStarCity = () => {
        setStarCityClosing(true)
        // 停止背景音乐
        stopStarCityMusic()

        // 500ms后完全关闭
        setTimeout(() => {
            setShowStarCity(false)
            setStarCityClosing(false)
        }, 500)
    }

    // 监听星星城页面状态，获取数据
    useEffect(() => {
        if (showStarCity) {
            fetchStarCityData()
            // 播放背景音乐
            setTimeout(() => {
                playStarCityMusic()
            }, 500) // 延迟500ms播放，确保页面已加载
        }
    }, [showStarCity])

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
                    .map(([name, count]) => ({name, count}))
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
            {/* 星星城背景音乐 */}
            <audio
                ref={starCityAudioRef}
                loop
                preload="auto"
                style={{display: 'none'}}
            >
                <source src="/audio/star-city-bg.mp3" type="audio/mpeg"/>
                <source src="/audio/star-city-bg.ogg" type="audio/ogg"/>
                您的浏览器不支持音频播放。
            </audio>

            {/* 星星城页面 */}
            {showStarCity && (
                <div
                    className={`star-city-container ${isMobileDevice && !starCityClosing ? 'force-landscape' : ''} ${starCityClosing ? 'closing' : ''}`}
                    style={{
                        backgroundImage: `url(/picture/lv${starCityData?.level || 1}.jpg)`,
                        zIndex: 99999,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white'
                    }}>
                    {/* 标题 */}
                    <h2 style={{
                        fontSize: '42px',
                        marginBottom: '10px',
                        textShadow: '0 0 25px rgba(0,0,0,0.8), 0 0 50px rgba(255,255,255,0.6)',
                        position: 'absolute',
                        bottom: '30px',
                        left: '50%',
                        transform: 'translateX(-50%)',
                        zIndex: 10,
                        color: 'white'
                    }}>
                        ✨星星城 LV{starCityData?.level || 1}✨
                    </h2>

                    {/* 城堡 - 中心白点 */}
                    <div
                        onClick={() => handleBuildingClick('castle')}
                        style={{
                            position: 'absolute',
                            top: '23%',
                            left: '48%',
                            transform: 'translate(-50%, -50%)',
                            width: '15px',
                            height: '15px',
                            borderRadius: '50%',
                            background: 'rgba(255, 255, 255, 0.9)',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            transition: 'all 0.3s ease',
                            backdropFilter: 'blur(5px)',
                            animation: 'castlePulse 3s ease-in-out infinite',
                            boxShadow: '0 4px 15px rgba(255, 255, 255, 0.3)',
                            zIndex: 12
                        }}
                        onMouseEnter={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1.2)'
                            e.target.style.background = 'rgba(255, 255, 255, 1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1)'
                            e.target.style.background = 'rgba(255, 255, 255, 0.9)'
                        }}
                        title="城堡 🏰 - 点击选择居住"
                    >
                    </div>

                    {/* 市政厅 - 左上方 */}
                    <div
                        onClick={() => handleBuildingClick('city_hall')}
                        style={{
                            position: 'absolute',
                            top: '12%',
                            left: '72%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px',
                            height: '12px',
                            borderRadius: '50%',
                            background: 'rgba(255, 255, 255, 0.8)',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            transition: 'all 0.3s ease',
                            backdropFilter: 'blur(5px)',
                            animation: 'castlePulse 3.5s ease-in-out infinite',
                            boxShadow: '0 3px 12px rgba(255, 255, 255, 0.25)',
                            zIndex: 11
                        }}
                        onMouseEnter={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1.3)'
                            e.target.style.background = 'rgba(255, 255, 255, 1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1)'
                            e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                        }}
                        title="市政厅 🏛️ - 点击选择居住"
                    >
                    </div>

                    {/* 行宫 - 右上方 */}
                    <div
                        onClick={() => handleBuildingClick('palace')}
                        style={{
                            position: 'absolute',
                            top: '8%',
                            left: '23%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px',
                            height: '12px',
                            borderRadius: '50%',
                            background: 'rgba(255, 255, 255, 0.8)',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            transition: 'all 0.3s ease',
                            backdropFilter: 'blur(5px)',
                            animation: 'castlePulse 4s ease-in-out infinite',
                            boxShadow: '0 3px 12px rgba(255, 255, 255, 0.25)',
                            zIndex: 11
                        }}
                        onMouseEnter={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1.3)'
                            e.target.style.background = 'rgba(255, 255, 255, 1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1)'
                            e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                        }}
                        title="行宫 🏯 - 点击选择居住"
                    >
                    </div>

                    {/* 小白鸽家 - 左下方 */}
                    <div
                        onClick={() => handleBuildingClick('dove_house')}
                        style={{
                            position: 'absolute',
                            top: '31%',
                            left: '61%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px',
                            height: '12px',
                            borderRadius: '50%',
                            background: 'rgba(255, 255, 255, 0.8)',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            transition: 'all 0.3s ease',
                            backdropFilter: 'blur(5px)',
                            animation: 'castlePulse 2.8s ease-in-out infinite',
                            boxShadow: '0 3px 12px rgba(255, 255, 255, 0.25)',
                            zIndex: 11
                        }}
                        onMouseEnter={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1.3)'
                            e.target.style.background = 'rgba(255, 255, 255, 1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1)'
                            e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                        }}
                        title="小白鸽家 🕊️ - 点击选择居住"
                    >
                    </div>

                    {/* 公园 - 右下方 */}
                    <div
                        onClick={() => handleBuildingClick('park')}
                        style={{
                            position: 'absolute',
                            top: '50%',
                            left: '40%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px',
                            height: '12px',
                            borderRadius: '50%',
                            background: 'rgba(255, 255, 255, 0.8)',
                            cursor: 'pointer',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            transition: 'all 0.3s ease',
                            backdropFilter: 'blur(5px)',
                            animation: 'castlePulse 3.2s ease-in-out infinite',
                            boxShadow: '0 3px 12px rgba(255, 255, 255, 0.25)',
                            zIndex: 11
                        }}
                        onMouseEnter={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1.3)'
                            e.target.style.background = 'rgba(255, 255, 255, 1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.transform = 'translate(-50%, -50%) scale(1)'
                            e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                        }}
                        title="公园 🌳 - 点击选择居住"
                    >
                    </div>

                    {/* 关闭按钮 */}
                    <button
                        className="star-city-close-btn"
                        style={{
                            position: 'absolute',
                            top: '30px',
                            right: '30px',
                            background: 'rgba(255, 255, 255, 0.3)',
                            color: 'white',
                            border: 'none',
                            width: '40px',
                            height: '40px',
                            borderRadius: '50%',
                            fontSize: '20px',
                            cursor: 'pointer',
                            backdropFilter: 'blur(10px)',
                            transition: 'all 0.3s ease',
                            boxShadow: '0 4px 15px rgba(0,0,0,0.2)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            lineHeight: '1',
                            fontFamily: 'Arial, sans-serif',
                            fontWeight: 'normal'
                        }}
                        onClick={() => closeStarCity()}
                        onMouseEnter={(e) => {
                            e.target.style.background = 'rgba(255, 255, 255, 0.5)'
                            e.target.style.transform = 'scale(1.1)'
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.background = 'rgba(255, 255, 255, 0.3)'
                            e.target.style.transform = 'scale(1)'
                        }}
                        title="返回愿望星空"
                    >
                        ✕
                    </button>

                    {/* 星星城数据显示 - 右下角 */}
                    {starCityData && (
                        <div
                            className="star-city-data"
                            onClick={openDonationModal}
                            style={{
                                position: 'absolute',
                                bottom: '30px',
                                right: '30px',
                                background: 'rgba(0, 0, 0, 0.7)',
                                color: 'white',
                                padding: '15px 20px',
                                borderRadius: '15px',
                                backdropFilter: 'blur(10px)',
                                border: '1px solid rgba(255, 255, 255, 0.2)',
                                minWidth: '200px',
                                textAlign: 'center',
                                boxShadow: '0 8px 32px rgba(0, 0, 0, 0.3)',
                                cursor: 'pointer',
                                transition: 'all 0.3s ease',
                                animation: 'cityDataPulse 3s ease-in-out infinite'
                            }}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.background = 'rgba(0, 0, 0, 0.8)'
                                e.currentTarget.style.transform = 'scale(1.05)'
                                e.currentTarget.style.animation = 'none'
                                e.currentTarget.style.boxShadow = '0 12px 40px rgba(0, 0, 0, 0.4), 0 0 0 8px rgba(255, 215, 0, 0.3)'
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.background = 'rgba(0, 0, 0, 0.7)'
                                e.currentTarget.style.transform = 'scale(1)'
                                e.currentTarget.style.animation = 'cityDataPulse 3s ease-in-out infinite'
                                e.currentTarget.style.boxShadow = '0 8px 32px rgba(0, 0, 0, 0.3)'
                            }}
                            title="点击进行城市捐献"
                        >
                            <div className="data-title" style={{
                                fontSize: '16px',
                                fontWeight: 'bold',
                                marginBottom: '10px',
                                color: '#FFD700',
                                textShadow: '0 0 10px rgba(255, 215, 0, 0.5)'
                            }}>
                                城市数据
                            </div>

                            <div style={{
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '8px',
                                fontSize: '14px'
                            }}>
                                <div className="data-item" style={{
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}>
                                    <span>👥 人口:</span>
                                    <span style={{color: '#87CEEB', fontWeight: 'bold'}}>
                    {starCityData.populationFormatted}
                  </span>
                                </div>

                                <div className="data-item" style={{
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}>
                                    <span>🍎 食物:</span>
                                    <span style={{color: '#90EE90', fontWeight: 'bold'}}>
                    {starCityData.foodFormatted}
                  </span>
                                </div>

                                <div className="data-item" style={{
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}>
                                    <span>😊 幸福:</span>
                                    <span style={{color: '#FFB6C1', fontWeight: 'bold'}}>
                    {starCityData.happiness !== undefined && starCityData.happiness !== null ? starCityData.happiness : '?'}
                  </span>
                                </div>
                            </div>

                            {starCityData.canUpgrade && starCityData.nextLevelRequirements && (
                                <div className="upgrade-info" style={{
                                    marginTop: '10px',
                                    padding: '8px',
                                    background: 'rgba(255, 215, 0, 0.2)',
                                    borderRadius: '8px',
                                    border: '1px solid rgba(255, 215, 0, 0.3)'
                                }}>
                                    <div style={{fontSize: '12px', color: '#FFD700', marginBottom: '4px'}}>
                                        🎯 升级条件 (LV{starCityData.level + 1}):
                                    </div>
                                    <div style={{fontSize: '11px', lineHeight: '1.3'}}>
                                        人口{starCityData.nextLevelRequirements.populationFormatted} |
                                        食物{starCityData.nextLevelRequirements.foodFormatted} |
                                        幸福{starCityData.nextLevelRequirements.happiness}
                                    </div>
                                </div>
                            )}
                        </div>
                    )}

                </div>
            )}

            {/* 捐献弹窗 */}
            {showDonationModal && (
                <div
                    className={`donation-modal-overlay ${isMobileDevice ? 'force-landscape' : ''}`}
                    style={{
                        position: 'fixed',
                        top: isMobileDevice ? '50%' : 0,
                        left: isMobileDevice ? '50%' : 0,
                        width: isMobileDevice ? '100vh' : '100vw',
                        height: isMobileDevice ? '100vw' : '100vh',
                        background: 'rgba(0, 0, 0, 0.8)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100000,
                        transform: isMobileDevice ? 'translate(-50%, -50%) rotate(90deg)' : 'none',
                        transformOrigin: 'center center'
                    }}>
                    <div className="donation-modal-content" style={{
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        borderRadius: '20px',
                        padding: isMobileDevice ? '20px' : '30px',
                        maxWidth: isMobileDevice ? '450px' : '700px',
                        width: '90%',
                        maxHeight: '80vh',
                        overflow: 'auto',
                        boxShadow: '0 20px 60px rgba(0, 0, 0, 0.3)',
                        border: '2px solid rgba(255, 255, 255, 0.2)',
                        fontSize: isMobileDevice ? '14px' : '16px'
                    }}>
                        {/* 标题 */}
                        <div style={{
                            textAlign: 'center',
                            marginBottom: '25px',
                            color: 'white',
                            position: 'relative'
                        }}>
                            <h3 style={{
                                fontSize: isMobileDevice ? '20px' : '24px',
                                margin: '0',
                                textShadow: '0 0 15px rgba(255, 255, 255, 0.5)',
                                display: 'inline-block'
                            }}>
                                城市捐献
                            </h3>
                            {/* 捐献效果提示 */}
                            {donationEffect && (
                                <div style={{
                                    position: 'absolute',
                                    right: isMobileDevice ? '-10px' : '0',
                                    top: '50%',
                                    transform: 'translateY(-50%)',
                                    background: 'linear-gradient(45deg, #FFD700, #FFA500)',
                                    color: '#333',
                                    padding: '6px 12px',
                                    borderRadius: '20px',
                                    fontSize: isMobileDevice ? '12px' : '14px',
                                    fontWeight: 'bold',
                                    whiteSpace: 'nowrap',
                                    boxShadow: '0 4px 15px rgba(255, 215, 0, 0.4)',
                                    animation: 'donationEffectPulse 0.5s ease-out',
                                    zIndex: 10
                                }}>
                                    ✨ {donationEffect}
                                </div>
                            )}
                            <p style={{
                                fontSize: isMobileDevice ? '12px' : '14px',
                                margin: '10px 0 0 0',
                                opacity: 0.9
                            }}>
                                为星星城的发展贡献您的奖品！
                            </p>
                        </div>

                        {/* 主要内容区域 - 响应式布局 */}
                        <div style={{
                            display: 'flex',
                            flexDirection: isMobileDevice ? 'row' : 'column',
                            gap: '20px',
                            marginBottom: '20px'
                        }}>
                            {/* 左侧/上方：捐献效果说明 */}
                            <div style={{
                                flex: isMobileDevice ? '1' : 'none',
                                background: 'rgba(255, 255, 255, 0.1)',
                                borderRadius: '15px',
                                padding: '15px',
                                color: 'white',
                                fontSize: isMobileDevice ? '12px' : '13px',
                                lineHeight: '1.5'
                            }}>
                                <div style={{fontWeight: 'bold', marginBottom: '8px', color: '#FFD700'}}>
                                    🎁 捐献效果：
                                </div>
                                <div>🍰 吃的～ → +1万食物</div>
                                <div>🥤 喝的～ → +0.5万食物 +1幸福</div>
                                <div>🎁 随机礼物 → +2幸福</div>
                            </div>

                            {/* 右侧/下方：可捐献的奖品列表 */}
                            <div style={{
                                flex: isMobileDevice ? '1' : 'none'
                            }}>

                                {userDonationPrizes.length > 0 ? (
                                    <div style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        gap: '6px'
                                    }}>
                                        {userDonationPrizes.map((prize, index) => (
                                            <div
                                                key={index}
                                                onClick={() => handleDonation(prize.name)}
                                                style={{
                                                    background: 'rgba(255, 255, 255, 0.15)',
                                                    borderRadius: '6px',
                                                    padding: '8px 10px',
                                                    cursor: 'pointer',
                                                    transition: 'all 0.3s ease',
                                                    border: '1px solid rgba(255, 255, 255, 0.2)',
                                                    display: 'flex',
                                                    justifyContent: 'space-between',
                                                    alignItems: 'center',
                                                    height: '32px'
                                                }}
                                                onMouseEnter={(e) => {
                                                    e.target.style.background = 'rgba(255, 255, 255, 0.25)'
                                                    e.target.style.transform = 'scale(1.02)'
                                                }}
                                                onMouseLeave={(e) => {
                                                    e.target.style.background = 'rgba(255, 255, 255, 0.15)'
                                                    e.target.style.transform = 'scale(1)'
                                                }}
                                            >
                                                <div style={{
                                                    color: 'white',
                                                    fontSize: isMobileDevice ? '14px' : '16px',
                                                    fontWeight: 'bold'
                                                }}>
                                                    {prize.name === '🍰 吃的～' && '🍽️'}
                                                    {prize.name === '🥤 喝的～' && '🥤'}
                                                    {prize.name === '🎁 随机礼物' && '🎁'}
                                                    {' ' + prize.name}
                                                </div>
                                                <div style={{
                                                    background: 'rgba(255, 215, 0, 0.8)',
                                                    color: '#333',
                                                    borderRadius: '12px',
                                                    padding: '3px 8px',
                                                    fontSize: isMobileDevice ? '11px' : '12px',
                                                    fontWeight: 'bold'
                                                }}>
                                                    x{prize.count}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                ) : (
                                    <div style={{
                                        textAlign: 'center',
                                        color: 'rgba(255, 255, 255, 0.7)',
                                        fontSize: isMobileDevice ? '14px' : '16px',
                                        padding: '30px',
                                        background: 'rgba(255, 255, 255, 0.1)',
                                        borderRadius: '15px'
                                    }}>
                                        <div>您没有可捐献的奖品</div>
                                        <div style={{fontSize: isMobileDevice ? '12px' : '14px', marginTop: '8px'}}>
                                            快去转转盘吧！
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* 关闭按钮 */}
                        <div style={{textAlign: 'center'}}>
                            <button
                                onClick={() => setShowDonationModal(false)}
                                style={{
                                    background: 'rgba(255, 255, 255, 0.2)',
                                    color: 'white',
                                    borderRadius: '25px',
                                    padding: isMobileDevice ? '10px 25px' : '12px 30px',
                                    fontSize: isMobileDevice ? '14px' : '16px',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    backdropFilter: 'blur(10px)',
                                    border: '1px solid rgba(255, 255, 255, 0.3)'
                                }}
                                onMouseEnter={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.3)'
                                    e.target.style.transform = 'scale(1.05)'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.2)'
                                    e.target.style.transform = 'scale(1)'
                                }}
                            >
                                关闭
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* 居住选择弹窗 */}
            {showResidenceModal && selectedBuilding && (
                <div
                    className={`residence-modal-overlay ${isMobileDevice ? 'force-landscape' : ''}`}
                    style={{
                        position: 'fixed',
                        top: isMobileDevice ? '50%' : 0,
                        left: isMobileDevice ? '50%' : 0,
                        width: isMobileDevice ? '100vh' : '100vw',
                        height: isMobileDevice ? '100vw' : '100vh',
                        background: 'rgba(0, 0, 0, 0.8)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100000,
                        transform: isMobileDevice ? 'translate(-50%, -50%) rotate(90deg)' : 'none',
                        transformOrigin: 'center center'
                    }}>
                    <div className="residence-modal-content" style={{
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        borderRadius: '20px',
                        padding: '30px',
                        maxWidth: isMobileDevice ? '450px' : '400px',
                        width: '90%',
                        textAlign: 'center',
                        boxShadow: '0 20px 60px rgba(0, 0, 0, 0.3)',
                        border: '2px solid rgba(255, 255, 255, 0.2)',
                        color: 'white',
                        fontSize: isMobileDevice ? '14px' : '16px'
                    }}>
                        {/* 建筑信息 */}
                        <div style={{
                            background: 'rgba(255, 255, 255, 0.1)',
                            borderRadius: '15px',
                            padding: '20px',
                            marginBottom: '25px'
                        }}>
                            <div style={{
                                fontSize: '48px',
                                marginBottom: '10px'
                            }}>
                                {selectedBuilding.emoji}
                            </div>
                            <div style={{
                                fontSize: '20px',
                                fontWeight: 'bold',
                                marginBottom: '8px'
                            }}>
                                {selectedBuilding.name}
                            </div>

                            {/* 当前居住人员 */}
                            <div style={{
                                fontSize: '14px',
                                opacity: 0.9,
                                marginBottom: '15px'
                            }}>
                                {loadingResidents ? (
                                    <div style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        gap: '8px'
                                    }}>
                                        <div style={{
                                            width: '16px',
                                            height: '16px',
                                            border: '2px solid rgba(255,255,255,0.3)',
                                            borderTop: '2px solid white',
                                            borderRadius: '50%',
                                            animation: 'spin 1s linear infinite'
                                        }}></div>
                                        正在获取居住人员...
                                    </div>
                                ) : (
                                    <>
                                        <div style={{marginBottom: '8px'}}>
                                            当前居住人员：{buildingResidents.length} 人
                                        </div>
                                        {buildingResidents.length > 0 ? (
                                            <div style={{
                                                background: 'rgba(0, 0, 0, 0.2)',
                                                borderRadius: '8px',
                                                padding: '8px',
                                                fontSize: '12px',
                                                wordBreak: 'break-all',
                                                lineHeight: '1.4'
                                            }}>
                                                👤 {buildingResidents.map(resident => resident.userId).join(', ')}
                                            </div>
                                        ) : (
                                            <div style={{
                                                color: 'rgba(255, 255, 255, 0.6)',
                                                fontSize: '12px'
                                            }}>
                                                暂无居住人员
                                            </div>
                                        )}
                                    </>
                                )}
                            </div>

                            <div style={{
                                fontSize: '14px',
                                opacity: 0.9
                            }}>
                                您确定要在这里居住吗？
                            </div>
                        </div>

                        {/* 按钮区域 */}
                        <div style={{
                            display: 'flex',
                            gap: '15px',
                            justifyContent: 'center'
                        }}>
                            <button
                                onClick={confirmResidence}
                                style={{
                                    background: 'rgba(255, 255, 255, 0.2)',
                                    color: 'white',
                                    borderRadius: '25px',
                                    padding: '12px 25px',
                                    fontSize: '16px',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    backdropFilter: 'blur(10px)',
                                    border: '1px solid rgba(255, 255, 255, 0.3)',
                                    fontWeight: 'bold'
                                }}
                                onMouseEnter={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.3)'
                                    e.target.style.transform = 'scale(1.05)'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.2)'
                                    e.target.style.transform = 'scale(1)'
                                }}
                            >
                                确认居住
                            </button>
                            <button
                                onClick={() => {
                                    setShowResidenceModal(false)
                                    setSelectedBuilding(null)
                                }}
                                style={{
                                    background: 'rgba(255, 255, 255, 0.1)',
                                    color: 'rgba(255, 255, 255, 0.8)',
                                    borderRadius: '25px',
                                    padding: '12px 25px',
                                    fontSize: '16px',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    backdropFilter: 'blur(10px)',
                                    border: '1px solid rgba(255, 255, 255, 0.2)'
                                }}
                                onMouseEnter={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.2)'
                                    e.target.style.color = 'white'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.1)'
                                    e.target.style.color = 'rgba(255, 255, 255, 0.8)'
                                }}
                            >
                                取消
                            </button>
                        </div>
                    </div>
                </div>
            )}
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

                        {/* 许愿入口按钮 - 用户姓名右侧，只对存在的用户显示 */}
                        {userInfo && userInfo.message !== "用户不存在" && (
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
                        )}
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

                        {/* 进入星星城按钮 */}
                        <button
                            className="star-city-entrance-button"
                            onClick={(e) => {
                                e.stopPropagation()
                                console.log('星星城按钮被点击了！')
                                console.log('当前 showStarCity 状态:', showStarCity)
                                setShowStarCity(true)
                                console.log('设置 showStarCity 为 true')
                            }}
                            title="进入星星城"
                        >
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

