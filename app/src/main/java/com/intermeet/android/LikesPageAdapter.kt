package com.intermeet.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class LikesPageAdapter(context: Context, private val likesuser_list: List<LikeUser>) : ArrayAdapter<LikeUser>(context, R.layout.likes_user_item, likesuser_list)
{
    override fun getCount(): Int {
        return likesuser_list.size
    }

    override fun getItem(position: Int): LikeUser? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewItem = convertView
        val viewHolder : ViewHolder

        if(viewItem == null)
        {
            viewItem = LayoutInflater.from(context).inflate(R.layout.likes_user_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.likesuser_image = viewItem.findViewById(R.id.likespage_image)
            viewItem.tag = viewHolder
        }
        else {
            viewHolder = viewItem.tag as ViewHolder
        }

        val likesuser = likesuser_list[position]

        Glide.with(context)
            .load(likesuser.thumbnail)
            .into(viewHolder.likesuser_image)

        return viewItem!!
    }

    // ViewHolder pattern for efficient view recycling
    private class ViewHolder {
        lateinit var likesuser_image: ImageView
        //lateinit var likesuser_name: TextView
    }
}