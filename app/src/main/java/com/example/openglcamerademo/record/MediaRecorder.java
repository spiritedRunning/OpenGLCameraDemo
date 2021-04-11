package com.example.openglcamerademo.record;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.io.IOException;

public class MediaRecorder {
    private final int mWidth, mHeight;
    private final String mPath;
    private final Context mContext;

    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private EGLContext mGlContext;
    private EGLEnv eglEnv;
    private MediaMuxer mMuxer;

    private Handler mHandler;
    private boolean isStart;
    private int track;
    private float mSpeed;
    private long mLastTimeStamp;


    public MediaRecorder(Context context, String path, EGLContext glContext, int width, int height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }

    public void start(float speed) throws IOException {
        mSpeed = speed;

        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
        // 颜色空间， 从surface中获取
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);   // 码率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);   // 帧率
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);    // 关键帧间隔

        mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mSurface = mMediaCodec.createInputSurface();

        mMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        mMediaCodec.start();

        HandlerThread handlerThread = new HandlerThread("mediacodec-thread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eglEnv = new EGLEnv(mContext, mGlContext, mSurface, mWidth, mHeight);
                isStart = true;
            }
        });
    }

    public void fireFrame(final int textureId, final long timestamp) {
        if (!isStart) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eglEnv.draw(textureId, timestamp);
                codec(false);
            }
        });
    }

    private void codec(boolean endOfStream) {
        if (endOfStream) {
            mMediaCodec.signalEndOfInputStream();
        }

        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int encoderStatus = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);

            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                if (endOfStream) {
                    break;
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // 输出格式发生改变， 第一次总会调用
                MediaFormat newFormat = mMediaCodec.getOutputFormat();

            }
        }
    }
}
