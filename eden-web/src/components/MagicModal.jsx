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
    onCast,
    userEnergy
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
                
                {/* 精力信息 */}
                {userEnergy && (
                    <div style={{
                        marginBottom: '15px',
                        padding: '12px',
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        borderRadius: '8px',
                        textAlign: 'center',
                        color: 'white'
                    }}>
                        <div style={{fontSize: '14px', marginBottom: '5px', opacity: 0.9}}>
                            ⚡ 当前精力
                        </div>
                        <div style={{fontSize: '28px', fontWeight: 'bold', letterSpacing: '2px'}}>
                            {userEnergy.energy} / {userEnergy.maxEnergy}
                        </div>
                        <div style={{fontSize: '11px', marginTop: '5px', opacity: 0.8}}>
                            每天凌晨12点恢复到满值
                        </div>
                    </div>
                )}
                
                {loading ? (
                    <div className="magic-loading">加载中...</div>
                ) : (
                    <div className="magic-list">
                        {magics.map((magic) => {
                            // 检查精力是否足够
                            const hasEnoughEnergy = !userEnergy || !magic.energyCost || userEnergy.energy >= magic.energyCost
                            
                            return (
                            <div 
                                key={magic.code} 
                                className={`magic-item ${!hasEnoughEnergy ? 'exhausted' : ''}`}
                            >
                                {/* 标题行：魔法名称 + 按钮 */}
                                <div className="magic-header">
                                    <div 
                                        className="magic-name-clickable"
                                        onClick={() => toggleExpand(magic.code)}
                                    >
                                        <span className="magic-expand-icon">
                                            {expandedCode === magic.code ? '▼' : '▶'}
                                        </span>
                                        <span className="magic-name">{magic.name}</span>
                                        {magic.energyCost && (
                                            <span className="magic-uses" style={{
                                                color: hasEnoughEnergy ? '#FFD700' : '#FF6B6B'
                                            }}>
                                                ⚡{magic.energyCost}
                                            </span>
                                        )}
                                    </div>
                                    <div className="magic-actions">
                                        <button
                                            className="magic-btn cast-btn"
                                            onClick={() => onCast(magic.code)}
                                            disabled={castingCode === magic.code || !hasEnoughEnergy}
                                        >
                                            {castingCode === magic.code ? '施展中...' : !hasEnoughEnergy ? '精力不足' : '施展魔法'}
                                        </button>
                                    </div>
                                </div>

                                {/* 展开的详情 */}
                                {expandedCode === magic.code && (
                                    <div className="magic-details">
                                        <div className="magic-description">{magic.description}</div>
                                        <div className="magic-info">
                                            {magic.energyCost && (
                                                <div style={{
                                                    color: hasEnoughEnergy ? '#FFD700' : '#FF6B6B',
                                                    fontWeight: 'bold',
                                                    fontSize: '16px'
                                                }}>
                                                    ⚡ 精力消耗: {magic.energyCost}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                            )
                        })}
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

