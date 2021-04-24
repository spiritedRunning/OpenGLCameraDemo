package com.example.openglcamerademo.face;

public class FaceTracker {
    static {
        System.loadLibrary("FaceTracker");
    }

    private long mNativeFace = 0;

    public FaceTracker(String faceModel, String landMarkerModel) {
        mNativeFace = nativeCreateFace(faceModel, landMarkerModel);
    }

    public void release() {
        nativeDestroyFace(mNativeFace);
        mNativeFace = 0;
    }

    public void start() {
        nativeStart(mNativeFace);
    }

    public void stop() {
        nativeStop(mNativeFace);
    }

    public Face detect(byte[] inputImage, int width, int height, int rotationDegrees) {
        return nativeDetect(mNativeFace, inputImage, width, height, rotationDegrees);
    }



    private static native long nativeCreateFace(String FaceModel, String landMarkerModel);

    private static native void nativeDestroyFace(long thiz);

    private static native void nativeStart(long thiz);

    private static native void nativeStop(long thiz);

    private static native Face nativeDetect(long thiz, byte[] inputImage, int width, int height, int rotationDegrees);
}
