import React from 'react'
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
    if (!show) return null

    return (
        <div className="decree-modal-overlay" onClick={onClose}>
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
                                <div className="decree-info">
                                    <div className="decree-name">{decree.name}</div>
                                    <div className="decree-description">{decree.description}</div>
                                    {decree.active && decree.issuedAt && (
                                        <div className="decree-time">
                                            颁布时间: {new Date(decree.issuedAt).toLocaleString('zh-CN')}
                                        </div>
                                    )}
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

