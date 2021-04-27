//
// Created by Zach on 2021/4/26.
//

#include <jni.h>
#include <string>

#include "FaceTrack.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeCreateFace(JNIEnv *env, jclass clazz,
                                                                    jstring facemodel_,
                                                                    jstring landmarkermodel_) {

    const char *facemodel = env->GetStringUTFChars(facemodel_, 0);
    const char *landmarkermodel = env->GetStringUTFChars(landmarkermodel_, 0);

    FaceTrack *faceTrack = new FaceTrack(facemodel, landmarkermodel);

    env->ReleaseStringUTFChars(facemodel_, facemodel);
    env->ReleaseStringUTFChars(landmarkermodel_, landmarkermodel);

    return (jlong) faceTrack;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeDestroyFace(JNIEnv *env, jclass clazz,
                                                                     jlong thiz) {
    if (thiz != 0) {
        FaceTrack *tracker = reinterpret_cast<FaceTrack *>(thiz);
        tracker->stop();
        delete tracker;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeStart(JNIEnv *env, jclass clazz,
                                                               jlong thiz) {
    if (thiz != 0) {
        FaceTrack *tracker = reinterpret_cast<FaceTrack *>(thiz);
        tracker->run();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeStop(JNIEnv *env, jclass clazz,
                                                              jlong thiz) {
    if (thiz != 0) {
        FaceTrack *tracker = reinterpret_cast<FaceTrack *>(thiz);
        tracker->stop();
    }
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeDetect(JNIEnv *env, jclass clazz,
                                                                jlong thiz, jbyteArray input_image,
                                                                jint width, jint height,
                                                                jint rotation_degrees) {
    if (thiz == 0) {
        return 0;
    }

    FaceTrack *tracker = reinterpret_cast<FaceTrack *>(thiz);
    jbyte *inputImage = env->GetByteArrayElements(input_image, 0);

    // I420
    Mat src(height * 3 / 2, width, CV_8UC1, inputImage);
    // 转为RGBA
    cvtColor(src, src, CV_YUV2RGBA_I420);

    if (rotation_degrees == 90) {
        rotate(src, src, ROTATE_90_CLOCKWISE);
    } else if (rotation_degrees == 270) {
        rotate(src, src, ROTATE_90_COUNTERCLOCKWISE);
    }

    Mat gray;
    cvtColor(src, gray, CV_RGBA2GRAY);
    equalizeHist(gray, gray);


    cv::Rect face;
    vector<SeetaPointF> points;
    tracker->process(gray, face, points);

    int w = src.cols;
    int h = src.rows;
    gray.release();
    src.release();
    env->ReleaseByteArrayElements(input_image, inputImage, 0);

    if (!face.empty() && !points.empty()) {
        jclass cls = env->FindClass("com/example/openglcamerademo/face/Face");
        jmethodID construct = env->GetMethodID(cls, "<init>", "(IIIIIIFFFF)V");
        SeetaPointF left = points[0];
        SeetaPointF right = points[1];
        jobject obj = env->NewObject(cls, construct, face.width, face.height, w, h, face.x, face.y, left.x, left.y, right.x, right.y);
        return obj;
    }

    return 0;
}