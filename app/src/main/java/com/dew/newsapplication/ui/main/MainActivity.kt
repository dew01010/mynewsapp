package com.dew.newsapplication.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dew.newsapplication.databinding.ActivityMainBinding
import com.dew.newsapplication.ui.main.adapter.ViewPagerAdapter
import com.dew.newsapplication.utility.getNewSource
import com.dew.newsapplication.viewModel.NewsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    //private val viewModel by viewModels<NewsViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // attaching adapter to viewpager2
        val sectionsPagerAdapter =
            ViewPagerAdapter(
                this,
                getNewSource()
            )
        binding.viewPager.adapter=sectionsPagerAdapter

        // setup tab layout with viewpager2
        TabLayoutMediator(binding.tabs, binding.viewPager, true,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                tab.text = getNewSource()[position].name
            }).attach()
    }
}