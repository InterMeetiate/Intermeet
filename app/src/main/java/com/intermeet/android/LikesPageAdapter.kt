package com.intermeet.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LikesPageAdapter(context: Context, private val likesuser_list: List<String>) : ArrayAdapter<String>(context, R.layout.likes_user_item, likesuser_list)
{
    override fun getCount(): Int {
        return likesuser_list.size
    }

    override fun getItem(position: Int): String? {
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


        val dbReference = FirebaseDatabase.getInstance().getReference("users/$likesuser")
        dbReference.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserData::class.java)

                userData?.let{ user ->
                    val image = user.photoDownloadUrls

                    Glide.with(context)
                        .load(image?.first())
                        .into(viewHolder.likesuser_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return viewItem!!
    }

    // ViewHolder pattern for efficient view recycling
    private class ViewHolder {
        lateinit var likesuser_image: ImageView
        //lateinit var likesuser_name: TextView
    }
}