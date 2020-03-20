package com.vincent.videocompressor

import android.os.AsyncTask

/**
 * Created by Vincent Woo
 * Date: 2017/8/16
 * Time: 15:15
 */
object VideoCompressor {
    private val TAG = VideoCompressor::class.java.simpleName
    fun compressVideoHigh(
        srcPath: String?,
        destPath: String?,
        listener: CompressListener?
    ): VideoCompressTask {
        val task =
            VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_HIGH)
        task.execute(srcPath, destPath)
        return task
    }

    fun compressVideoMedium(
        srcPath: String?,
        destPath: String?,
        listener: CompressListener?
    ): VideoCompressTask {
        val task =
            VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_MEDIUM)
        task.execute(srcPath, destPath)
        return task
    }

    fun compressVideoLow(
        srcPath: String?,
        destPath: String?,
        listener: CompressListener?
    ): VideoCompressTask {
        val task =
            VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_LOW)
        task.execute(srcPath, destPath)
        return task
    }

    class VideoCompressTask(
        private val mListener: CompressListener?,
        private val mQuality: Int
    ) :
        AsyncTask<String?, Float?, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            mListener?.onStart()
        }

        override fun doInBackground(vararg paths: String?): Boolean {
            return VideoController.getInstance().convertVideo(
                paths[0],
                paths[1],
                mQuality
            ) { percent -> publishProgress(percent) }
        }

        override fun onProgressUpdate(vararg values: Float?) {
            mListener?.onProgress(values[0]!!)
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if (mListener != null) {
                if (result) {
                    mListener.onSuccess()
                } else {
                    mListener.onFail()
                }
            }
        }
    }

    interface CompressListener {
        fun onStart()
        fun onSuccess()
        fun onFail()
        fun onProgress(percent: Float)
    }
}