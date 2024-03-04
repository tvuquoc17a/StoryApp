package com.example.storyapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.storyapp.databinding.FragmentStoryBinding
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.sql.DataSource


class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private var isImageLoadedFromCache = false
    private lateinit var imageUrl: String
    private lateinit var progressBar: ProgressBar


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

        lifecycleScope.launch(Dispatchers.IO) {
            activity?.runOnUiThread {
                loadData()
            }
//            lifecycleScope.launch(Dispatchers.Main) {
//                while (progressBar.progress < progressBar.max) {
//                    delay(100)
//                    progressBar.progress += 100
//                    Log.d("progress", "in onViewCreated")
//                }
//            }
            Log.d("fragment_state", "fragment created")
        }
    }

    private fun runProgressBar() {
        lifecycleScope.launch {
            progressBar.progress = 0
            while (progressBar.progress < progressBar.max) {
                delay(10)
                progressBar.progress += 10
                Log.d("progress", "in onViewCreated")
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
            //runProgressBar()
            Log.d("progress", "in loadData block")
        }

    }


    override fun onResume() {
        super.onResume()
        runProgressBar()
        Log.d("progress", "in resume block")
        Log.d("fragment_state", "onResume")

    }


}