package com.intermeet.android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserDetailAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val userIds = ArrayList<String>()

    fun setUserIds(newIds: List<String>) {
        userIds.clear()
        userIds.addAll(newIds)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = userIds.size

    override fun createFragment(position: Int): Fragment {
        return UserDetailFragment.newInstance(userIds[position])
    }
}
