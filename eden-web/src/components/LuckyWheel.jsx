import React, {useEffect, useMemo, useRef, useState} from 'react'
import {LuckyWheel} from '@lucky-canvas/react'
import './LuckyWheel.css'
import AvatarCrop from './AvatarCrop'

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

    // 星星城背景音乐（简化为单个文件）
    const starCityMusicUrl = '/audio/star-city-bg.mp3'
    const [userName, setUserName] = useState(() => {
        // 从localStorage读取保存的用户名
        return localStorage.getItem('eden_userName') || ''
    }) // 用户姓名
    const [showNameInput, setShowNameInput] = useState(() => {
        // 如果localStorage中有用户名，则不显示输入框
        return !localStorage.getItem('eden_userName')
    }) // 是否显示姓名输入框
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
    const [loadingResidents, setLoadingResidents] = useState(false) // 加载居住人员状态
    const [allBuildingResidents, setAllBuildingResidents] = useState({}) // 所有建筑的居住人员
    const [specialCombos, setSpecialCombos] = useState(null) // 特殊居住组合状态 // 星星城关闭动画状态 // 星星城页面状态
    const [showEventHistory, setShowEventHistory] = useState(false) // 显示事件历史弹窗
    const [eventHistory, setEventHistory] = useState([]) // 事件历史数据
    const [loadingEventHistory, setLoadingEventHistory] = useState(false) // 加载事件历史状态
    const [wishes, setWishes] = useState([]) // 所有许愿列表
    const [showWishInput, setShowWishInput] = useState(false) // 是否显示许愿输入框
    const [wishContent, setWishContent] = useState('') // 许愿内容
    const [selectedWish, setSelectedWish] = useState(null) // 选中的许愿
    const [showWishAnimation, setShowWishAnimation] = useState(false) // 是否显示许愿变星星动画
    const [animatingWish, setAnimatingWish] = useState(null) // 正在动画的许愿数据
    const [showPrizeStats, setShowPrizeStats] = useState(false) // 是否显示奖品统计
    const [prizeStats, setPrizeStats] = useState([]) // 奖品统计数据
    const [showAvatarCrop, setShowAvatarCrop] = useState(false) // 是否显示头像裁剪弹窗
    const [userAvatar, setUserAvatar] = useState(null) // 用户头像路径
    const [userAvatars, setUserAvatars] = useState({}) // 缓存所有用户头像 {userId: avatarPath}

    // 居民头像详情弹框状态
    const [showResidentDetail, setShowResidentDetail] = useState(false)
    const [selectedResident, setSelectedResident] = useState(null)
    const [residentDetailInfo, setResidentDetailInfo] = useState(null)
    const [loadingResidentDetail, setLoadingResidentDetail] = useState(false)

    // 耐力不足提示弹框
    const [showNoStaminaModal, setShowNoStaminaModal] = useState(false)

    // 用户头像预览弹框状态
    const [showAvatarPreview, setShowAvatarPreview] = useState(false)

    // 居所事件状态
    const [residenceEvents, setResidenceEvents] = useState({})

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
        // 🔥 CPU优化：移动端大幅减少星星数量
        const starCount = isMobileDevice ? 16 : 16 // 从50减少到8/20
        for (let i = 0; i < starCount; i++) {
            stars.push({
                id: i,
                left: Math.random() * 100,
                top: Math.random() * 100,
                animationDelay: Math.random() * 3,
                fontSize: 8 + Math.random() * 4
            })
        }
        return stars
    }, [isMobileDevice]) // 添加依赖，设备类型变化时重新计算

    // 监听屏幕尺寸变化（仅在星星城页面时）
    useEffect(() => {
        if (!showStarCity) return

        // 🔥 CPU优化：防抖函数，减少频繁调用
        let resizeTimer
        const checkScreenSize = () => {
            clearTimeout(resizeTimer)
            resizeTimer = setTimeout(() => {
                // 移动端设备始终强制横屏显示
                const isMobile = window.innerWidth <= 768 || window.screen.width <= 768
                setIsMobileDevice(isMobile)
            }, 150) // 150ms防抖
        }

        // 初始检查
        checkScreenSize()

        // 🔥 CPU优化：使用passive监听器提升性能
        const options = {passive: true}
        window.addEventListener('resize', checkScreenSize, options)
        window.addEventListener('orientationchange', () => {
            setTimeout(checkScreenSize, 200)
        }, options)

        return () => {
            clearTimeout(resizeTimer)
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

    // 获取特殊居住组合状态
    const fetchSpecialCombos = async () => {
        try {
            const response = await fetch('/api/star-city/special-combos')
            const data = await response.json()
            if (data.success) {
                console.log('获取特殊居住组合状态成功:', data.data)
                setSpecialCombos(data.data)
            } else {
                console.error('获取特殊居住组合状态失败:', data.message)
            }
        } catch (error) {
            console.error('获取特殊居住组合状态出错:', error)
        }
    }

    // 获取居所事件
    const fetchResidenceEvent = async (residence) => {
        try {
            const response = await fetch(`/api/residence-events/${residence}`)
            const result = await response.json()

            if (result.success) {
                return result.data
            } else {
                console.error('获取居所事件失败:', result.message)
                return null
            }
        } catch (error) {
            console.error('获取居所事件网络错误:', error)
            return null
        }
    }

    // 加载所有居所的事件
    const loadAllResidenceEvents = async () => {
        const residences = ['castle', 'city_hall', 'palace', 'white_dove_house', 'park']
        const events = {}

        try {
            await Promise.all(residences.map(async (residence) => {
                const eventData = await fetchResidenceEvent(residence)
                if (eventData) {
                    events[residence] = eventData
                }
            }))

            setResidenceEvents(events)
            console.log('所有居所事件加载完成:', events)
        } catch (error) {
            console.error('加载居所事件失败:', error)
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

    // 播放星星城背景音乐（彻底修复双重下载）
    const playStarCityMusic = () => {
        if (starCityAudioRef.current && !isMusicPlaying) {
            // 🔥 彻底修复：只在首次播放时设置src，确保只下载一次
            if (!starCityAudioRef.current.src) {
                console.log('首次设置音频源:', starCityMusicUrl)
                starCityAudioRef.current.src = starCityMusicUrl
            }
            starCityAudioRef.current.currentTime = 0
            starCityAudioRef.current.loop = true // 循环播放
            starCityAudioRef.current.play().then(() => {
                setIsMusicPlaying(true)
                console.log('星星城背景音乐开始播放:', starCityMusicUrl)
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

    // 音乐结束时的处理函数（简化版 - 循环播放不需要）
    const handleMusicEnded = () => {
        // 🔥 简化：由于设置了loop=true，这个函数基本不会被调用
        console.log('音乐意外结束，重新播放')
        if (isMusicPlaying) {
            playStarCityMusic()
        }
    }

    // 建筑信息映射
    const buildingInfo = {
        castle: {name: '城堡', emoji: '🏰', key: 'castle'},
        city_hall: {name: '市政厅', emoji: '🏛️', key: 'city_hall'},
        palace: {name: '行宫', emoji: '🏯', key: 'palace'},
        white_dove_house: {name: '小白鸽家', emoji: '🕊️', key: 'white_dove_house'},
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

    // 加载所有建筑的居住人员信息
    const loadAllBuildingResidents = async () => {
        const buildings = ['castle', 'city_hall', 'palace', 'white_dove_house', 'park']
        const residentsData = {}
        const allUserIds = []

        try {
            for (const building of buildings) {
                const response = await fetch(`/api/residence/residents/${building}`)
                const data = await response.json()

                if (data.success) {
                    residentsData[building] = data.data.residents || []
                    // 收集所有用户ID用于批量获取头像
                    const userIds = (data.data.residents || []).map(resident => resident.userId)
                    allUserIds.push(...userIds)
                } else {
                    residentsData[building] = []
                }
            }
            setAllBuildingResidents(residentsData)

            // 批量获取所有居民的头像
            if (allUserIds.length > 0) {
                await fetchMultipleUserAvatars([...new Set(allUserIds)]) // 去重
            }

        } catch (error) {
            console.error('加载建筑居住人员失败:', error)
        }
    }

    // 检查是否是特殊情侣组合
    const isSpecialCouple = (residents) => {
        if (residents.length === 2) {
            // 两人组合：李星斗 + 秦小淮
            const names = residents.map(r => r.userId).sort()
            return names.includes('李星斗') && names.includes('秦小淮')
        } else if (residents.length === 3) {
            // 三人组合：秦小淮 + 李星斗 + 存子
            const names = residents.map(r => r.userId).sort()
            return names.includes('秦小淮') && names.includes('李星斗') && names.includes('存子')
        }
        return false
    }

    // 检查是否是危险居住组合（秦小淮要住进只有李星斗的地方）
    const isDangerousResidence = (residents, currentUser) => {
        if (!currentUser || !residents) return false

        // 如果当前用户是秦小淮，且居所只有李星斗一个人
        if (currentUser === '秦小淮' && residents.length === 1) {
            return residents[0].userId === '李星斗'
        }

        return false
    }

    // 获取特殊组合的显示文字
    const getSpecialCoupleText = (residents) => {
        if (residents.length === 2) {
            return "💕 秦小淮和李星斗正在爱爱 💕 \n💕 她被日得胡言乱语了～ 💕"
        } else if (residents.length === 3) {
            return "💕 秦小淮、李星斗和存子正在疯狂爱爱 💕"
        }
        return ""
    }

    // 检查当前用户是否已经在该居所中
    const isUserAlreadyInResidence = (residents, currentUser) => {
        if (!residents || !currentUser) return false
        return residents.some(resident => resident.userId === currentUser)
    }

    // 刷新当前居所事件
    const refreshCurrentResidenceEvents = async () => {
        if (!selectedBuilding || !userName) {
            return
        }

        try {
            console.log(`正在刷新 ${selectedBuilding.name} 的事件...`)

            // 调用后端刷新单个居所事件接口，传递userId
            const response = await fetch(`/api/residence-events/refresh/${selectedBuilding.key}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userName
                })
            })

            const data = await response.json()
            if (data.success) {
                console.log(`${selectedBuilding.name} 事件刷新成功，剩余耐力: ${data.stamina}/5`)

                // 重新加载当前居所的事件
                const eventResponse = await fetch(`/api/residence-events/${selectedBuilding.key}`)
                const eventData = await eventResponse.json()

                if (eventData.success) {
                    setResidenceEvents(prev => ({
                        ...prev,
                        [selectedBuilding.key]: eventData.data
                    }))
                    console.log(`${selectedBuilding.name} 事件已更新`)
                }

                // 不弹窗，只在控制台记录
            } else {
                // 如果是耐力不足的提示，显示弹框
                console.error('刷新事件失败:', data.message)
                if (data.message === '你已经精疲力尽了，歇会吧') {
                    setShowNoStaminaModal(true)
                } else {
                    // 其他错误用alert
                    alert(data.message)
                }
            }
        } catch (error) {
            console.error('刷新事件失败:', error)
            alert('刷新事件失败，请稍后重试')
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
                // 重新获取用户信息以更新居住地点
                fetchUserInfo(userName)
                // 刷新所有建筑的居住人员信息以更新爱心显示状态
                loadAllBuildingResidents()
                // 刷新特殊居住组合状态
                fetchSpecialCombos()
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

    // 获取事件历史
    const fetchEventHistory = async (residence) => {
        if (!residence) return

        setLoadingEventHistory(true)
        try {
            const response = await fetch(`/api/residence-event-history/${residence}`)
            if (response.ok) {
                const data = await response.json()
                if (data.success) {
                    setEventHistory(data.history || [])
                } else {
                    console.error('获取事件历史失败:', data.message)
                    setEventHistory([])
                }
            } else {
                console.error('获取事件历史失败:', response.statusText)
                setEventHistory([])
            }
        } catch (error) {
            console.error('获取事件历史时发生错误:', error)
            setEventHistory([])
        } finally {
            setLoadingEventHistory(false)
        }
    }

    // 显示事件历史弹窗
    const showResidenceEventHistory = () => {
        if (selectedBuilding) {
            fetchEventHistory(selectedBuilding.key)
            setShowEventHistory(true)
        }
    }

    // 格式化历史时间
    const formatHistoryTime = (createdAt) => {
        if (!createdAt) return ''
        const date = new Date(createdAt)
        return date.toLocaleString('zh-CN', {
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    // 解析事件数据
    const parseEventData = (eventData) => {
        try {
            return JSON.parse(eventData)
        } catch (error) {
            return []
        }
    }

    // 监听星星城页面状态，获取数据
    useEffect(() => {
        if (showStarCity) {
            fetchStarCityData()
            fetchSpecialCombos() // 获取特殊居住组合状态
            loadAllBuildingResidents() // 加载所有建筑的居住人员信息
            loadAllResidenceEvents() // 加载所有居所事件

            // 🔥 修复双重下载：移除独立的预加载，直接播放
            // 音频会在首次播放时自动加载
            const audioDelay = isMobileDevice ? 2000 : 1000 // 移动端延迟更久
            setTimeout(() => {
                playStarCityMusic()
            }, audioDelay)
        }
    }, [showStarCity])

    // 页面加载时自动获取已保存用户的信息
    useEffect(() => {
        const savedUserName = localStorage.getItem('eden_userName')
        if (savedUserName && savedUserName.trim()) {
            // 如果有保存的用户名，自动获取用户信息
            console.log('自动加载保存的用户:', savedUserName)
            fetchUserInfo(savedUserName)
            fetchUserAvatar(savedUserName)
        }
    }, [])

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

                // 同时获取用户头像信息
                fetchUserAvatar(userId)
            } else {
                console.error('获取用户信息失败:', result.message)
            }
        } catch (error) {
            console.error('获取用户信息网络错误:', error)
        }
    }

    // 获取用户头像信息
    const fetchUserAvatar = async (userId) => {
        try {
            const response = await fetch(`/api/avatar/${userId}`)
            const result = await response.json()

            if (result.success && result.data.avatarPath) {
                // 后端返回相对路径，前端拼接完整地址
                const fullAvatarUrl = result.data.avatarPath.startsWith('http')
                    ? result.data.avatarPath
                    : window.location.origin + result.data.avatarPath
                setUserAvatar(fullAvatarUrl)
                console.log('获取用户头像成功:', result.data)
            } else {
                setUserAvatar(null)
                console.log('用户暂无头像')
            }
        } catch (error) {
            console.error('获取用户头像网络错误:', error)
            setUserAvatar(null)
        }
    }

    // 批量获取多个用户的头像
    const fetchMultipleUserAvatars = async (userIds) => {
        const newAvatars = {}

        // 过滤出还没有缓存的用户ID
        const uncachedUserIds = userIds.filter(userId => !userAvatars[userId])

        if (uncachedUserIds.length === 0) {
            return // 所有头像都已缓存
        }

        try {
            // 并发获取所有未缓存的用户头像
            const promises = uncachedUserIds.map(async (userId) => {
                try {
                    const response = await fetch(`/api/avatar/${userId}`)
                    const result = await response.json()

                    if (result.success && result.data.avatarPath) {
                        // 后端返回相对路径，前端拼接完整地址
                        const fullAvatarUrl = result.data.avatarPath.startsWith('http')
                            ? result.data.avatarPath
                            : window.location.origin + result.data.avatarPath
                        newAvatars[userId] = fullAvatarUrl
                    } else {
                        newAvatars[userId] = null // 用户暂无头像
                    }
                } catch (error) {
                    console.error(`获取用户 ${userId} 头像失败:`, error)
                    newAvatars[userId] = null
                }
            })

            await Promise.all(promises)

            // 更新头像缓存
            setUserAvatars(prev => ({...prev, ...newAvatars}))

        } catch (error) {
            console.error('批量获取用户头像失败:', error)
        }
    }

    // 头像上传成功回调
    const handleAvatarSave = (avatarPath) => {
        // 后端返回相对路径，前端拼接完整地址
        const fullAvatarUrl = avatarPath.startsWith('http')
            ? avatarPath
            : window.location.origin + avatarPath
        setUserAvatar(fullAvatarUrl)
        console.log('头像上传成功:', avatarPath)
    }

    // 打开头像裁剪弹窗或预览弹窗
    const handleAvatarClick = () => {
        if (userName && userInfo && userInfo.message !== '用户不存在') {
            // 如果用户已有头像，先显示预览弹框
            if (userAvatar) {
                setShowAvatarPreview(true)
            } else {
                // 如果没有头像，直接打开裁剪弹窗
                setShowAvatarCrop(true)
            }
        } else {
            if (!userName) {
                alert('请先输入用户姓名！')
            } else if (!userInfo) {
                alert('正在获取用户信息，请稍后再试...')
            } else if (userInfo.message === '用户不存在') {
                alert('用户不存在，无法上传头像。请联系管理员添加用户。')
            }
        }
    }

    // 关闭头像预览弹框
    const closeAvatarPreview = () => {
        setShowAvatarPreview(false)
    }

    // 从预览弹框打开裁剪弹窗
    const openAvatarCropFromPreview = () => {
        setShowAvatarPreview(false)
        setShowAvatarCrop(true)
    }

    // 处理居民头像点击
    const handleResidentAvatarClick = async (userId, avatarPath) => {
        console.log('点击居民头像:', userId, avatarPath)
        setSelectedResident({
            userId: userId,
            avatarPath: avatarPath
        })
        setShowResidentDetail(true)

        // 获取用户详细信息
        await fetchResidentDetailInfo(userId)
    }

    // 获取居民详细信息
    const fetchResidentDetailInfo = async (userId) => {
        if (!userId) return

        setLoadingResidentDetail(true)
        try {
            const response = await fetch(`/api/user-info/${userId}`)
            if (response.ok) {
                const data = await response.json()
                if (data.success) {
                    setResidentDetailInfo(data.userInfo)
                } else {
                    console.error('获取用户详细信息失败:', data.message)
                    setResidentDetailInfo(null)
                }
            } else {
                console.error('获取用户详细信息失败:', response.statusText)
                setResidentDetailInfo(null)
            }
        } catch (error) {
            console.error('获取用户详细信息时发生错误:', error)
            setResidentDetailInfo(null)
        } finally {
            setLoadingResidentDetail(false)
        }
    }

    // 关闭居民详情弹框
    const closeResidentDetail = () => {
        setShowResidentDetail(false)
        setSelectedResident(null)
        setResidentDetailInfo(null)
        setLoadingResidentDetail(false)
    }

    // 根据状态获取颜色配置
    const getStatusStyle = (status) => {
        const statusColors = {
            '安居乐业中': {
                background: 'linear-gradient(135deg, #4CAF50, #45a049)',
                boxShadow: '0 2px 8px rgba(76, 175, 80, 0.3)'
            },
            '忙碌中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '沉吟中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '装酷中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '思考中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '工作中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '学习中': {
                background: 'linear-gradient(135deg, #2196F3, #1976D2)',
                boxShadow: '0 2px 8px rgba(33, 150, 243, 0.3)'
            },
            '幻想中': {
                background: 'linear-gradient(135deg, #E91E63, #C2185B)',
                boxShadow: '0 2px 8px rgba(233, 30, 99, 0.3)'
            },
            '兽性大发中': {
                background: 'linear-gradient(135deg, #E91E63, #C2185B)',
                boxShadow: '0 2px 8px rgba(233, 30, 99, 0.3)'
            },
            '发情中': {
                background: 'linear-gradient(135deg, #E91E63, #C2185B)',
                boxShadow: '0 2px 8px rgba(233, 30, 99, 0.3)'
            },
        }

        // 如果找不到对应状态，使用默认的绿色
        return statusColors[status] || statusColors['安居乐业中']
    }

    // 渲染居民头像列表
    const renderResidentAvatars = (buildingType, residents) => {
        if (!residents || residents.length === 0) {
            return null
        }

        // 根据建筑类型确定位置
        const buildingPositions = {
            castle: {top: '23%', left: '48%'},
            city_hall: {top: '12%', left: '72%'},
            palace: {top: '8%', left: '23%'},
            white_dove_house: {top: '31%', left: '61%'},
            park: {top: '50%', left: '40%'}
        }

        const position = buildingPositions[buildingType]
        if (!position) return null

        return (
            <div className="resident-avatars" style={{
                position: 'absolute',
                top: `calc(${position.top} + 18px)`, // 在白圈下方18px，更贴近
                left: position.left,
                transform: 'translateX(-50%)',
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'center',
                gap: '4px', // 稍微增加间距
                zIndex: 10,
                pointerEvents: 'auto' // 允许点击头像
            }}>
                {residents.slice(0, 3).map((resident, index) => { // 最多显示3个头像
                    const avatarPath = userAvatars[resident.userId]
                    return (
                        <div
                            key={resident.userId}
                            className="resident-avatar-small"
                            style={{
                                width: '20px', // 从16px增加到20px
                                height: '20px',
                                borderRadius: '50%',
                                border: '1px solid rgba(255, 255, 255, 0.8)',
                                overflow: 'hidden',
                                backgroundColor: 'rgba(255, 255, 255, 0.2)',
                                backgroundImage: avatarPath ? `url(${avatarPath})` : 'none',
                                backgroundSize: 'cover',
                                backgroundPosition: 'center',
                                backgroundRepeat: 'no-repeat',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: '10px', // 从8px增加到10px
                                color: 'white',
                                textShadow: '0 1px 2px rgba(0,0,0,0.8)',
                                boxShadow: '0 1px 3px rgba(0,0,0,0.3)',
                                cursor: 'pointer', // 添加点击指针
                                transition: 'all 0.2s ease'
                            }}
                            title={resident.userId}
                            onClick={(e) => {
                                e.stopPropagation()
                                handleResidentAvatarClick(resident.userId, avatarPath)
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.transform = 'scale(1.2)'
                                e.target.style.borderColor = 'rgba(255, 255, 255, 1)'
                                e.target.style.boxShadow = '0 2px 8px rgba(255, 255, 255, 0.4)'
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.transform = 'scale(1)'
                                e.target.style.borderColor = 'rgba(255, 255, 255, 0.8)'
                                e.target.style.boxShadow = '0 1px 3px rgba(0,0,0,0.3)'
                            }}
                        >
                            {!avatarPath && '👤'}
                        </div>
                    )
                })}
                {residents.length > 3 && (
                    <div
                        className="resident-count-more"
                        style={{
                            width: '20px', // 从16px增加到20px
                            height: '20px',
                            borderRadius: '50%',
                            border: '1px solid rgba(255, 255, 255, 0.8)',
                            backgroundColor: 'rgba(0, 0, 0, 0.6)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            fontSize: '9px', // 从8px增加到9px
                            color: 'white',
                            textShadow: '0 1px 2px rgba(0,0,0,0.8)',
                            boxShadow: '0 1px 3px rgba(0,0,0,0.3)'
                        }}
                        title={`还有${residents.length - 3}人`}
                    >
                        +{residents.length - 3}
                    </div>
                )}
            </div>
        )
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

        // 保存用户名到localStorage
        localStorage.setItem('eden_userName', newUserName)

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
            {/* 星星城背景音乐（彻底修复双重下载） */}
            <audio
                ref={starCityAudioRef}
                preload="none"
                style={{display: 'none'}}
                onEnded={handleMusicEnded}
            >
                {/* 🔥 移除source标签，避免HTML层面的下载 */}
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

                    {/* 天气特效层 */}
                    <div style={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        pointerEvents: 'none',
                        zIndex: 15,
                        overflow: 'hidden'
                    }}>
                        {/* 雨天特效 */}
                        {starCityData?.weather === 'rainy' && (
                            <>
                                {/* 雨滴 */}
                        {[...Array(isMobileDevice ? 50 : 80)].map((_, i) => {
                            const delay = Math.random() * 3;
                            const duration = 0.8 + Math.random() * 0.4;
                            const left = Math.random() * 110;
                            const startTop = -10 - Math.random() * 15;

                            return (
                                <div
                                    key={`rain-${i}`}
                                    style={{
                                        position: 'absolute',
                                        left: `${left}%`,
                                        top: `${startTop}%`,
                                        width: '2px',
                                        height: '25px',
                                        background: 'linear-gradient(180deg, transparent, rgba(174, 194, 224, 0.7), rgba(174, 194, 224, 0.9))',
                                        animation: `rainDrop ${duration}s linear infinite`,
                                        animationDelay: `${delay}s`,
                                        opacity: 0.9,
                                        willChange: 'transform',
                                        transform: 'translateZ(0)',
                                        transformOrigin: 'top center'
                                    }}
                                />
                            );
                        })}

                        {/* 雨雾效果 */}
                        <div style={{
                            position: 'absolute',
                            top: 0,
                            left: 0,
                            right: 0,
                            bottom: 0,
                            background: 'linear-gradient(to bottom, rgba(100, 120, 150, 0.15), transparent 40%)',
                            pointerEvents: 'none'
                        }}/>

                        {/* 地面水花效果 */}
                        {[...Array(isMobileDevice ? 12 : 20)].map((_, i) => {
                            const delay = Math.random() * 2;
                            const left = Math.random() * 100;

                            return (
                                <div
                                    key={`splash-${i}`}
                                    style={{
                                        position: 'absolute',
                                        left: `${left}%`,
                                        bottom: '5%',
                                        width: '4px',
                                        height: '4px',
                                        borderRadius: '50%',
                                        background: 'rgba(174, 194, 224, 0.6)',
                                        animation: `rainSplash 1.2s ease-out infinite`,
                                        animationDelay: `${delay}s`
                                    }}
                                />
                            );
                        })}
                            </>
                        )}

                        {/* 雪天特效 */}
                        {starCityData?.weather === 'snowy' && (
                            <>
                                {/* 雪花 */}
                                {[...Array(isMobileDevice ? 40 : 60)].map((_, i) => {
                                    const delay = Math.random() * 5;
                                    const duration = 3 + Math.random() * 2;
                                    const left = Math.random() * 110;
                                    const startTop = -10 - Math.random() * 20;
                                    const size = 8 + Math.random() * 8;
                                    
                                    return (
                                        <div
                                            key={`snow-${i}`}
                                            style={{
                                                position: 'absolute',
                                                left: `${left}%`,
                                                top: `${startTop}%`,
                                                fontSize: `${size}px`,
                                                animation: `snowFall ${duration}s linear infinite`,
                                                animationDelay: `${delay}s`,
                                                opacity: 0.9,
                                                willChange: 'transform',
                                                transform: 'translateZ(0)'
                                            }}
                                        >
                                            ❄️
                                        </div>
                                    );
                                })}
                            </>
                        )}

                        {/* 多云特效 */}
                        {starCityData?.weather === 'cloudy' && (
                            <>
                                {/* 云朵 */}
                                {[...Array(isMobileDevice ? 4 : 6)].map((_, i) => {
                                    const duration = 20 + i * 5;
                                    const delay = i * 3;
                                    const top = 5 + i * 12;
                                    
                                    return (
                                        <div
                                            key={`cloud-${i}`}
                                            style={{
                                                position: 'absolute',
                                                top: `${top}%`,
                                                left: '-10%',
                                                fontSize: '60px',
                                                opacity: 0.6,
                                                animation: `cloudMove ${duration}s linear infinite`,
                                                animationDelay: `${delay}s`,
                                                willChange: 'transform',
                                                transform: 'translateZ(0)'
                                            }}
                                        >
                                            ☁️
                                        </div>
                                    );
                                })}
                            </>
                        )}

                        {/* 夜晚特效 */}
                        {starCityData?.weather === 'night' && (
                            <>
                                {/* 月亮 */}
                                <div style={{
                                    position: 'absolute',
                                    top: '4%',
                                    right: '4%',
                                    fontSize: '60px',
                                    animation: 'moonGlow 3s ease-in-out infinite',
                                    zIndex: 5,
                                    pointerEvents: 'none',
                                    willChange: 'transform',
                                    transform: 'translateZ(0)'
                                }}>
                                    🌙
                                </div>
                                
                                {/* 星星 - 分布在屏幕上半部边框10%区域 */}
                                {[...Array(isMobileDevice ? 12 : 20)].map((_, i) => {
                                    const starCount = isMobileDevice ? 12 : 20;
                                    const quarterCount = Math.floor(starCount / 4);
                                    
                                    let top, left;
                                    
                                    // 将星星分成4组，分布在上半部的四个边框区域
                                    if (i < quarterCount) {
                                        // 左上角：左边框 0-8%，上半部 0-50%
                                        left = Math.random() * 8;
                                        top = Math.random() * 50;
                                    } else if (i < quarterCount * 2) {
                                        // 顶部左侧：上边框 0-10%，左半部 10-45%
                                        top = Math.random() * 10;
                                        left = 10 + Math.random() * 35;
                                    } else if (i < quarterCount * 3) {
                                        // 顶部右侧：上边框 0-10%，右半部 55-90%
                                        top = Math.random() * 10;
                                        left = 55 + Math.random() * 35;
                                    } else {
                                        // 右上角：右边框 90-100%，上半部 0-50%
                                        left = 92 + Math.random() * 8;
                                        top = Math.random() * 50;
                                    }
                                    
                                    const delay = Math.random() * 3;
                                    const duration = 1.5 + Math.random() * 1.5;
                                    
                                    return (
                                        <div
                                            key={`star-${i}`}
                                            style={{
                                                position: 'absolute',
                                                top: `${top}%`,
                                                left: `${left}%`,
                                                fontSize: '12px',
                                                animation: `starTwinkle ${duration}s ease-in-out infinite`,
                                                animationDelay: `${delay}s`,
                                                pointerEvents: 'none',
                                                zIndex: 5,
                                                willChange: 'opacity',
                                                transform: 'translateZ(0)'
                                            }}
                                        >
                                            ⭐
                                        </div>
                                    );
                                })}
                                
                                {/* 夜晚遮罩 */}
                                <div style={{
                                    position: 'absolute',
                                    top: 0,
                                    left: 0,
                                    right: 0,
                                    bottom: 0,
                                    background: 'rgba(0, 10, 30, 0.4)',
                                    pointerEvents: 'none'
                                }} />
                            </>
                        )}

                        {/* 晴天特效 */}
                        {starCityData?.weather === 'sunny' && (
                            <>
                                {/* 太阳 */}
                                <div style={{
                                    position: 'absolute',
                                    top: '10%',
                                    right: '10%',
                                    width: '80px',
                                    height: '80px',
                                    background: 'radial-gradient(circle, rgba(255, 215, 0, 0.8) 0%, transparent 70%)',
                                    borderRadius: '50%',
                                    boxShadow: '0 0 60px rgba(255, 215, 0, 0.6)',
                                    animation: 'sunRotate 20s linear infinite',
                                    pointerEvents: 'none',
                                    zIndex: 5,
                                    willChange: 'transform',
                                    transform: 'translateZ(0)'
                                }}>
                                    {/* 太阳光线 */}
                                    {[...Array(12)].map((_, i) => (
                                        <div
                                            key={`ray-${i}`}
                                            style={{
                                                position: 'absolute',
                                                top: '50%',
                                                left: '50%',
                                                width: '4px',
                                                height: '40px',
                                                background: 'linear-gradient(transparent, rgba(255, 215, 0, 0.6))',
                                                transform: `rotate(${i * 30}deg) translateY(-60px)`,
                                                transformOrigin: '2px 60px'
                                            }}
                                        />
                                    ))}
                                </div>
                            </>
                        )}
                    </div>

                    {/* 标题 */}
                    <h2 style={{
                        fontSize: '42px',
                        marginBottom: '10px',
                        textShadow: '0 0 25px rgba(0,0,0,0.8), 0 0 50px rgba(255,255,255,0.6)',
                        position: 'absolute',
                        bottom: '10px',
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
                            width: '12px', // 从15px缩小到12px
                            height: '12px', // 从15px缩小到12px
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
                        {/* 特殊情侣飘动爱心 */}
                        {allBuildingResidents.castle && isSpecialCouple(allBuildingResidents.castle) && (
                            <div style={{
                                position: 'absolute',
                                top: '-25px', // 从-35px调整到-25px，更贴近白圈
                                left: '50%',
                                transform: 'translateX(-50%)',
                                fontSize: '18px',
                                color: '#ff1744',
                                animation: 'floatingHeart 3s ease-in-out infinite',
                                zIndex: 15,
                                pointerEvents: 'none'
                            }}>
                                💗
                            </div>
                        )}
                    </div>

                    {/* 城堡居民头像列表 */}
                    {allBuildingResidents.castle && renderResidentAvatars('castle', allBuildingResidents.castle)}

                    {/* 市政厅 - 左上方 */}
                    <div
                        onClick={() => handleBuildingClick('city_hall')}
                        style={{
                            position: 'absolute',
                            top: '12%',
                            left: '72%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px', // 保持12px
                            height: '12px', // 保持12px
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
                        {/* 特殊情侣飘动爱心 */}
                        {allBuildingResidents.city_hall && isSpecialCouple(allBuildingResidents.city_hall) && (
                            <div style={{
                                position: 'absolute',
                                top: '-25px', // 从-35px调整到-25px，更贴近白圈
                                left: '50%',
                                transform: 'translateX(-50%)',
                                fontSize: '18px',
                                color: '#ff1744',
                                animation: 'floatingHeart 3s ease-in-out infinite',
                                zIndex: 15,
                                pointerEvents: 'none'
                            }}>
                                💗
                            </div>
                        )}
                    </div>

                    {/* 市政厅居民头像列表 */}
                    {allBuildingResidents.city_hall && renderResidentAvatars('city_hall', allBuildingResidents.city_hall)}

                    {/* 行宫 - 右上方 */}
                    <div
                        onClick={() => handleBuildingClick('palace')}
                        style={{
                            position: 'absolute',
                            top: '8%',
                            left: '23%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px', // 从15px缩小到12px
                            height: '12px', // 从15px缩小到12px
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
                        {/* 特殊情侣飘动爱心 */}
                        {allBuildingResidents.palace && isSpecialCouple(allBuildingResidents.palace) && (
                            <div style={{
                                position: 'absolute',
                                top: '-25px', // 从-35px调整到-25px，更贴近白圈
                                left: '50%',
                                transform: 'translateX(-50%)',
                                fontSize: '18px',
                                color: '#ff1744',
                                animation: 'floatingHeart 3s ease-in-out infinite',
                                zIndex: 15,
                                pointerEvents: 'none'
                            }}>
                                💗
                            </div>
                        )}
                    </div>

                    {/* 行宫居民头像列表 */}
                    {allBuildingResidents.palace && renderResidentAvatars('palace', allBuildingResidents.palace)}

                    {/* 小白鸽家 - 左下方 */}
                    <div
                        onClick={() => handleBuildingClick('white_dove_house')}
                        style={{
                            position: 'absolute',
                            top: '31%',
                            left: '61%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px', // 从15px缩小到12px
                            height: '12px', // 从15px缩小到12px
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
                        {/* 特殊情侣飘动爱心 */}
                        {allBuildingResidents.white_dove_house && isSpecialCouple(allBuildingResidents.white_dove_house) && (
                            <div style={{
                                position: 'absolute',
                                top: '-25px', // 从-35px调整到-25px，更贴近白圈
                                left: '50%',
                                transform: 'translateX(-50%)',
                                fontSize: '18px',
                                color: '#ff1744',
                                animation: 'floatingHeart 3s ease-in-out infinite',
                                zIndex: 15,
                                pointerEvents: 'none'
                            }}>
                                💗
                            </div>
                        )}
                    </div>

                    {/* 小白鸽家居民头像列表 */}
                    {allBuildingResidents.white_dove_house && renderResidentAvatars('white_dove_house', allBuildingResidents.white_dove_house)}

                    {/* 公园 - 右下方 */}
                    <div
                        onClick={() => handleBuildingClick('park')}
                        style={{
                            position: 'absolute',
                            top: '50%',
                            left: '40%',
                            transform: 'translate(-50%, -50%)',
                            width: '12px', // 从15px缩小到12px
                            height: '12px', // 从15px缩小到12px
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
                        {/* 特殊情侣飘动爱心 */}
                        {allBuildingResidents.park && isSpecialCouple(allBuildingResidents.park) && (
                            <div style={{
                                position: 'absolute',
                                top: '-25px', // 从-35px调整到-25px，更贴近白圈
                                left: '50%',
                                transform: 'translateX(-50%)',
                                fontSize: '18px',
                                color: '#ff1744',
                                animation: 'floatingHeart 3s ease-in-out infinite',
                                zIndex: 15,
                                pointerEvents: 'none'
                            }}>
                                💗
                            </div>
                        )}
                    </div>

                    {/* 公园居民头像列表 */}
                    {allBuildingResidents.park && renderResidentAvatars('park', allBuildingResidents.park)}

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
                                bottom: '15px',
                                right: '15px',
                                background: 'rgba(0, 0, 0, 0.7)',
                                color: 'white',
                                padding: '8px 12px',
                                borderRadius: '10px',
                                backdropFilter: 'blur(10px)',
                                border: '1px solid rgba(255, 255, 255, 0.2)',
                                minWidth: '140px',
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
                                fontSize: '12px',
                                fontWeight: 'bold',
                                marginBottom: '4px',
                                color: '#FFD700',
                                textShadow: '0 0 6px rgba(255, 215, 0, 0.5)'
                            }}>
                                城市数据
                            </div>

                            <div style={{
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '2px',
                                fontSize: '10px'
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

                            {/* 特殊居住组合状态显示 */}
                            {specialCombos && specialCombos.hasSpecialCombos && (
                                <div className="special-combo-info" style={{
                                    marginTop: '6px',
                                    padding: '4px 6px',
                                    background: 'rgba(255, 105, 180, 0.2)',
                                    borderRadius: '4px',
                                    border: '1px solid rgba(255, 105, 180, 0.4)',
                                    animation: 'loveGlow 2s ease-in-out infinite alternate'
                                }}>
                                    <div style={{
                                        fontSize: '8px',
                                        color: '#FF69B4',
                                        marginBottom: '1px',
                                        fontWeight: 'bold',
                                        textAlign: 'center'
                                    }}>
                                        💕 爱情加成 💕
                                    </div>
                                    <div style={{
                                        fontSize: '8px',
                                        lineHeight: '1.1',
                                        textAlign: 'center',
                                        color: '#FFB6C1'
                                    }}>
                                        每小时人口 +{specialCombos.totalHourlyBonus}
                                    </div>
                                </div>
                            )}

                            {starCityData.canUpgrade && starCityData.nextLevelRequirements && (
                                <div className="upgrade-info" style={{
                                    marginTop: '6px',
                                    padding: '4px',
                                    background: 'rgba(255, 215, 0, 0.2)',
                                    borderRadius: '4px',
                                    border: '1px solid rgba(255, 215, 0, 0.3)'
                                }}>
                                    <div style={{fontSize: '8px', color: '#FFD700', marginBottom: '1px'}}>
                                        🎯 升级条件 (LV{starCityData.level + 1}):
                                    </div>
                                    <div style={{fontSize: '7px', lineHeight: '1.1'}}>
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
                        maxWidth: '630px',
                        width: isMobileDevice ? '95%' : '90%',
                        minHeight: 'auto',
                        maxHeight: isMobileDevice ? '400px' : '80vh',
                        textAlign: 'center',
                        boxShadow: '0 20px 60px rgba(0, 0, 0, 0.3)',
                        border: '2px solid rgba(255, 255, 255, 0.2)',
                        color: 'white',
                        fontSize: isMobileDevice ? '14px' : '16px',
                        position: 'relative',
                        overflow: 'hidden',
                        display: 'flex',
                        flexDirection: 'column'
                    }}>
                        {/* 特殊情侣的爱心背景动画 - 覆盖整个弹框（从事件接口控制） */}
                        {selectedBuilding && residenceEvents[selectedBuilding.key] && residenceEvents[selectedBuilding.key].showSpecialEffect && (
                            <div style={{
                                position: 'absolute',
                                top: 0,
                                left: 0,
                                right: 0,
                                bottom: '80px', // 避免出现在按钮区域，留出底部空间
                                pointerEvents: 'none',
                                zIndex: 0 // 降低到最底层，不遮挡任何内容
                            }}>
                                {/* 🔥 CPU优化：减少爱心数量，移动端4个，桌面端8个 */}
                                {[...Array(isMobileDevice ? 8 : 8)].map((_, i) => {
                                    // 🔥 CPU优化：使用预定义位置，减少Math.random()计算
                                    const positions = [
                                        {left: 15, top: 20},
                                        {left: 85, top: 30},
                                        {left: 10, top: 70},
                                        {left: 90, top: 80},
                                        {left: 20, top: 50},
                                        {left: 80, top: 60},
                                        {left: 25, top: 85},
                                        {left: 75, top: 15}
                                    ]
                                    const pos = positions[i] || positions[0]

                                    return (
                                        <div
                                            key={i}
                                            style={{
                                                position: 'absolute',
                                                fontSize: `${isMobileDevice ? 25 : 25}px`, // 移动端更小
                                                color: '#ff69b4',
                                                left: `${pos.left}%`,
                                                top: `${pos.top}%`,
                                                animation: `heartFloat ${2 + (i % 2) * 1}s ease-in-out infinite`,
                                                animationDelay: `${i * 0.3}s`,
                                                opacity: 0.4,
                                                // 🔥 CPU优化：强制GPU加速
                                                willChange: 'transform',
                                                transform: 'translateZ(0)'
                                            }}
                                        >
                                            💖
                                        </div>
                                    );
                                })}

                                {/* 🔥 CPU优化：移动端移除装饰性爱心，减少CPU负担 */}
                                {!isMobileDevice && [...Array(3)].map((_, i) => {
                                    // 顶部区域的爱心
                                    const isTopArea = i < 3;
                                    const topPosition = isTopArea
                                        ? Math.random() * 20 // 顶部 0-20%
                                        : 80 + Math.random() * 15; // 底部 80-95%

                                    return (
                                        <div
                                            key={`decoration-${i}`}
                                            style={{
                                                position: 'absolute',
                                                fontSize: `${Math.random() * 12 + 10}px`, // 更小的装饰性爱心
                                                color: '#ff69b4',
                                                left: `${20 + Math.random() * 60}%`, // 中间区域 20-80%
                                                top: `${topPosition}%`,
                                                animation: `heartFloat ${3 + Math.random() * 2}s ease-in-out infinite`,
                                                animationDelay: `${Math.random() * 3}s`,
                                                opacity: 0.25 // 更淡的装饰性爱心
                                            }}
                                        >
                                            💕
                                        </div>
                                    );
                                })}
                            </div>
                        )}
                        {/* 建筑信息 */}
                        <div style={{
                            background: 'rgba(255, 255, 255, 0.1)',
                            borderRadius: '15px',
                            padding: '10px', // 从20px减少到15px
                            marginBottom: '15px', // 从25px减少到15px
                            position: 'relative',
                            zIndex: 2,
                            flex: 1, // 允许这个区域占据剩余空间
                            display: 'flex',
                            flexDirection: 'column',
                            minHeight: 0 // 重要：允许flex子元素收缩到小于内容高度
                        }}>
                            <div style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                gap: '12px',
                                marginBottom: '6px', // 从8px减少到6px
                                flexShrink: 0 // 防止标题被压缩
                            }}>
                                <div style={{
                                    fontSize: '48px'
                                }}>
                                    {selectedBuilding.emoji}
                                </div>
                                <div style={{
                                    fontSize: '20px',
                                    fontWeight: 'bold'
                                }}>
                                    {selectedBuilding.name}
                                </div>
                            </div>

                            {/* 当前居住人员 */}
                            <div style={{
                                fontSize: '14px',
                                opacity: 0.9,
                                marginBottom: '12px', // 从15px减少到12px
                                position: 'relative',
                                zIndex: 2,
                                flexShrink: 0 // 防止居住人员区域被压缩
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
                                        {/* 普通显示 - 现在统一处理 */}
                                        {buildingResidents.length > 0 ? (
                                            <div style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                                gap: '8px',
                                                flexWrap: 'wrap'
                                            }}>
                                                <span style={{fontSize: '14px'}}>
                                                    当前居住人员：
                                                </span>
                                                <div style={{
                                                    background: 'rgba(0, 0, 0, 0.2)',
                                                    borderRadius: '8px',
                                                    padding: '4px 8px',
                                                    fontSize: '12px',
                                                    wordBreak: 'break-all',
                                                    lineHeight: '1.4'
                                                }}>
                                                    👤 {buildingResidents.map(resident => resident.userId).join(', ')}
                                                </div>
                                            </div>
                                        ) : (
                                            <div style={{
                                                color: 'rgba(255, 255, 255, 0.6)',
                                                fontSize: '12px',
                                                textAlign: 'center'
                                            }}>
                                                当前居住人员：暂无居住人员
                                            </div>
                                        )}
                                    </>
                                )}
                            </div>

                            {/* 居所事件显示 */}
                            {selectedBuilding && residenceEvents[selectedBuilding.key] && (
                                <div
                                    className="residence-event-scroll"
                                    style={{
                                        paddingRight: '5px', // 为滚动条留出空间
                                        flex: 1, // 占据剩余空间
                                        overflowY: 'auto', // 允许垂直滚动
                                        minHeight: 0, // 允许收缩
                                        WebkitOverflowScrolling: 'touch' // iOS平滑滚动
                                    }}
                                >
                                    {/* 渲染多条事件 */}
                                    {residenceEvents[selectedBuilding.key].events && residenceEvents[selectedBuilding.key].events.map((event, index) => (
                                        <div key={index} style={{
                                            marginBottom: index < residenceEvents[selectedBuilding.key].events.length - 1 ? '6px' : '0',
                                            textAlign: 'center'
                                        }}>
                                            <div style={{
                                                fontSize: event.type === 'special' ? '16px' : '14px',
                                                fontWeight: event.type === 'special' ? 'bold' : '500',
                                                lineHeight: '1.4',
                                                color: event.type === 'special' ? '#ff69b4' : 'rgba(255, 255, 255, 0.9)',
                                                textShadow: event.type === 'special'
                                                    ? '0 0 10px rgba(255, 105, 180, 0.5)'
                                                    : 'none',
                                                animation: event.type === 'special'
                                                    ? 'loveGlow 2s ease-in-out infinite alternate'
                                                    : 'none'
                                            }}>
                                                {event.description || '未知事件'}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* 按钮区域 */}
                        <div style={{
                            display: 'flex',
                            gap: '15px',
                            justifyContent: 'center',
                            position: 'relative',
                            zIndex: 2,
                            flexShrink: 0 // 防止按钮区域被压缩
                        }}>
                            {/* 危险警告文字 */}
                            {isDangerousResidence(buildingResidents, userName) && (
                                <div style={{
                                    position: 'absolute',
                                    top: '-25px',
                                    left: '50%',
                                    transform: 'translateX(-50%)',
                                    fontSize: '12px',
                                    color: '#ff6b6b',
                                    fontWeight: 'bold',
                                    textAlign: 'center',
                                    whiteSpace: 'nowrap'
                                }}>
                                    ⚠️警告！真的要住进来吗？
                                </div>
                            )}
                            <button
                                onClick={isUserAlreadyInResidence(buildingResidents, userName) ? refreshCurrentResidenceEvents : confirmResidence}
                                disabled={false}
                                style={{
                                    background: isUserAlreadyInResidence(buildingResidents, userName)
                                        ? 'linear-gradient(135deg, #ff9800, #f57c00)' // 橙色表示搞事情
                                        : isDangerousResidence(buildingResidents, userName)
                                            ? 'linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%)'
                                            : 'rgba(255, 255, 255, 0.2)',
                                    color: 'white',
                                    borderRadius: '25px',
                                    padding: '12px 25px',
                                    fontSize: '16px',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    backdropFilter: 'blur(10px)',
                                    border: isUserAlreadyInResidence(buildingResidents, userName)
                                        ? '1px solid rgba(255, 152, 0, 0.5)'
                                        : isDangerousResidence(buildingResidents, userName)
                                            ? '1px solid rgba(255, 107, 107, 0.5)'
                                            : '1px solid rgba(255, 255, 255, 0.3)',
                                    fontWeight: 'bold',
                                    boxShadow: isUserAlreadyInResidence(buildingResidents, userName)
                                        ? '0 4px 15px rgba(255, 152, 0, 0.3)'
                                        : isDangerousResidence(buildingResidents, userName)
                                            ? '0 4px 15px rgba(255, 107, 107, 0.3)'
                                            : 'none',
                                    opacity: 1
                                }}
                                onMouseEnter={(e) => {
                                    if (isUserAlreadyInResidence(buildingResidents, userName)) {
                                        e.target.style.background = 'linear-gradient(135deg, #ffb74d, #ff9800)'
                                        e.target.style.boxShadow = '0 6px 20px rgba(255, 152, 0, 0.4)'
                                    } else if (isDangerousResidence(buildingResidents, userName)) {
                                        e.target.style.background = 'linear-gradient(135deg, #ff5252 0%, #d32f2f 100%)'
                                        e.target.style.boxShadow = '0 6px 20px rgba(255, 107, 107, 0.4)'
                                    } else {
                                        e.target.style.background = 'rgba(255, 255, 255, 0.3)'
                                    }
                                    e.target.style.transform = 'scale(1.05)'
                                }}
                                onMouseLeave={(e) => {
                                    if (isUserAlreadyInResidence(buildingResidents, userName)) {
                                        e.target.style.background = 'linear-gradient(135deg, #ff9800, #f57c00)'
                                        e.target.style.boxShadow = '0 4px 15px rgba(255, 152, 0, 0.3)'
                                    } else if (isDangerousResidence(buildingResidents, userName)) {
                                        e.target.style.background = 'linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%)'
                                        e.target.style.boxShadow = '0 4px 15px rgba(255, 107, 107, 0.3)'
                                    } else {
                                        e.target.style.background = 'rgba(255, 255, 255, 0.2)'
                                    }
                                    e.target.style.transform = 'scale(1)'
                                }}
                            >
                                {isUserAlreadyInResidence(buildingResidents, userName) ? '搞点事情' : '居住'}
                            </button>
                            <button
                                onClick={showResidenceEventHistory}
                                style={{
                                    background: 'rgba(255, 255, 255, 0.15)',
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
                                    e.target.style.background = 'rgba(255, 255, 255, 0.25)'
                                    e.target.style.transform = 'scale(1.05)'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.15)'
                                    e.target.style.transform = 'scale(1)'
                                }}
                            >
                                历史
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

            {/* 用户头像 - 左上角 */}
            {userName && userInfo && userInfo.message !== '用户不存在' && (
                <div
                    className="main-page-avatar"
                    onClick={(e) => {
                        e.stopPropagation()
                        handleAvatarClick()
                    }}
                    style={{
                        position: 'fixed',
                        top: '20px',
                        left: '20px',
                        width: '60px',
                        height: '60px',
                        borderRadius: '50%',
                        backgroundImage: userAvatar ? `url(${userAvatar})` : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        backgroundRepeat: 'no-repeat',
                        border: '3px solid rgba(255, 255, 255, 0.8)',
                        cursor: 'pointer',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        transition: 'all 0.3s ease',
                        boxShadow: '0 4px 15px rgba(0, 0, 0, 0.3)',
                        zIndex: 1000,
                        pointerEvents: 'auto',
                        overflow: 'hidden', // 确保内容不会溢出圆形边界
                        boxSizing: 'border-box' // 确保边框包含在尺寸内
                    }}
                    onMouseEnter={(e) => {
                        e.target.style.transform = 'scale(1.1)';
                        e.target.style.boxShadow = '0 6px 20px rgba(0, 0, 0, 0.4)';
                    }}
                    onMouseLeave={(e) => {
                        e.target.style.transform = 'scale(1)';
                        e.target.style.boxShadow = '0 4px 15px rgba(0, 0, 0, 0.3)';
                    }}
                >
                    {!userAvatar && (
                        <div style={{
                            fontSize: '24px',
                            color: 'white',
                            textShadow: '0 2px 4px rgba(0, 0, 0, 0.3)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            width: '100%',
                            height: '100%',
                            marginTop: '-4px'
                        }}>
                            📷
                        </div>
                    )}
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

            {/* 头像裁剪弹窗 */}
            <AvatarCrop
                isOpen={showAvatarCrop}
                onClose={() => setShowAvatarCrop(false)}
                onSave={handleAvatarSave}
                userName={userName}
            />

            {/* 用户头像预览弹框 */}
            {showAvatarPreview && userAvatar && (
                <div
                    className={`avatar-preview-overlay ${isMobileDevice ? 'force-landscape' : ''}`}
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0, 0, 0, 0.8)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100000,
                        backdropFilter: 'blur(8px)'
                    }}
                    onClick={closeAvatarPreview}
                >
                    <div
                        className="avatar-preview-content"
                        style={{
                            background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.95))',
                            borderRadius: '25px',
                            padding: '40px',
                            maxWidth: '450px',
                            width: '90%',
                            textAlign: 'center',
                            boxShadow: '0 25px 50px rgba(0, 0, 0, 0.4), 0 0 0 1px rgba(255, 255, 255, 0.2)',
                            border: '2px solid rgba(255, 255, 255, 0.3)',
                            backdropFilter: 'blur(15px)',
                            position: 'relative',
                            animation: 'fadeInScale 0.3s ease-out'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* 关闭按钮 */}
                        <button
                            onClick={closeAvatarPreview}
                            style={{
                                position: 'absolute',
                                top: '20px',
                                right: '20px',
                                width: '35px',
                                height: '35px',
                                borderRadius: '50%',
                                border: 'none',
                                background: 'rgba(255, 255, 255, 0.8)',
                                color: '#666',
                                fontSize: '18px',
                                cursor: 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                transition: 'all 0.2s ease',
                                boxShadow: '0 3px 10px rgba(0, 0, 0, 0.15)'
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.background = 'rgba(255, 255, 255, 1)'
                                e.target.style.color = '#333'
                                e.target.style.transform = 'scale(1.1)'
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                                e.target.style.color = '#666'
                                e.target.style.transform = 'scale(1)'
                            }}
                        >
                            ✕
                        </button>

                        {/* 标题 */}
                        <h3 style={{
                            margin: '0 0 25px',
                            fontSize: '28px',
                            fontWeight: '600',
                            color: '#333',
                            textShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                            background: 'linear-gradient(135deg, #667eea, #764ba2)',
                            WebkitBackgroundClip: 'text',
                            WebkitTextFillColor: 'transparent',
                            backgroundClip: 'text'
                        }}>
                            我的头像
                        </h3>

                        {/* 头像显示 */}
                        <div style={{
                            width: '200px',
                            height: '200px',
                            borderRadius: '50%',
                            margin: '0 auto 30px',
                            overflow: 'hidden',
                            border: '5px solid rgba(255, 255, 255, 0.9)',
                            boxShadow: '0 15px 40px rgba(0, 0, 0, 0.3), inset 0 0 0 2px rgba(255, 255, 255, 0.4)',
                            background: `url(${userAvatar})`,
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                            backgroundRepeat: 'no-repeat',
                            position: 'relative',
                            transition: 'all 0.3s ease'
                        }}
                             onMouseEnter={(e) => {
                                 e.target.style.transform = 'scale(1.05)'
                                 e.target.style.boxShadow = '0 20px 50px rgba(0, 0, 0, 0.4), inset 0 0 0 2px rgba(255, 255, 255, 0.6)'
                             }}
                             onMouseLeave={(e) => {
                                 e.target.style.transform = 'scale(1)'
                                 e.target.style.boxShadow = '0 15px 40px rgba(0, 0, 0, 0.3), inset 0 0 0 2px rgba(255, 255, 255, 0.4)'
                             }}
                        />

                        {/* 用户名显示 */}
                        <div style={{
                            margin: '0 0 25px',
                            fontSize: '20px',
                            fontWeight: '500',
                            color: '#555',
                            background: 'rgba(255, 255, 255, 0.7)',
                            padding: '12px 20px',
                            borderRadius: '15px',
                            border: '1px solid rgba(255, 255, 255, 0.8)',
                            boxShadow: '0 3px 10px rgba(0, 0, 0, 0.1)'
                        }}>
                            👤 {userName}
                        </div>

                        {/* 操作按钮 */}
                        <div style={{
                            display: 'flex',
                            gap: '15px',
                            justifyContent: 'center',
                            flexWrap: 'wrap'
                        }}>
                            <button
                                onClick={openAvatarCropFromPreview}
                                style={{
                                    padding: '12px 25px',
                                    borderRadius: '25px',
                                    border: 'none',
                                    background: 'linear-gradient(135deg, #667eea, #764ba2)',
                                    color: 'white',
                                    fontSize: '16px',
                                    fontWeight: '500',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    boxShadow: '0 5px 15px rgba(102, 126, 234, 0.4)',
                                    minWidth: '120px'
                                }}
                                onMouseEnter={(e) => {
                                    e.target.style.transform = 'translateY(-2px)'
                                    e.target.style.boxShadow = '0 8px 25px rgba(102, 126, 234, 0.6)'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.transform = 'translateY(0)'
                                    e.target.style.boxShadow = '0 5px 15px rgba(102, 126, 234, 0.4)'
                                }}
                            >
                                替换头像
                            </button>

                            <button
                                onClick={closeAvatarPreview}
                                style={{
                                    padding: '12px 25px',
                                    borderRadius: '25px',
                                    border: '2px solid rgba(255, 255, 255, 0.8)',
                                    background: 'rgba(255, 255, 255, 0.8)',
                                    color: '#666',
                                    fontSize: '16px',
                                    fontWeight: '500',
                                    cursor: 'pointer',
                                    transition: 'all 0.3s ease',
                                    boxShadow: '0 3px 10px rgba(0, 0, 0, 0.1)',
                                    minWidth: '120px'
                                }}
                                onMouseEnter={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 1)'
                                    e.target.style.color = '#333'
                                    e.target.style.transform = 'translateY(-2px)'
                                    e.target.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.2)'
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                                    e.target.style.color = '#666'
                                    e.target.style.transform = 'translateY(0)'
                                    e.target.style.boxShadow = '0 3px 10px rgba(0, 0, 0, 0.1)'
                                }}
                            >
                                保持当前
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* 居民头像详情弹框 */}
            {showResidentDetail && selectedResident && (
                <div
                    className={`resident-detail-overlay ${isMobileDevice ? 'force-landscape' : ''}`}
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0, 0, 0, 0.7)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100000,
                        backdropFilter: 'blur(5px)'
                    }}
                    onClick={closeResidentDetail}
                >
                    <div
                        className="resident-detail-content"
                        style={{
                            background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.95))',
                            borderRadius: '20px',
                            padding: '30px',
                            maxWidth: '400px',
                            width: '90%',
                            textAlign: 'center',
                            boxShadow: '0 20px 40px rgba(0, 0, 0, 0.3), 0 0 0 1px rgba(255, 255, 255, 0.2)',
                            border: '2px solid rgba(255, 255, 255, 0.3)',
                            backdropFilter: 'blur(10px)',
                            position: 'relative',
                            animation: 'fadeInScale 0.3s ease-out'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* 关闭按钮 */}
                        <button
                            onClick={closeResidentDetail}
                            style={{
                                position: 'absolute',
                                top: '15px',
                                right: '15px',
                                width: '30px',
                                height: '30px',
                                borderRadius: '50%',
                                border: 'none',
                                background: 'rgba(255, 255, 255, 0.8)',
                                color: '#666',
                                fontSize: '16px',
                                cursor: 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                transition: 'all 0.2s ease',
                                boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)'
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.background = 'rgba(255, 255, 255, 1)'
                                e.target.style.color = '#333'
                                e.target.style.transform = 'scale(1.1)'
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.background = 'rgba(255, 255, 255, 0.8)'
                                e.target.style.color = '#666'
                                e.target.style.transform = 'scale(1)'
                            }}
                        >
                            ✕
                        </button>

                        {/* 头像显示 */}
                        <div style={{
                            width: '200px',
                            height: '200px',
                            borderRadius: '50%',
                            margin: '0 auto 20px',
                            overflow: 'hidden',
                            border: '4px solid rgba(255, 255, 255, 0.8)',
                            boxShadow: '0 8px 32px rgba(0, 0, 0, 0.2), inset 0 0 0 1px rgba(255, 255, 255, 0.3)',
                            background: selectedResident.avatarPath
                                ? `url(${selectedResident.avatarPath})`
                                : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                            backgroundRepeat: 'no-repeat',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center'
                        }}>
                            {!selectedResident.avatarPath && (
                                <div style={{
                                    fontSize: '80px',
                                    color: 'rgba(255, 255, 255, 0.8)',
                                    textShadow: '0 2px 10px rgba(0, 0, 0, 0.3)'
                                }}>
                                    👤
                                </div>
                            )}
                        </div>

                        {/* 用户名和状态 */}
                        {loadingResidentDetail ? (
                            <>
                                <h3 style={{
                                    margin: '0 0 15px',
                                    fontSize: '24px',
                                    fontWeight: '600',
                                    color: '#333',
                                    textShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
                                    background: 'linear-gradient(135deg, #667eea, #764ba2)',
                                    WebkitBackgroundClip: 'text',
                                    WebkitTextFillColor: 'transparent',
                                    backgroundClip: 'text'
                                }}>
                                    {selectedResident.userId}
                                </h3>
                                <div style={{
                                    padding: '10px',
                                    color: '#666',
                                    fontSize: '14px',
                                    fontStyle: 'italic'
                                }}>
                                    加载中...
                                </div>
                            </>
                        ) : residentDetailInfo ? (
                            <>
                                {/* 用户名和状态并排显示 */}
                                <div style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    gap: '15px',
                                    marginBottom: '15px',
                                    flexWrap: 'wrap'
                                }}>
                                    <h3 style={{
                                        margin: '0',
                                        fontSize: '24px',
                                        fontWeight: '600',
                                        color: '#333',
                                        textShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
                                        background: 'linear-gradient(135deg, #667eea, #764ba2)',
                                        WebkitBackgroundClip: 'text',
                                        WebkitTextFillColor: 'transparent',
                                        backgroundClip: 'text'
                                    }}>
                                        {selectedResident.userId}
                                    </h3>

                                    <div style={{
                                        padding: '6px 12px',
                                        borderRadius: '15px',
                                        color: 'white',
                                        fontSize: '12px',
                                        fontWeight: '500',
                                        whiteSpace: 'nowrap',
                                        ...getStatusStyle(residentDetailInfo.status || '在线')
                                    }}>
                                        {residentDetailInfo.status || '在线'}
                                    </div>
                                </div>

                                {/* 用户简介 */}
                                <div style={{
                                    padding: '15px 20px',
                                    background: 'rgba(255, 255, 255, 0.6)',
                                    borderRadius: '15px',
                                    border: '1px solid rgba(255, 255, 255, 0.8)',
                                    color: '#666',
                                    fontSize: '14px',
                                    lineHeight: '1.6',
                                    textAlign: 'center',
                                    minHeight: '60px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center'
                                }}>
                                    <div>
                                        {residentDetailInfo.profile || '这个人很神秘，什么都没有留下...'}
                                    </div>
                                </div>
                            </>
                        ) : (
                            /* 装饰性信息（当API调用失败时的后备显示） */
                            <div style={{
                                padding: '15px 20px',
                                background: 'rgba(255, 255, 255, 0.6)',
                                borderRadius: '15px',
                                border: '1px solid rgba(255, 255, 255, 0.8)',
                                color: '#666',
                                fontSize: '14px',
                                lineHeight: '1.6'
                            }}>
                                <div style={{marginBottom: '5px'}}>
                                    ✨ 星星城居民
                                </div>
                                <div>
                                    🏠 安居乐业中
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}

            {/* 事件历史弹窗 */}
            {showEventHistory && selectedBuilding && (
                <div
                    className={`residence-modal-overlay ${isMobileDevice ? 'force-landscape' : ''}`}
                    style={{
                        position: 'fixed',
                        top: isMobileDevice ? '50%' : 0,
                        left: isMobileDevice ? '50%' : 0,
                        width: '100%',
                        height: '100%',
                        background: 'rgba(0, 0, 0, 0.8)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100001,
                        transform: isMobileDevice ? 'translate(-50%, -50%) rotate(90deg)' : 'none',
                        transformOrigin: 'center center'
                    }}
                    onClick={() => setShowEventHistory(false)}
                >
                    <div
                        className="residence-modal-content"
                        style={{
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            borderRadius: '20px',
                            padding: '20px',
                            maxWidth: isMobileDevice ? '750px' : '600px',
                            width: isMobileDevice ? '95%' : '90%',
                            height: isMobileDevice ? '400px' : 'auto',
                            maxHeight: isMobileDevice ? '400px' : '80vh',
                            textAlign: 'center',
                            boxShadow: '0 20px 60px rgba(0, 0, 0, 0.3)',
                            border: '2px solid rgba(255, 255, 255, 0.2)',
                            color: 'white',
                            fontSize: isMobileDevice ? '14px' : '16px',
                            position: 'relative',
                            overflow: 'hidden',
                            display: 'flex',
                            flexDirection: 'column'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* 标题 */}
                        <div style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            marginBottom: isMobileDevice ? '15px' : '20px',
                            fontSize: isMobileDevice ? '18px' : '20px',
                            fontWeight: 'bold',
                            flexShrink: 0
                        }}>
              <span style={{marginRight: '10px', fontSize: '24px'}}>
                {selectedBuilding.icon}
              </span>
                            <span>{selectedBuilding.name} - 事件历史</span>
                        </div>

                        {/* 历史列表 */}
                        <div
                            ref={(el) => {
                                if (el && eventHistory.length > 0) {
                                    // 直接设置滚动到底部
                                    el.scrollTop = el.scrollHeight
                                }
                            }}
                            style={{
                                height: isMobileDevice ? '250px' : '400px',
                                overflowY: 'auto',
                                marginBottom: isMobileDevice ? '15px' : '20px',
                                paddingRight: '10px',
                                flex: 1,
                                minHeight: 0
                            }}
                            className="residence-event-scroll"
                        >
                            {loadingEventHistory ? (
                                <div style={{padding: '20px', color: 'rgba(255, 255, 255, 0.7)'}}>
                                    加载中...
                                </div>
                            ) : eventHistory.length === 0 ? (
                                <div style={{padding: '20px', color: 'rgba(255, 255, 255, 0.7)'}}>
                                    暂无历史记录
                                </div>
                            ) : (
                                eventHistory.map((history, index) => (
                                    <div
                                        key={history.id || index}
                                        style={{
                                            background: 'rgba(255, 255, 255, 0.1)',
                                            borderRadius: '15px',
                                            padding: '15px',
                                            marginBottom: '15px',
                                            textAlign: 'left',
                                            border: '1px solid rgba(255, 255, 255, 0.2)'
                                        }}
                                    >
                                        {/* 时间和居住人员 */}
                                        <div style={{
                                            display: 'flex',
                                            justifyContent: 'space-between',
                                            alignItems: 'center',
                                            marginBottom: '10px',
                                            fontSize: '12px',
                                            color: 'rgba(255, 255, 255, 0.8)'
                                        }}>
                                            <span>⏰ {formatHistoryTime(history.createdAt)}</span>
                                            <span>
                        👥 {JSON.parse(history.residentsInfo || '[]').join(', ') || '无人'}
                      </span>
                                        </div>

                                        {/* 事件列表 */}
                                        <div>
                                            {parseEventData(history.eventData).map((event, eventIndex) => (
                                                <div
                                                    key={eventIndex}
                                                    style={{
                                                        marginBottom: '8px',
                                                        color: event.type === 'special'
                                                            ? '#ffb3d9'
                                                            : 'rgba(255, 255, 255, 0.9)',
                                                        fontSize: '14px',
                                                        textShadow: event.type === 'special'
                                                            ? '0 0 10px rgba(255, 105, 180, 0.5)'
                                                            : 'none'
                                                    }}
                                                >
                                                    {event.description}
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>

                        {/* 关闭按钮 */}
                        <button
                            onClick={() => setShowEventHistory(false)}
                            style={{
                                background: 'rgba(255, 255, 255, 0.2)',
                                color: 'white',
                                borderRadius: '25px',
                                padding: isMobileDevice ? '10px 20px' : '12px 25px',
                                fontSize: isMobileDevice ? '14px' : '16px',
                                cursor: 'pointer',
                                transition: 'all 0.3s ease',
                                backdropFilter: 'blur(10px)',
                                border: '1px solid rgba(255, 255, 255, 0.3)',
                                fontWeight: 'bold',
                                flexShrink: 0
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
            )}

            {/* 耐力不足提示弹框 */}
            {showNoStaminaModal && (
                <div
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0, 0, 0, 0.7)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 100001,
                        backdropFilter: 'blur(5px)'
                    }}
                    onClick={() => setShowNoStaminaModal(false)}
                >
                    <div
                        style={{
                            background: 'linear-gradient(135deg, rgba(255, 240, 245, 0.95), rgba(255, 228, 230, 0.95))',
                            borderRadius: '20px',
                            padding: '40px',
                            maxWidth: '400px',
                            width: '90%',
                            textAlign: 'center',
                            boxShadow: '0 20px 40px rgba(0, 0, 0, 0.3), 0 0 0 1px rgba(255, 255, 255, 0.2)',
                            border: '2px solid rgba(255, 182, 193, 0.5)',
                            backdropFilter: 'blur(10px)',
                            position: 'relative',
                            animation: 'fadeInScale 0.3s ease-out'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* 表情图标 */}
                        <div style={{
                            fontSize: '50px',
                            marginBottom: '20px',
                            animation: 'pulse 2s ease-in-out infinite'
                        }}>
                            😴
                        </div>

                        {/* 提示文字 */}
                        <h3 style={{
                            fontSize: '24px',
                            fontWeight: 'bold',
                            marginBottom: '15px',
                            color: '#d63031',
                            textShadow: '0 2px 4px rgba(0, 0, 0, 0.1)'
                        }}>
                            精疲力尽了！
                        </h3>

                        <p style={{
                            fontSize: '16px',
                            color: '#666',
                            lineHeight: '1.6',
                            marginBottom: '25px'
                        }}>
                            休息一下，每30分钟会自动恢复
                        </p>

                        {/* 确定按钮 */}
                        <button
                            onClick={() => setShowNoStaminaModal(false)}
                            style={{
                                background: 'linear-gradient(135deg, #ff6b6b, #ee5a52)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '25px',
                                padding: '12px 40px',
                                fontSize: '16px',
                                fontWeight: 'bold',
                                cursor: 'pointer',
                                boxShadow: '0 4px 15px rgba(255, 107, 107, 0.3)',
                                transition: 'all 0.3s ease'
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.transform = 'scale(1.05)'
                                e.target.style.boxShadow = '0 6px 20px rgba(255, 107, 107, 0.4)'
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.transform = 'scale(1)'
                                e.target.style.boxShadow = '0 4px 15px rgba(255, 107, 107, 0.3)'
                            }}
                        >
                            知道了
                        </button>
                    </div>
                </div>
            )}
    </div>
  )
}

export default LotteryLuckyWheel

