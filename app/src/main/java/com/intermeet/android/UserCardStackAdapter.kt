import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intermeet.android.R
import com.intermeet.android.UserDataModel
import com.intermeet.android.helperFunc.calculateAgeWithCalendar

class UserCardStackAdapter(
    private var userList: List<UserDataModel>
) : RecyclerView.Adapter<UserCardStackAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUserList: List<UserDataModel>) {
        userList = newUserList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_detail, parent, false) // Ensure correct layout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user) // Properly bind user data to views
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView1)
        private val textViewName: TextView = view.findViewById(R.id.textViewName)

        @SuppressLint("SetTextI18n")
        fun bind(user: UserDataModel) {
            textViewName.text = "${user.firstName}, ${calculateAgeWithCalendar(user.birthday)}"
            Glide.with(itemView)
                .load(user.photoDownloadUrls.firstOrNull())
                .centerCrop()
                .into(imageView) // Ensure Glide loads correctly
        }
    }
}