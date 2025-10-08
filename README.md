# Multi-Module Android + Web Scaffold

This repository contains a minimal scaffold with four parts:

- `app` — Android app (Kotlin) using Camera2, JNI bridge, and an OpenGL ES 2.0 renderer.
- `jni` — Android library with C++/CMake using OpenCV to run Canny edge detection.
- `gl` — Android library with OpenGL ES 2.0 renderer for displaying RGBA textures.
- `web` — TypeScript viewer that draws a static processed image and overlays FPS/resolution.

Android data flow: Camera2 → JNI (OpenCV Canny) → GL texture → Screen.

Refer to each module's README for setup and usage.
