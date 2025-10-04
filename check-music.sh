#!/bin/bash

# 星星城背景音乐部署脚本

echo "正在检查星星城背景音乐文件..."

AUDIO_DIR="eden-web/public/audio"
MP3_FILE="$AUDIO_DIR/star-city-bg.mp3"
OGG_FILE="$AUDIO_DIR/star-city-bg.ogg"

# 检查音频目录是否存在
if [ ! -d "$AUDIO_DIR" ]; then
    echo "创建音频目录: $AUDIO_DIR"
    mkdir -p "$AUDIO_DIR"
fi

# 检查音频文件
if [ ! -f "$MP3_FILE" ]; then
    echo "警告: 未找到星星城背景音乐文件 $MP3_FILE"
    echo "请将背景音乐文件命名为 star-city-bg.mp3 并放置在 $AUDIO_DIR 目录下"
    echo ""
    echo "音乐文件要求:"
    echo "- 格式: MP3 或 OGG"
    echo "- 大小: 建议 2MB 以内"
    echo "- 风格: 轻柔、梦幻的背景音乐"
    echo "- 循环: 能够无缝循环播放"
    echo ""
else
    echo "✓ 找到背景音乐文件: $MP3_FILE"
fi

if [ ! -f "$OGG_FILE" ]; then
    echo "提示: 可选择添加 OGG 格式文件 $OGG_FILE 以提高浏览器兼容性"
else
    echo "✓ 找到 OGG 格式文件: $OGG_FILE"
fi

echo "背景音乐检查完成！"
