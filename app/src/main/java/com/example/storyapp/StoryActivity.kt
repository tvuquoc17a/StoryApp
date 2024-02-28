package com.example.storyapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.storyapp.adapters.DynamicStoryAdapter
import com.example.storyapp.databinding.ActivityStoryBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlin.math.log
import kotlin.properties.Delegates
import kotlin.random.Random

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding


    private lateinit var adapter: DynamicStoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        performStory()

    }

    private fun performStory() {
        val storyList = arrayListOf<Fragment>()
        val storageRef = Singleton.storage.reference.child("animals/")
        val items: MutableList<String> = mutableListOf()

        storageRef.listAll()
            .addOnSuccessListener { list ->
                val downloadTasks = mutableListOf<Task<Uri>>()
                list.items.forEach { item ->
                    val task = item.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.d("url1", "$uri")
                            items.add(uri.toString())
                        }
                        .addOnFailureListener { exception ->
                            Log.d("url1", exception.message.toString())
                        }
                    downloadTasks.add(task)
                }

                Tasks.whenAllComplete(downloadTasks)
                    .addOnSuccessListener {
                        for (i in 0 until Singleton.storyCount) {
                            val randomIndex = Random.nextInt(items.size)
                            val randomImg = items[randomIndex]
                            Log.d("randomImg , randomIndex", randomImg + randomIndex)
                            storyList.add(StoryFragment(randomImg))
                        }

                        adapter = DynamicStoryAdapter(storyList, supportFragmentManager, lifecycle)
                        binding.storyPage.adapter = adapter
                        Log.d("fragments", adapter.itemCount.toString())
                    }
                    .addOnFailureListener { exception ->
                        Log.d("url1", exception.message.toString())
                    }
            }
            .addOnFailureListener { exception ->
                Log.d("url1", exception.message.toString())
            }
    }

}