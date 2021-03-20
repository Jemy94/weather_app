package com.jemy.robustaweather.ui.fragments.home.adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jemy.robustaweather.R
import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.utils.extensions.loadAsBitmap
import kotlinx.android.synthetic.main.item_weather_place.view.*

class WeatherPlacesAdapter : RecyclerView.Adapter<WeatherPlaceViewHolder>() {

    private var itemCallback: ((WeatherEntity?) -> Unit)? = null
    var items = mutableListOf<WeatherEntity>()

    fun addItems(items: List<WeatherEntity>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherPlaceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_weather_place, parent, false)
        return WeatherPlaceViewHolder(view, itemCallback)
    }

    override fun onBindViewHolder(holder: WeatherPlaceViewHolder, position: Int) {
        val popular = items[position]
        holder.bind(popular)
    }

    fun setItemCallBack(itemCallback: (WeatherEntity?) -> Unit) {
        this.itemCallback = itemCallback
    }

    override fun getItemCount(): Int = items.size
}

class WeatherPlaceViewHolder(
    itemView: View,
    private val itemCallback: ((WeatherEntity?) -> Unit)?
) :
    RecyclerView.ViewHolder(itemView) {

    private var city = itemView.placeNameTextView
    private var temp = itemView.tempTextView
    private var condition = itemView.conditionTextView
    private var humidity = itemView.humidityTextView
    private var image = itemView.placeImageView

    fun bind(weather: WeatherEntity?) {
        itemView.setOnClickListener { itemCallback?.invoke(weather) }
        city.text = weather?.city
        temp.text = "${weather?.temp} C"
        humidity.text = "${weather?.humidity} %"
        condition.text = weather?.condition
        val byteArray = Base64.decode(weather?.image, Base64.DEFAULT)
        image.loadAsBitmap(byteArray)
    }
}