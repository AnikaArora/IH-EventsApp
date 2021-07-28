package com.example.eventful.data

import com.example.eventful.R
import com.example.eventful.model.EventCard

class DataSource {
    fun loadEvents() : List<EventCard> {
        return listOf<EventCard>(
            EventCard(R.string.event_title_example, R.string.event_logistics_example, R.string.distance_example, R.drawable.event_image),
            EventCard(R.string.event_title_example, R.string.event_logistics_example, R.string.distance_example, R.drawable.event_image),
            EventCard(R.string.event_title_example, R.string.event_logistics_example, R.string.distance_example, R.drawable.event_image),
            EventCard(R.string.event_title_example, R.string.event_logistics_example, R.string.distance_example, R.drawable.event_image),
            EventCard(R.string.event_title_example, R.string.event_logistics_example, R.string.distance_example, R.drawable.event_image)
        )
    }
}