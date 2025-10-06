// 星星城移动端性能优化代码片段

// 1. 减少背景星星数量（从50个减少到20个）
const backgroundStars = useMemo(() => {
    const stars = []
    // 移动端减少星星数量以提升性能
    const starCount = isMobileDevice ? 15 : 30 // 原来是50个
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
}, [isMobileDevice]) // 添加isMobileDevice依赖

// 2. 延迟音频预加载
useEffect(() => {
    if (showStarCity) {
        // 延迟预加载，避免阻塞页面渲染
        const timer = setTimeout(() => {
            preloadMusic()
        }, 1000) // 延迟1秒

        // 延迟播放音乐
        const musicTimer = setTimeout(() => {
            playStarCityMusic()
        }, 1500) // 延迟1.5秒播放

        return () => {
            clearTimeout(timer)
            clearTimeout(musicTimer)
        }
    }
}, [showStarCity])

// 3. 优化爱心特效数量
{selectedBuilding && residenceEvents[selectedBuilding.key] && residenceEvents[selectedBuilding.key].showSpecialEffect && (
    <div style={{
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: '80px',
        pointerEvents: 'none',
        zIndex: 0
    }}>
        {/* 减少爱心数量：移动端8个，桌面端12个 */}
        {[...Array(isMobileDevice ? 6 : 12)].map((_, i) => {
            const isLeftSide = i % 2 === 0;
            const leftPosition = isLeftSide 
                ? Math.random() * 25 
                : 75 + Math.random() * 25;
            const topPosition = Math.random() * 100;
            
            return (
                <div
                    key={i}
                    style={{
                        position: 'absolute',
                        fontSize: `${Math.random() * 12 + 12}px`, // 减小尺寸
                        color: '#ff69b4',
                        left: `${leftPosition}%`,
                        top: `${topPosition}%`,
                        animation: `heartFloat ${2 + Math.random() * 3}s ease-in-out infinite`,
                        animationDelay: `${Math.random() * 2}s`,
                        opacity: 0.3, // 降低透明度减少GPU负担
                        // 添加GPU加速
                        willChange: 'transform',
                        transform: 'translateZ(0)' // 强制GPU加速
                    }}
                >
                    💖
                </div>
            );
        })}
    </div>
)}

// 4. 优化背景图片加载
const getBackgroundImage = (level) => {
    // 移动端使用压缩版本（需要先创建这些文件）
    if (isMobileDevice) {
        return `/picture/mobile/lv${level || 1}.webp` // 使用WebP格式
    }
    return `/picture/lv${level || 1}.jpg`
}

// 5. 添加loading状态
const [isStarCityLoading, setIsStarCityLoading] = useState(true)

useEffect(() => {
    if (showStarCity) {
        setIsStarCityLoading(true)
        
        // 预加载背景图片
        const img = new Image()
        img.onload = () => {
            setIsStarCityLoading(false)
        }
        img.onerror = () => {
            setIsStarCityLoading(false) // 即使失败也要移除loading
        }
        img.src = getBackgroundImage(starCityData?.level)
    }
}, [showStarCity, starCityData?.level, isMobileDevice])

// 6. 优化事件监听器
useEffect(() => {
    if (!showStarCity) return

    let resizeTimer
    const checkScreenSize = () => {
        // 防抖处理，避免频繁调用
        clearTimeout(resizeTimer)
        resizeTimer = setTimeout(() => {
            const isMobile = window.innerWidth <= 768 || window.screen.width <= 768
            setIsMobileDevice(isMobile)
        }, 100)
    }

    checkScreenSize()
    window.addEventListener('resize', checkScreenSize, { passive: true })
    window.addEventListener('orientationchange', () => {
        setTimeout(checkScreenSize, 200)
    }, { passive: true })

    return () => {
        clearTimeout(resizeTimer)
        window.removeEventListener('resize', checkScreenSize)
        window.removeEventListener('orientationchange', checkScreenSize)
    }
}, [showStarCity])

// 7. 星星城容器优化渲染
{showStarCity && (
    <div
        className={`star-city-container ${isMobileDevice && !starCityClosing ? 'force-landscape' : ''} ${starCityClosing ? 'closing' : ''}`}
        style={{
            backgroundImage: isStarCityLoading ? 'none' : `url(${getBackgroundImage(starCityData?.level)})`,
            backgroundColor: isStarCityLoading ? '#667eea' : 'transparent', // loading时显示纯色背景
            zIndex: 99999,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            // 添加GPU加速
            willChange: 'transform',
            transform: 'translateZ(0)'
        }}>
        
        {/* Loading状态 */}
        {isStarCityLoading && (
            <div style={{
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
                color: 'white',
                fontSize: '18px',
                textAlign: 'center'
            }}>
                <div>🌟</div>
                <div>加载中...</div>
            </div>
        )}
        
        {/* 只在非loading状态显示内容 */}
        {!isStarCityLoading && (
            <>
                {/* 原有内容 */}
            </>
        )}
    </div>
)}
