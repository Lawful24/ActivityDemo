package com.appplanet.activitydemo

import android.content.res.AssetManager

class InputJsonRunnable : Runnable {
    override fun run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
        
    }
}