package com.example.eventful.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventful.R
import com.example.eventful.model.EventCard
import com.example.eventful.ui.dashboard.DashboardFragment
class ItemAdapter(
    private val context: DashboardFragment,
    private val dataset: List<EventCard>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val eventTitle: TextView = view.findViewById(R.id.event_title)
        val eventLogistics: TextView = view.findViewById(R.id.event_logistics)
        val eventDistance: TextView = view.findViewById(R.id.event_distance)
        val eventImage: ImageView = view.findViewById(R.id.event_image)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //adapter Layout now holds reference to the list item view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.eventTitle.text = item.eventTitle
        holder.eventLogistics.text = item.eventAddress
        holder.eventDistance.text = context.resources.getString(item.distanceResourceId)
        holder.eventImage.setImageResource(item.imageResourceId)
    }
    override fun getItemCount(): Int {
        return dataset.size
    }
}