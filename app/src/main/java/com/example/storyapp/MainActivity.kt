package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.adapters.ViewPagerAdapter
import com.example.storyapp.onboarding.screens.FirstFragment
import com.example.storyapp.onboarding.screens.SecondFragment
import com.example.storyapp.onboarding.screens.ThirdFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var view: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var indicators: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        view = findViewById(R.id.viewPager)
        indicators = findViewById(R.id.indicators)
        val fragmentList = arrayListOf(FirstFragment(), SecondFragment(), ThirdFragment())

        adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        view.isUserInputEnabled = true
        view.adapter = adapter
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // hiện thông báo khi chuyển trang
                Log.d("scrolled", "${position + 1}")
                val dotList = arrayListOf<ImageView>(binding.dot1, binding.dot2, binding.dot3)
                for (i in 0..<adapter.itemCount) {
                    if (i <= position) {
                        dotList[i].setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.ic_active_dot
                            )
                        )
                    } else {
                        dotList[i].setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.ic_inactive_dot
                            )
                        )
                    }
                }
                if (position == 1) {
                    binding.btnPrevious.visibility = View.VISIBLE
                    binding.btnNext.text ="next"
                } else if (position == 2) {
                    binding.btnNext.text ="start"
                } else {
                    binding.btnPrevious.visibility = View.GONE
                }

            }
        })
        binding.btnNext.setOnClickListener {
            if (view.currentItem < adapter.itemCount - 1) {
                view.currentItem += 1
            } else {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                //finish()
            }
        }
        binding.btnPrevious.setOnClickListener {
            if (view.currentItem == 1 || view.currentItem == 2) {
                view.currentItem -= 1
            } else {
                Toast.makeText(this, "This is the first page", Toast.LENGTH_SHORT).show()
            }
        }
    }
}