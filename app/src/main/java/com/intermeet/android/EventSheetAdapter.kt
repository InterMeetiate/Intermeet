import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.intermeet.android.Event
import com.intermeet.android.R

class EventSheetAdapter(context: Context, private val eventsList: List<Event>) :
    ArrayAdapter<Event>(context, R.layout.event_sheet_item, eventsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.event_sheet_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.eventImage = itemView.findViewById(R.id.event_image)
            viewHolder.eventTitle = itemView.findViewById(R.id.events_title)
            viewHolder.eventDate = itemView.findViewById(R.id.event_date)
            viewHolder.eventDescription = itemView.findViewById(R.id.event_description)
            itemView.tag = viewHolder
        } else {
            viewHolder = itemView.tag as ViewHolder
        }

        val event = eventsList[position]

        // Bind data to views
        viewHolder.eventTitle.text = event.title
        viewHolder.eventDate.text = event.whenInfo
        viewHolder.eventDescription.text = event.description

        // Load image using Glide library
        Glide.with(context)
            .load(event.thumbnail)
            .into(viewHolder.eventImage)

        return itemView!!
    }

    // ViewHolder pattern for efficient view recycling
    private class ViewHolder {
        lateinit var eventImage: ImageView
        lateinit var eventTitle: TextView
        lateinit var eventDate: TextView
        lateinit var eventDescription: TextView
    }
}