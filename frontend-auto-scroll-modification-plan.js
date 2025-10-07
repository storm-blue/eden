// 前端修改方案：在 LuckyWheel.jsx 中添加自动滚动到底部的功能

// 1. 在 fetchEventHistory 函数中添加滚动逻辑
const fetchEventHistory = async (residence) => {
    if (!residence) return
    
    setLoadingEventHistory(true)
    try {
        const response = await fetch(`/api/residence-event-history/${residence}`)
        if (response.ok) {
            const data = await response.json()
            if (data.success) {
                setEventHistory(data.history || [])
                // 添加延时滚动到底部，确保DOM更新完成
                setTimeout(() => {
                    const scrollContainer = document.querySelector('.residence-event-scroll')
                    if (scrollContainer) {
                        scrollContainer.scrollTop = scrollContainer.scrollHeight
                    }
                }, 100)
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

// 2. 也可以使用 useEffect 监听 eventHistory 变化
useEffect(() => {
    if (showEventHistory && eventHistory.length > 0) {
        // 滚动到底部
        setTimeout(() => {
            const scrollContainer = document.querySelector('.residence-event-scroll')
            if (scrollContainer) {
                scrollContainer.scrollTop = scrollContainer.scrollHeight
            }
        }, 100)
    }
}, [eventHistory, showEventHistory])

// 3. 为了更好的用户体验，可以添加平滑滚动
const scrollToBottom = () => {
    const scrollContainer = document.querySelector('.residence-event-scroll')
    if (scrollContainer) {
        scrollContainer.scrollTo({
            top: scrollContainer.scrollHeight,
            behavior: 'smooth'
        })
    }
}
