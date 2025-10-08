cmake_minimum_required(VERSION 3.22.1)
project(native_lib)

set(CMAKE_CXX_STANDARD 17)

add_library(native-lib SHARED native-lib.cpp)

find_library(log-lib log)

# OpenCV: expect environment variable OPENCV_ANDROID_SDK
if(DEFINED ENV{OPENCV_ANDROID_SDK})
    set(OpenCV_DIR $ENV{OPENCV_ANDROID_SDK}/sdk/native/jni)
    find_package(OpenCV REQUIRED)
    target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})
    target_link_libraries(native-lib ${OpenCV_LIBS})
else()
    message(WARNING "OPENCV_ANDROID_SDK is not set. Building without OpenCV will fail.")
endif()

target_link_libraries(
    native-lib
    ${log-lib}
    android
    jnigraphics
)


