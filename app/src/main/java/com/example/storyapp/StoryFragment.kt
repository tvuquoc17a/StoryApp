package com.example.storyapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.storyapp.databinding.FragmentStoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StoryFragment(private var viewPager2: ViewPager2) : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private var isImageLoadedFromCache = false
    private lateinit var imageUrl: String
    private lateinit var progressBar: ProgressBar
    private var isPageLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("fragment_state", "fragment create")
        // Inflate the layout for this fragment

        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageUrl = arguments?.getString("imageUrl").toString()
        progressBar = binding.storyProgress
        progressBar.max = 5000

        //listenForPageChange()
        lifecycleScope.launch(Dispatchers.IO) {
            activity?.runOnUiThread {
                loadData()
            }
            Log.d("fragment_state", "fragment created")
        }
        runProgressBar()
        abc()
    }

    private fun abc(){
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("asgawgawgawg", "onPageSelected: $position")
                job?.cancel()
                job?.start()
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })
    }
    private var job: Job? = null

    private fun runProgressBar() {

        job =  lifecycleScope.launch {
            binding.idStory.text = viewPager2.currentItem.toString()
            progressBar.progress = 0
            for (i in 0..progressBar.max step 10) {
                progressBar.progress = i
                delay(10)
                if (progressBar.progress == progressBar.max ) {
                    Log.d("progress", "transfer")

                    viewPager2.currentItem += 1
                }
            }
        }
    }


    private fun loadData() {
        try {
            val glideRequest = Glide.with(requireContext()).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        isImageLoadedFromCache =
                            (dataSource == com.bumptech.glide.load.DataSource.MEMORY_CACHE)
                        return false
                    }

                })
            glideRequest.into(binding.imgStory)
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        } finally {
        }

    }


    override fun onResume() {
        super.onResume()
        Log.d("current_page ", "from fragment ${viewPager2.currentItem}")
        isPageLoaded = false
        Log.d("fragment_state", "onResume")

    }
}