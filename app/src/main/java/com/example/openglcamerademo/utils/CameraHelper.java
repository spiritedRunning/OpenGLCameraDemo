package com.example.openglcamerademo.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.openglcamerademo.face.Face;
import com.example.openglcamerademo.face.FaceTracker;
import com.example.openglcamerademo.ui.ImageUtils;

public class CameraHelper implements ImageAnalysis.Analyzer, LifecycleObserver {

    private HandlerThread handlerThread;
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.BACK;
    private Preview.OnPreviewOutputUpdateListener listener;

    private FaceTracker faceTracker;
    private Face face;

    public CameraHelper(LifecycleOwner lifecycleOwner, Preview.OnPreviewOutputUpdateListener listener) {
        this.listener = listener;
        lifecycleOwner.getLifecycle().addObserver(this);

        handlerThread = new HandlerThread("Analyze-Thread");
        handlerThread.start();
        CameraX.bindToLifecycle(lifecycleOwner, getPreView(), getImageAnalysis());
    }

    private Preview getPreView() {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(new Size(640, 480))
                .setLensFacing(currentFacing)
                .build();

        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(listener);
        return preview;
    }

    private ImageAnalysis getImageAnalysis() {
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setCallbackHandler(new Handler(handlerThread.getLooper()))
                .setLensFacing(currentFacing)
                .setTargetResolution(new Size(640, 480))
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        imageAnalysis.setAnalyzer(this);
        return imageAnalysis;

    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        byte[] bytes = ImageUtils.getBytes(image, rotationDegrees, image.getWidth(), image.getHeight());

        synchronized (this) {
            face = faceTracker.detect(bytes, image.getWidth(), image.getHeight(), rotationDegrees);
        }
    }

    public synchronized Face getFace() {
        return face;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        faceTracker = new FaceTracker("/sdcard/lbpcascade_frontalface.xml", "/sdcard/pd_2_00_pts5.dat");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner owner) {
        faceTracker.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {
        faceTracker.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        faceTracker.release();
        owner.getLifecycle().removeObserver(this);
    }


}
