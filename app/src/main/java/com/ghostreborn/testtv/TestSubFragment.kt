package com.ghostreborn.testtv

import android.R.attr.width
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.leanback.app.BrowseSupportFragment

class TestSubFragment: BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        headersState = HEADERS_HIDDEN

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getRealMetrics(metrics)
        val width = metrics.widthPixels
        Log.e("TAG", "$width")
    }
}