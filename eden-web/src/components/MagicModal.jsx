import React, { useState } from 'react'
import './MagicModal.css'

/**
 * 魔法管理弹窗组件
 * 仅秦小淮可见和使用
 */
const MagicModal = ({ 
    show, 
    onClose, 
    magics, 
    loading, 
    castingCode, 
    onCast 
}) => {
    const [expandedCode, setExpandedCode] = useState(null)

    if (!show) return null

    const toggleExpand = (code) => {
        setExpandedCode(expandedCode === code ? null : code)
    }

    return (
        <div className="magic-modal-overlay force-landscape" onClick={onClose}>
            <div 
                className="magic-modal-content" 
                onClick={(e) => e.stopPropagation()}
            >
                <h2 className="magic-modal-title">✨ 魔法管理</h2>
                
                {loading ? (
                    <div className="magic-loading">加载中...</div>
                ) : (
                    <div className="magic-list">
                        {magics.map((magic) => (
                            <div 
                                key={magic.code} 
                                className={`magic-item ${magic.remainingUses === 0 ? 'exhausted' : ''}`}
                            >
                                {/* 标题行：魔法名称 + 次数 + 按钮 */}
                                <div className="magic-header">
                                    <div 
                                        className="magic-name-clickable"
                                        onClick={() => toggleExpand(magic.code)}
                                    >
                                        <span className="magic-expand-icon">
                                            {expandedCode === magic.code ? '▼' : '▶'}
                                        </span>
                                        <span className="magic-name">{magic.name}</span>
                                        <span className="magic-uses">
                                            ({magic.remainingUses}/{magic.dailyLimit})
                                        </span>
                                    </div>
                                    <div className="magic-actions">
                                        <button
                                            className="magic-btn cast-btn"
                                            onClick={() => onCast(magic.code)}
                                            disabled={castingCode === magic.code || magic.remainingUses === 0}
                                        >
                                            {castingCode === magic.code ? '施展中...' : magic.remainingUses === 0 ? '已用完' : '施展魔法'}
                                        </button>
                                    </div>
                                </div>

                                {/* 展开的详情 */}
                                {expandedCode === magic.code && (
                                    <div className="magic-details">
                                        <div className="magic-description">{magic.description}</div>
                                        <div className="magic-info">
                                            <div>每日次数: {magic.dailyLimit}</div>
                                            <div>剩余次数: {magic.remainingUses}</div>
                                            {magic.lastRefreshAt && (
                                                <div>
                                                    上次刷新: {new Date(magic.lastRefreshAt).toLocaleString('zh-CN')}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                        ))}
                        {magics.length === 0 && (
                            <div className="magic-empty">暂无魔法</div>
                        )}
                    </div>
                )}
                
                <button className="magic-close-btn" onClick={onClose}>
                    关闭
                </button>
            </div>
        </div>
    )
}

export default MagicModal

