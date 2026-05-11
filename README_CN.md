# Wolvic VR 浏览器 (华为VR Glass适配版)

本项目基于 [Wolvic](https://github.com/Igalia/wolvic) 源码修改，增加了VR180°视频俯仰角(Pitch)调整功能，适配华为VR Glass CV10设备。

## 新增功能

### VR180° Pitch调整功能

播放VR180°视频时，可以通过调整俯仰角实现躺着观看：
- 正值(+90°)：向上看 - 适合仰卧观看
- 负值(-90°)：向下看 - 适合俯卧观看
- 0°：正常观看角度

**使用方法：**
1. 打开VR180°视频
2. 选择投影模式（180、180 Stereo LR、180 Stereo TB）
3. 点击媒体控制栏中的Pitch按钮（向上箭头图标）
4. 拖动垂直滑块调整角度

## 构建说明

### 前提条件

1. Android SDK (API 35)
2. JDK 17
3. Gradle 8.x

### 构建华为VR Glass版本

华为VR Glass版本需要华为VisionGlass SDK，请按以下步骤准备：

1. 从华为开发者网站下载VisionGlass SDK
   - 访问: https://developer.huawei.com/consumer/cn/
   - 下载 `com.huawei.usblib.VisionGlass` SDK

2. 将SDK文件放入项目目录：
   ```
   third_party/hvr/
   ```

3. 创建 `app/agconnect-services.json` 文件（华为AGConnect配置）

4. 构建APK：
   ```bash
   ./gradlew assembleArm64VisionglassGeckoGenericDebug
   ```

### 构建无VR SDK版本（用于测试）

```bash
./gradlew assembleArm64NoapiGeckoGenericDebug
```

## 项目结构

主要修改文件：

- `app/src/main/cpp/VRVideo.cpp` - Pitch旋转矩阵实现
- `app/src/main/cpp/VRVideo.h` - Pitch方法声明
- `app/src/main/cpp/BrowserWorld.cpp` - JNI接口实现
- `app/src/common/shared/com/igalia/wolvic/ui/widgets/MediaControlsWidget.java` - UI控制逻辑
- `app/src/main/res/layout/media_controls.xml` - 添加Pitch按钮
- `app/src/common/shared/com/igalia/wolvic/ui/views/PitchControl.java` - Pitch滑块控件

## 设备兼容性

- 华为 Mate 40 Pro + 华为 VR Glass CV10
- HarmonyOS 环境
- 3DoF追踪模式

## 许可证

MPL 2.0 License - 详见原始Wolvic项目许可证

## 参考项目

- [Wolvic](https://github.com/Igalia/wolvic) - 原始VR浏览器
- [JellyQuest](https://github.com/JoeCotellese/JellyQuest) - Jellyfin VR播放器参考
