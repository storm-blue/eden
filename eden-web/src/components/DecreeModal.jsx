import React, { useState } from 'react'
import './DecreeModal.css'

/**
 * 命令管理弹窗组件
 * 仅秦小淮可见和使用
 */
const DecreeModal = ({ 
    show, 
    onClose, 
    decrees, 
    loading, 
    operatingCode, 
    onIssue, 
    onCancel 
}) => {
    const [expandedCode, setExpandedCode] = useState(null)

    if (!show) return null

    const toggleExpand = (code) => {
        setExpandedCode(expandedCode === code ? null : code)
    }

    return (
        <div className="decree-modal-overlay force-landscape" onClick={onClose}>
            <div 
                className="decree-modal-content" 
                onClick={(e) => e.stopPropagation()}
            >
                <h2 className="decree-modal-title">📜 命令管理</h2>
                
                {loading ? (
                    <div className="decree-loading">加载中...</div>
                ) : (
                    <div className="decree-list">
                        {decrees.map((decree) => (
                            <div 
                                key={decree.code} 
                                className={`decree-item ${decree.active ? 'active' : ''}`}
                            >
                                {/* 标题行：命令名称 + 按钮 */}
                                <div className="decree-header">
                                    <div 
                                        className="decree-name-clickable"
                                        onClick={() => toggleExpand(decree.code)}
                                    >
                                        <span className="decree-expand-icon">
                                            {expandedCode === decree.code ? '▼' : '▶'}
                                        </span>
                                        <span className="decree-name">{decree.name}</span>
                                    </div>
                                    <div className="decree-actions">
                                        {decree.active ? (
                                            <button
                                                className="decree-btn cancel-btn"
                                                onClick={() => onCancel(decree.code)}
                                                disabled={operatingCode === decree.code}
                                            >
                                                {operatingCode === decree.code ? '处理中...' : '取消命令'}
                                            </button>
                                        ) : (
                                            <button
                                                className="decree-btn issue-btn"
                                                onClick={() => onIssue(decree.code)}
                                                disabled={operatingCode === decree.code}
                                            >
                                                {operatingCode === decree.code ? '处理中...' : '颁布命令'}
                                            </button>
                                        )}
                                    </div>
                                </div>

                                {/* 展开的详情 */}
                                {expandedCode === decree.code && (
                                    <div className="decree-details">
                                        <div className="decree-description">{decree.description}</div>
                                        {decree.active && decree.issuedAt && (
                                            <div className="decree-time">
                                                颁布时间: {new Date(decree.issuedAt).toLocaleString('zh-CN')}
                                            </div>
                                        )}
                                    </div>
                                )}
                            </div>
                        ))}
                        {decrees.length === 0 && (
                            <div className="decree-empty">暂无命令</div>
                        )}
                    </div>
                )}
                
                <button className="decree-close-btn" onClick={onClose}>
                    关闭
                </button>
            </div>
        </div>
    )
}

export default DecreeModal

