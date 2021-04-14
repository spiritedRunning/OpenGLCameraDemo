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
import java.nio.ByteBuffer;

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

        mHandler.post(() -> {
            eglEnv = new EGLEnv(mContext, mGlContext, mSurface, mWidth, mHeight);
            isStart = true;
        });
    }

    public void fireFrame(final int textureId, final long timestamp) {
        if (!isStart) {
            return;
        }

        mHandler.post(() -> {
            eglEnv.draw(textureId, timestamp);
            codec(false);
        });
    }

    private void codec(boolean endOfStream) {
        if (endOfStream) {
            mMediaCodec.signalEndOfInputStream();
        }

        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int encoderStatus = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);

            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {  // 需要更多数据
                if (endOfStream) {  // todo 这里有疑问
                    break;
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) { // 输出格式发生改变， 总会调用一次
                MediaFormat newFormat = mMediaCodec.getOutputFormat();
                track = mMuxer.addTrack(newFormat);
                mMuxer.start();
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // 可忽略
            } else {
                // 调整时间戳
                bufferInfo.presentationTimeUs = (long) (bufferInfo.presentationTimeUs / mSpeed);
                // 解决可能出现的异常
                if (bufferInfo.presentationTimeUs <= mLastTimeStamp) {
                    bufferInfo.presentationTimeUs = (long) (mLastTimeStamp + 1_000_000 / 25 / mSpeed);
                }
                mLastTimeStamp = bufferInfo.presentationTimeUs;

                ByteBuffer encodeData = mMediaCodec.getOutputBuffer(encoderStatus);
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) { // 配置信息丢弃
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0 && encodeData != null) {
                    encodeData.position(bufferInfo.offset);  // 读出编码后的数据
                    encodeData.limit(bufferInfo.offset + bufferInfo.size);   // 设置能读数据的总长度
                    mMuxer.writeSampleData(track, encodeData, bufferInfo);  // 写出到mp4
                }

                // 释放这个缓冲区，后续可以存放新的编码后数据
                mMediaCodec.releaseOutputBuffer(encoderStatus, false);

                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) { // 收到结束信号
                    break;
                }
            }
        }
    }

    public void stop() {
        isStart = false;

        mHandler.post(() -> {
            codec(true);
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;

            mMuxer.stop();
            mMuxer.release();
            mMuxer = null;

            eglEnv.release();
            eglEnv = null;

            mSurface = null;
            mHandler.getLooper().quitSafely();
            mHandler = null;
        });
    }
}
