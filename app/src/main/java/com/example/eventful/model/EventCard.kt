package com.example.eventful.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class EventCard(val titleResourceId: Int,
                     val logisticsResourceId: Int,
                     val distanceResourceId: Int,
                     @DrawableRes val imageResourceId: Int) {

}