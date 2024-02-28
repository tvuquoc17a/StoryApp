package com.example.storyapp

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.storage.storage

object Singleton {
    var storyCount : Int = 1
    val firebaseRemoteConfig : FirebaseRemoteConfig
    var storage = Firebase.storage

    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 1
    }
    init {// cho vào pt khoi tạo để khởi tạo ngay khi chương trình chạy mà không lưu trữ trong các trường tĩnh
        firebaseRemoteConfig  = Firebase.remoteConfig
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_default_values)
    }
}