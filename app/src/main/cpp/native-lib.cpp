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

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeDestroyFace(JNIEnv *env, jclass clazz,
                                                                     jlong thiz) {
    // TODO: implement nativeDestroyFace()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeStart(JNIEnv *env, jclass clazz,
                                                               jlong thiz) {
    // TODO: implement nativeStart()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeStop(JNIEnv *env, jclass clazz,
                                                              jlong thiz) {
    // TODO: implement nativeStop()
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_openglcamerademo_face_FaceTracker_nativeDetect(JNIEnv *env, jclass clazz,
                                                                jlong thiz, jbyteArray input_image,
                                                                jint width, jint height,
                                                                jint rotation_degrees) {
    // TODO: implement nativeDetect()
}