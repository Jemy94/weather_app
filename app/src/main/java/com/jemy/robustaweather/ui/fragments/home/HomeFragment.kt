package com.jemy.robustaweather.ui.fragments.home

import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog
import com.jemy.robustaweather.R
import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.ui.MainActivity
import com.jemy.robustaweather.ui.fragments.home.adapter.WeatherPlacesAdapter
import com.jemy.robustaweather.utils.ResourceState
import com.jemy.robustaweather.utils.extensions.loadAsBitmap
import com.jemy.robustaweather.utils.extensions.gone
import com.jemy.robustaweather.utils.extensions.messageDialog
import com.jemy.robustaweather.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.dialog_image_full_size.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(HomeViewModel::class.java)
    }
    private val weatherAdapter by lazy { WeatherPlacesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarTitle()
        getWeatherPlaces()
        setSwipeAction()
    }

    private fun setupToolbarTitle() {
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        toolbarTitle.text = getString(R.string.home)
    }


    private fun setSwipeAction() {
        placesSwipeLayout.setOnRefreshListener {
            weatherAdapter.clear()
            getWeatherPlaces()
        }
    }

    private fun getWeatherPlaces() {
        viewModel.getWeatherList().observe(viewLifecycleOwner) { resources ->
            when (resources.state) {
                ResourceState.LOADING -> {
                    placesSwipeLayout.isRefreshing = true
                }
                ResourceState.SUCCESS -> {
                    placesSwipeLayout.isRefreshing = false
                    resources.data?.let { weatherPlaces ->
                        if (!weatherPlaces.isNullOrEmpty()) {
                            weatherPlacesRecycler.visible()
                            noDataTextView.gone()
                            setupWeatherPlacesAdapter(weatherPlaces)
                        } else {
                            weatherPlacesRecycler.gone()
                            noDataTextView.visible()
                        }

                    }
                }
                ResourceState.ERROR -> {
                    weatherPlacesRecycler.gone()
                    noDataTextView.visible()
                    placesSwipeLayout.isRefreshing = false
                    resources.message?.let { messageDialog(message = it)?.show() }
                }
            }
        }
    }

    private fun setupWeatherPlacesAdapter(list: List<WeatherEntity>) {
        weatherPlacesRecycler.adapter = weatherAdapter
        weatherAdapter.addItems(list)
        weatherAdapter.setItemCallBack { weatherEntity ->
            val swipeDialog = SwipeDismissDialog.Builder(requireActivity())
                .setLayoutResId(R.layout.dialog_image_full_size)
                .build()
            val image = swipeDialog.fullSizeImage
            val backButton = swipeDialog.iconBack
            val byteArray = Base64.decode(weatherEntity?.image,Base64.DEFAULT)
            image.loadAsBitmap(byteArray)
            backButton.setOnClickListener { swipeDialog.dismiss() }
            swipeDialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionAddPlace -> {
                view?.findNavController()?.navigate(R.id.action_homeFragment_to_addPhotoFragment)
                false
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}