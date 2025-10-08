# jni module

Android library with C++/CMake using OpenCV to run Canny edge detection on RGBA frames.

Setup:
1. Download and extract the OpenCV Android SDK.
2. Set env var `OPENCV_ANDROID_SDK` to the SDK root.
3. Sync and build (`native-lib`).

Exports JNI:
- `NativeBridge.processFrame(input: ByteArray, width: Int, height: Int): ByteArray`
