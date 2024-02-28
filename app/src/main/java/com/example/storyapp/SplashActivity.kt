package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            getValueFromRemoteConfig()
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            //finish the activity
            finish()
        }, 2000)
    }

    private fun getValueFromRemoteConfig() {
        Singleton.firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result

                    Singleton.storyCount = Singleton.firebaseRemoteConfig.getLong("storyCount").toInt()
                    Log.d("storyCount", Singleton.storyCount.toString())
                }
            }
    }
}