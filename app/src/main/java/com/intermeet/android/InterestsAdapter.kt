import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.intermeet.android.R

class InterestsAdapter(private val interests: List<String>) :
    RecyclerView.Adapter<InterestsAdapter.InterestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        holder.interestText.text = interests[position]
    }

    override fun getItemCount(): Int = interests.size

    class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val interestText: TextView = itemView.findViewById(R.id.tvInterest)
    }
}
