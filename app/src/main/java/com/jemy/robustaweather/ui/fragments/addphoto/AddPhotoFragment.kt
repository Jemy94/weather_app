package com.jemy.robustaweather.ui.fragments.addphoto

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.jemy.robustaweather.R
import com.jemy.robustaweather.ui.MainActivity
import com.jemy.robustaweather.utils.Constants
import com.jemy.robustaweather.utils.ResourceState
import com.jemy.robustaweather.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_add_photo.*
import rx.Observable
import rx.Subscription
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddPhotoFragment : Fragment(R.layout.fragment_add_photo) {

    @Inject
    lateinit var viewModelFactory: AddPhotoViewModelFactory
    private val viewModel: AddPhotoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AddPhotoViewModel::class.java)
    }
    private var subscription: Subscription? = null
    private var wid = 0
    private var city = ""
    private var temp = 0.0
    private var humidity = 0.0
    private var condition = ""
    private var image64 = ""
    private var sharedFile: File? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarTitle()
        setupSearchButtonClickListener()
        observeWeatherData()
        setupTakePhotoClickListener()
        setupSaveClickListener()
        setupShareIconListener()
    }

    private fun setupToolbarTitle() {
        toolbarTitle.text = getString(R.string.add_place)
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getWeatherData() {
        city = cityEditText.text.toString()
        viewModel.city = city
        viewModel.getWeather()
    }

    private fun setupSearchButtonClickListener() {
        getWeatherButton.setOnClickListener {
            getWeatherData()
        }
    }

    private fun setupSaveClickListener() {
        saveButton.setOnClickListener {
            saveWeatherData()
        }
    }

    private fun saveWeatherData() {
        viewModel.city = city
        viewModel.temp = temp
        viewModel.humidity = humidity
        viewModel.condition = condition
        viewModel.image = image64
        viewModel.wid = wid
        viewModel.addWeatherData().observe(viewLifecycleOwner) { resources ->
            when (resources.state) {
                ResourceState.LOADING -> {
                    loadingDialog.visible()
                }
                ResourceState.SUCCESS -> {
                    loadingDialog.gone()
                    resources.message?.let { messageDialog(message = it)?.show() }
                }
                ResourceState.ERROR -> {
                    loadingDialog.gone()
                    resources.message?.let { msg ->
                        Log.d("Add Photo", msg)
                        if (msg == Constants.Error.EMPTY_FIELD) {
                            messageDialog(message = getString(R.string.please_fill_missing_data))?.show()
                        } else {
                            messageDialog(message = msg)?.show()
                        }

                    }
                }
            }
        }
    }

    private fun observeWeatherData() {
        val loadingDialog = loadingDialog(viewModel.compositeDisposable)
        viewModel.weatherResponse.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.state) {
                ResourceState.LOADING -> loadingDialog?.show()
                ResourceState.SUCCESS -> {
                    loadingDialog?.dismiss()
                    resource.data?.let { weatherResponse ->
                        wid = weatherResponse.id!!
                        temp = weatherResponse.main?.temp ?: 0.0
                        humidity = weatherResponse.main?.temp ?: 0.0
                        condition = weatherResponse.weather?.get(0)?.description ?: "clear"
                        tempTV.text = weatherResponse.main?.temp.toString()
                        humidityTV.text = weatherResponse.main?.humidity.toString()
                        conditionTV.text = weatherResponse.weather?.get(0)?.description ?: "clear"
                    }
                }
                ResourceState.ERROR -> {
                    wid = 0
                    temp = 0.0
                    humidity = 0.0
                    condition = ""
                    wid = 0
                    loadingDialog?.dismiss()
                    resource.message?.let { msg ->
                        when (msg) {
                            Constants.Error.GENERAL -> messageDialog(R.string.general_error)?.show()
                            Constants.Error.NETWORK -> messageDialog(R.string.network_not_available)?.show()
                            Constants.Error.EMPTY_FIELD -> {
                                messageDialog(R.string.please_fill_missing_data)?.show()
                            }
                            else -> messageDialog(message = resource.message)?.show()
                        }
                    } ?: messageDialog(R.string.general_error)?.show()
                }
            }
        })
    }

    private fun setupTakePhotoClickListener() {
        var imagePickerObservable: Observable<List<File>?>
        addPhotoTextView.setOnClickListener {
            imagePickerObservable = openReactiveImagePicker()
            if (subscription == null) {
                subscription = imagePickerObservable.subscribe({ files ->
                    files?.let {
                        if (it.isNotEmpty()) {
                            val file = files.first()
                            sharedFile = files.first()
                            placeImage.load(file.path)
                            addPhotoTextView.invisible()
                            imageCard.visible()
                            shareIcon.visible()
                            image64 = file.toBase64()
                        }
                    }
                }, {
                    Log.d("Add Photo", it.message ?: "Unknown error")
                })
            }
        }
    }

    private fun setupShareIconListener() {
        shareIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).setType("image/*")
            val bitmap = placeImage.drawable.toBitmap()
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "tempimage", null)
            val uri = Uri.parse(path)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(intent)
        }
    }
}