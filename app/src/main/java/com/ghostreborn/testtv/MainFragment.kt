package com.ghostreborn.testtv

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt

class MainFragment: BrowseSupportFragment() {

    private lateinit var mMetrics: DisplayMetrics

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
//        prepareBackgroundManager()
        mMetrics = DisplayMetrics()
        updateBackground(Anime("One Piece", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"))
        loadRows()
        view?.findViewById<ViewGroup>(androidx.leanback.R.id.browse_container_dock)?.apply {
            setPadding(0,200, 0 , 0)
        }
    }

    private fun loadRows() {
        val animeList = getAnimeList()
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for(anime in animeList) {
            listRowAdapter.add(anime)
        }
        rowsAdapter.add(ListRow(HeaderItem(0, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(1, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(2, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(3, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(4, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(5, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(6, "Anime"), listRowAdapter))
        adapter = rowsAdapter
    }

    fun getAnimeList(): List<Anime> {
        return listOf(
            Anime("Anime Title 1", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"),
            Anime("Anime Title 2", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"),
            Anime("Anime Title 3", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"),
            Anime("Anime Title 4", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"),
            Anime("Anime Title 5", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg")
        )
    }

    private fun setupUIElements() {
        headersState = HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.brand_color)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_affordance)
    }

    private fun updateBackground(anime: Anime) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        val mBackgroundManager = BackgroundManager.getInstance(activity)

        val textDrawable = object : Drawable() {
            override fun draw(canvas: android.graphics.Canvas) {
                val paint = android.graphics.Paint()
                paint.color = "#B0000000".toColorInt() // Semi-transparent black background
                paint.textSize = 30f
                paint.textAlign = android.graphics.Paint.Align.LEFT
                paint.color = Color.WHITE

                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

                val x = 50f // Left margin
                var y = 50f // Top margin

                // Draw title
                paint.textSize = 40f
                canvas.drawText(anime.title, x, y, paint)
                y += 50f // Add space

                // Draw description
                paint.textSize = 20f
                val descriptionLines = getLines(anime.title, paint, width - 100) // Wrap description
                for (line in descriptionLines) {
                    canvas.drawText(line, x, y, paint)
                    y += 25f
                }
                y += 25f

                // Draw rating
                paint.textSize = 25f
                canvas.drawText("Rating: ${anime.title}", x, y, paint)
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {}
            override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT
        }
        mBackgroundManager.drawable = textDrawable
    }

    // Helper function to wrap text
    private fun getLines(text: String, paint: android.graphics.Paint, maxWidth: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""
        for (word in words) {
            if (paint.measureText("$currentLine $word") < maxWidth) {
                currentLine += "$word "
            } else {
                lines.add(currentLine)
                currentLine = "$word "
            }
        }
        lines.add(currentLine)
        return lines
    }

    private fun prepareBackgroundManager() {
        mMetrics = DisplayMetrics()
        val mManager = BackgroundManager.getInstance(activity)
        mManager.attach(requireActivity().window)
        val defaultBackground = ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
        mManager.drawable = defaultBackground
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(requireActivity())
                .data("https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg")
                .scale(Scale.FILL)
                .build()

            val result = activity?.imageLoader?.execute(request)
            withContext(Dispatchers.Main) {
                if (result?.drawable != null) {
                    val layers = arrayOf(result.drawable, defaultBackground)
                    val layersDrawable = LayerDrawable(layers)
                    mManager.drawable = layersDrawable
                }
            }
        }
    }

}