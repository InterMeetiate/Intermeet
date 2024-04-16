import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intermeet.android.R

class LikesAdapter(private val likesList: List<String>) : RecyclerView.Adapter<LikesAdapter.LikesViewHolder>() {
//take list of likes as parameter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_likes, parent, false)
        return LikesViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: LikesViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        val like = likesList[position]
        holder.bind(like)
        //updates content and display data at certain position
    }
    override fun getItemCount() = likesList.size
    //returns how many items in the list
    class LikesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewLike: TextView = itemView.findViewById(R.id.text_no_likes)
        //displays the text
        fun bind(like: String) {
            textViewLike.text = like
        }//bind the data to textview
    }
}