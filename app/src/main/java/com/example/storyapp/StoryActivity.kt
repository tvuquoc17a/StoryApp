package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.storyapp.adapters.DynamicStoryAdapter
import com.example.storyapp.databinding.ActivityStoryBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlin.math.log
import kotlin.properties.Delegates

class StoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryBinding
    private lateinit var firebaseRemoteConfig : FirebaseRemoteConfig
    private var storyCount by Delegates.notNull<Int>()
    private lateinit var adapter: DynamicStoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_default_values)


        //getValueFromRemoteConfig()
        performStory()

    }

    private fun performStory() {
        val storyList = arrayListOf<Fragment>()
        for(i in 0..<5){
            storyList.add(StoryFragment())
        }

        adapter = DynamicStoryAdapter(storyList, supportFragmentManager, lifecycle)
        binding.storyPage.adapter = adapter
        Log.d("fragments", adapter.itemCount.toString())
    }

    private fun getValueFromRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    val updated = task.result

                    storyCount = firebaseRemoteConfig.getLong("storyCount").toInt()
                    Log.d("updatedlog", firebaseRemoteConfig.getString("storyCount"))
                    //performStory()
                }
            }
    }
}