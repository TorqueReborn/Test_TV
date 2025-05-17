package com.ghostreborn.testtv

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment

class MainFragment: BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
    }

    private fun setupUIElements() {
        title = "App Name"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.brand_color)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_affordance)
    }

}