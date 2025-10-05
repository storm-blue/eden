import React, { useState, useRef, useCallback } from 'react';
import ReactCrop from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';
import './AvatarCrop.css';

const AvatarCrop = ({ isOpen, onClose, onSave, userName }) => {
    const [imageSrc, setImageSrc] = useState(null);
    const [crop, setCrop] = useState({
        unit: '%',
        width: 50,
        height: 50,
        x: 25,
        y: 25,
    });
    const [completedCrop, setCompletedCrop] = useState(null);
    const [uploading, setUploading] = useState(false);
    const [selectedFile, setSelectedFile] = useState(null); // 新增：存储选择的文件
    const imgRef = useRef(null);
    const previewCanvasRef = useRef(null);
    const fileInputRef = useRef(null);

    // 选择文件
    const onSelectFile = useCallback((e) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            setSelectedFile(file); // 存储选择的文件
            
            const reader = new FileReader();
            reader.addEventListener('load', () => setImageSrc(reader.result));
            reader.readAsDataURL(file);
        }
    }, []);

    // 图片加载完成
    const onImageLoad = useCallback((e) => {
        if (e.currentTarget) {
            imgRef.current = e.currentTarget;
            
            // 设置默认裁剪区域（正方形，居中）
            const { width, height } = e.currentTarget;
            const size = Math.min(width, height);
            const x = (width - size) / 2;
            const y = (height - size) / 2;
            
            const newCrop = {
                unit: 'px',
                width: size,
                height: size,
                x: x,
                y: y,
            };
            setCrop(newCrop);
            setCompletedCrop(newCrop);
        }
    }, []);

    // 生成预览
    const generatePreview = useCallback((image, crop, canvas) => {
        if (!crop || !canvas || !image) {
            return;
        }

        const scaleX = image.naturalWidth / image.width;
        const scaleY = image.naturalHeight / image.height;
        const ctx = canvas.getContext('2d');
        const pixelRatio = window.devicePixelRatio;

        canvas.width = 200 * pixelRatio;
        canvas.height = 200 * pixelRatio;

        ctx.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
        ctx.imageSmoothingQuality = 'high';

        ctx.drawImage(
            image,
            crop.x * scaleX,
            crop.y * scaleY,
            crop.width * scaleX,
            crop.height * scaleY,
            0,
            0,
            200,
            200,
        );
    }, []);

    // 裁剪完成
    const onCropComplete = useCallback((crop) => {
        setCompletedCrop(crop);
        if (imgRef.current && previewCanvasRef.current && crop.width && crop.height) {
            generatePreview(imgRef.current, crop, previewCanvasRef.current);
        }
    }, [generatePreview]);

    // 关闭弹窗
    const handleClose = useCallback(() => {
        setImageSrc(null);
        setSelectedFile(null); // 清理选择的文件
        setCrop({
            unit: '%',
            width: 50,
            height: 50,
            x: 25,
            y: 25,
        });
        setCompletedCrop(null);
        setUploading(false);
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
        onClose();
    }, [onClose]);

    // 保存头像
    const handleSave = useCallback(async () => {
        if (!completedCrop || !imgRef.current || !previewCanvasRef.current) {
            alert('请先选择并裁剪图片');
            return;
        }

        if (!selectedFile) {
            alert('请重新选择文件');
            return;
        }

        setUploading(true);
        try {
            // 获取图片的实际尺寸和显示尺寸
            const img = imgRef.current;
            const naturalWidth = img.naturalWidth;
            const naturalHeight = img.naturalHeight;
            const displayWidth = img.width;
            const displayHeight = img.height;
            
            // 计算缩放比例
            const scaleX = naturalWidth / displayWidth;
            const scaleY = naturalHeight / displayHeight;
            
            // 转换裁剪坐标到原始图片坐标系
            const actualX = Math.round(completedCrop.x * scaleX);
            const actualY = Math.round(completedCrop.y * scaleY);
            const actualWidth = Math.round(completedCrop.width * scaleX);
            const actualHeight = Math.round(completedCrop.height * scaleY);
            
            console.log('显示尺寸:', displayWidth, 'x', displayHeight);
            console.log('原始尺寸:', naturalWidth, 'x', naturalHeight);
            console.log('缩放比例:', scaleX, 'x', scaleY);
            console.log('显示裁剪区域:', completedCrop);
            console.log('实际裁剪区域:', { x: actualX, y: actualY, width: actualWidth, height: actualHeight });

            const formData = new FormData();
            formData.append('file', selectedFile);
            formData.append('userId', userName);
            formData.append('x', actualX);
            formData.append('y', actualY);
            formData.append('width', actualWidth);
            formData.append('height', actualHeight);

            const response = await fetch('/api/avatar/upload', {
                method: 'POST',
                body: formData,
            });

            const result = await response.json();
            if (result.success) {
                onSave(result.data.avatarPath);
                handleClose();
            } else {
                alert(result.message || '上传失败');
            }
        } catch (error) {
            console.error('头像上传失败:', error);
            alert('头像上传失败: ' + error.message);
        } finally {
            setUploading(false);
        }
    }, [completedCrop, selectedFile, userName, onSave, handleClose]);

    if (!isOpen) return null;

    return (
        <div className="avatar-crop-overlay">
            <div className="avatar-crop-modal">
                <div className="avatar-crop-header">
                    <h3>上传头像</h3>
                    <button className="avatar-crop-close" onClick={handleClose}>
                        ×
                    </button>
                </div>

                <div className="avatar-crop-content">
                    {!imageSrc ? (
                        <div className="avatar-crop-upload">
                            <input
                                ref={fileInputRef}
                                type="file"
                                accept="image/*"
                                onChange={onSelectFile}
                                style={{ display: 'none' }}
                            />
                            <div 
                                className="avatar-crop-upload-area"
                                onClick={() => fileInputRef.current?.click()}
                            >
                                <div className="avatar-crop-upload-icon">📷</div>
                                <div className="avatar-crop-upload-text">点击选择图片</div>
                                <div className="avatar-crop-upload-hint">支持 JPG、PNG 格式，最大 5MB</div>
                            </div>
                        </div>
                    ) : (
                        <div className="avatar-crop-editor">
                            <div className="avatar-crop-main">
                                <div className="avatar-crop-image-container">
                                    <ReactCrop
                                        crop={crop}
                                        onChange={(_, percentCrop) => setCrop(percentCrop)}
                                        onComplete={(c) => onCropComplete(c)}
                                        aspect={1}
                                        minWidth={50}
                                        minHeight={50}
                                    >
                                        <img
                                            ref={imgRef}
                                            alt="Crop me"
                                            src={imageSrc}
                                            onLoad={onImageLoad}
                                            style={{ maxHeight: '400px', maxWidth: '100%' }}
                                        />
                                    </ReactCrop>
                                </div>
                                
                                <div className="avatar-crop-preview">
                                    <div className="avatar-crop-preview-title">预览</div>
                                    <div className="avatar-crop-preview-container">
                                        <canvas
                                            ref={previewCanvasRef}
                                            style={{
                                                width: '120px',
                                                height: '120px',
                                                border: '1px solid #ddd',
                                                borderRadius: '50%',
                                                objectFit: 'cover',
                                            }}
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="avatar-crop-actions">
                                <button 
                                    className="avatar-crop-btn avatar-crop-btn-secondary"
                                    onClick={() => fileInputRef.current?.click()}
                                    disabled={uploading}
                                >
                                    重新选择
                                </button>
                                <button 
                                    className="avatar-crop-btn avatar-crop-btn-primary"
                                    onClick={handleSave}
                                    disabled={uploading}
                                >
                                    {uploading ? '上传中...' : '保存头像'}
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default AvatarCrop;
