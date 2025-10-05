#!/bin/bash

echo "=== 头像裁剪调试指南 ==="

echo "1. 调试步骤："
echo "   a) 打开浏览器开发者工具 (F12)"
echo "   b) 切换到 Console 标签页"
echo "   c) 上传图片并进行裁剪"
echo "   d) 点击'保存头像'按钮"
echo "   e) 查看控制台输出的调试信息"

echo -e "\n2. 需要关注的调试信息："
echo "   前端 Console 输出："
echo "   - 显示尺寸: XXXx XXX"
echo "   - 原始尺寸: XXXx XXX"
echo "   - 缩放比例: X.XX x X.XX"
echo "   - 显示裁剪区域: {x, y, width, height}"
echo "   - 实际裁剪区域: {x, y, width, height}"

echo -e "\n   后端日志输出："
echo "   - 原始图片尺寸: XXXxXXX"
echo "   - 裁剪参数: x=XX, y=XX, width=XX, height=XX"

echo -e "\n3. 常见问题和解决方案："
echo "   问题1: 前端显示尺寸和原始尺寸差异很大"
echo "   解决: 这是正常的，前端会自动缩放显示"
echo ""
echo "   问题2: 裁剪结果偏移"
echo "   解决: 检查缩放比例计算是否正确"
echo ""
echo "   问题3: 裁剪区域超出图片边界"
echo "   解决: 检查坐标转换逻辑"

echo -e "\n4. 如果仍有问题，请提供："
echo "   - 前端控制台的完整输出"
echo "   - 后端日志的相关信息"
echo "   - 原始图片的尺寸"
echo "   - 期望裁剪的区域描述"

echo -e "\n=== 调试指南完成 ==="
