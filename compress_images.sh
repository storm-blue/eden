#!/bin/bash

# 图片压缩脚本
echo "开始压缩星星城图片..."

# 创建备份目录
mkdir -p eden-web/public/picture/backup
cp eden-web/public/picture/*.png eden-web/public/picture/backup/

# 压缩所有PNG图片
for file in eden-web/public/picture/lv*.png; do
    if [ -f "$file" ]; then
        echo "压缩 $file ..."
        filename=$(basename "$file")
        
        # 使用sips压缩图片，保持原分辨率但降低质量
        # 先转换为JPEG格式以减小文件大小，质量设为85%
        sips -s format jpeg -s formatOptions 85 "$file" --out "eden-web/public/picture/${filename%.png}.jpg"
        
        # 删除原PNG文件
        rm "$file"
        
        echo "✅ ${filename} 已压缩为 ${filename%.png}.jpg"
    fi
done

echo "压缩完成！查看压缩后的文件大小："
ls -lh eden-web/public/picture/*.jpg

echo ""
echo "备份的原始文件保存在 eden-web/public/picture/backup/ 目录中"
