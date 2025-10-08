#include <jni.h>
#include <vector>
#include <cstring>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

using namespace cv;

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_jni_NativeBridge_processFrame(
        JNIEnv* env,
        jobject /* thiz */,
        jbyteArray input,
        jint width,
        jint height) {
    const int w = static_cast<int>(width);
    const int h = static_cast<int>(height);

    // Expect input as RGBA
    jsize inLen = env->GetArrayLength(input);
    std::vector<jbyte> inBuf(inLen);
    env->GetByteArrayRegion(input, 0, inLen, inBuf.data());

    Mat rgba(h, w, CV_8UC4, inBuf.data());
    Mat gray; cvtColor(rgba, gray, COLOR_RGBA2GRAY);
    Mat edges; Canny(gray, edges, 80, 150);
    Mat out(h, w, CV_8UC4, Scalar(0,0,0,255));
    // Copy edges to all channels (white edges)
    for (int y = 0; y < h; ++y) {
        const uchar* e = edges.ptr<uchar>(y);
        Vec4b* o = out.ptr<Vec4b>(y);
        for (int x = 0; x < w; ++x) {
            uchar v = e[x];
            o[x] = Vec4b(v, v, v, 255);
        }
    }

    jsize outLen = w * h * 4;
    jbyteArray result = env->NewByteArray(outLen);
    env->SetByteArrayRegion(result, 0, outLen, reinterpret_cast<jbyte*>(out.data));
    return result;
}


