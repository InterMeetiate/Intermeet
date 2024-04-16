package com.intermeet.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment

class LikesPageFragment : Fragment()
{
    private lateinit var likesUser_list : GridView //can be a problem

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_likespage1, container, false)

        var likeList = mutableListOf<String>()
        likeList.add("DK6LQRJYmxarqxOHCdy4MKS63Pp2")
        likeList.add("a6TWxI1076ahgLhZFOaHmNPRbom2")
            //need to append proper datta of users into the list

        val likeUserAdapter = LikesPageAdapter(requireContext(), likeList)
        likesUser_list.adapter = likeUserAdapter

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = DiscoverFragment()
    }
}