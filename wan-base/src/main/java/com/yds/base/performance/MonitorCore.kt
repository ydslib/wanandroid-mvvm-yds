package com.yds.base.performance

import android.os.SystemClock
import android.util.Log
import android.util.Printer

internal class MonitorCore : Printer {
    private var mStartTime: Long = 0
    private var mStartThreadTime: Long = 0
    private var mPrintingStarted = false
    private val mStackSampler: StackSampler
    override fun println(x: String?) {
        if (!mPrintingStarted) {
            mStartTime = System.currentTimeMillis()
            mStartThreadTime = SystemClock.currentThreadTimeMillis()
            mPrintingStarted = true
            mStackSampler.startDump()
        } else {
            val endTime = System.currentTimeMillis()
            val endThreadTime: Long = SystemClock.currentThreadTimeMillis()
            mPrintingStarted = false
            if (isBlock(endTime)) {
                val entries: ArrayList<String> = mStackSampler.getThreadStackEntries(mStartTime, endTime)
                if (entries.size > 0) {
                    val blockInfo: BlockInfo = BlockInfo.newInstance()
                        .setMainThreadTimeCost(mStartTime, endTime, mStartThreadTime, endThreadTime)
                        .setThreadStackEntries(entries)
                        .flushString()
                    Log.v(TAG, blockInfo.toString())
                }
            }
            mStackSampler.stopDump()
        }
    }

    private fun isBlock(endTime: Long): Boolean {
        return endTime - mStartTime > BLOCK_THRESHOLD_MILLIS
    }

    fun shutDown() {
        mStackSampler.shutDown()
    }

    companion object {
        private const val TAG = "MonitorCore"

        /**
         * 卡顿阈值
         */
        private const val BLOCK_THRESHOLD_MILLIS = 200
    }

    init {
        mStackSampler = StackSampler()
        mStackSampler.init()
    }
}