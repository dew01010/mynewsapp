package com.dew.newsapplication.ui.main.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dew.newsapplication.model.NewsSource
import com.dew.newsapplication.ui.newHeadline.NewsHeadlineListFragment

/**
 * A [ViewPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
 class ViewPagerAdapter(fa: FragmentActivity,private  val list:ArrayList<NewsSource>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return list.size
    }
    override fun createFragment(position: Int): Fragment {
        return NewsHeadlineListFragment.newInstance(list[position])
    }
}

