import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.libraries.places.api.model.AutocompletePrediction

class AutocompleteAdapter(context: Context) :
    ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

    private var predictions: MutableList<AutocompletePrediction> = ArrayList()

    fun setPredictions(predictions: List<AutocompletePrediction>) {
        this.predictions.clear()
        this.predictions.addAll(predictions)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return predictions.size
    }

    override fun getItem(position: Int): AutocompletePrediction? {
        return predictions[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val prediction = predictions[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = prediction.getPrimaryText(null).toString()
        return view
    }
}
