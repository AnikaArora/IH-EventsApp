package com.example.eventful.data
import android.content.Intent
import android.os.Parcelable
import android.util.EventLog
import android.util.Log
import com.example.eventful.R
import com.example.eventful.model.EventCard
import com.example.eventful.MainActivity
import com.example.eventful.listOfEvents
class DataSource {
    fun loadEvents() : List<EventCard> {
        val events = listOfEvents
        val eventCards = mutableListOf<EventCard>()
        for(i in events.indices) {
            val title = events[i].title
            val address = events[i].address
            val event = EventCard(title, address, R.string.distance_example, R.drawable.event_image)
            eventCards.add(event)
        }
        return eventCards
    }
}