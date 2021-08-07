package com.example.eventful.model
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
data class EventCard(val eventTitle: String,
                     val eventAddress: String,
                     val distanceResourceId: Int,
                     @DrawableRes val imageResourceId: Int) {
}