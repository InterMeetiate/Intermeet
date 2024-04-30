package com.intermeet.android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UsersPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var userIds = emptyList<String>()

    fun setUserIds(newIds: List<String>) {
        userIds = newIds
    }

    override fun getItemCount(): Int = userIds.size

    override fun createFragment(position: Int): Fragment {
        return UserDetailFragment.newInstance(userIds[position])
    }

    fun getUserId(position: Int): String {
        return userIds[position]
    }
}