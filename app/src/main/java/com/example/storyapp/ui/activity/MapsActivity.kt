package com.example.storyapp.ui.activity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryResponse
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.preferences.AppPreferences
import com.example.storyapp.ui.viewmodel.StoryViewModel
import com.example.storyapp.ui.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    private var storyWithLocation = listOf<ListStoryResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val appPreferences = AppPreferences(this)
        val token = appPreferences.authToken

        if (token != null) {
            storyViewModel.getStories(token)
        }

        storyViewModel.message.observe(this) {
            getLocationUser(storyViewModel.story)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        setMapStyle()
    }

    private fun getLocationUser(story: List<ListStoryResponse>) {
        if (story.isNotEmpty()) {
            for (stories in story) {
                if (stories.lat != null && stories.lon != null) {
                    val position = LatLng(stories.lat, stories.lon)
                    val width = 164
                    val height = 164
                    storyWithLocation = storyWithLocation + stories

                    // Menggunakan Glide untuk memuat gambar dari URL dan mengubahnya menjadi Bitmap
                    Glide.with(this)
                        .asBitmap()
                        .load(stories.photoUrl)
                        .override(width, height) // Set the desired width and height for the icon
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val newWidth = 132 // Replace with the desired resized width
                                val newHeight = 132 // Replace with the desired resized height

                                // Resize the Bitmap if needed
                                val resizedBitmap =
                                    Bitmap.createScaledBitmap(resource, newWidth, newHeight, false)

                                // Mengubah Bitmap menjadi ikon marker
                                val markerIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)

                                // Menambahkan marker dengan ikon gambar ke dalam peta
                                val marker = mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(stories.name)
                                        .icon(markerIcon)
                                )

                                // Mengatur tag marker dengan deskripsi
                                marker?.tag = stories.description
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // Do nothing
                            }
                        })
                }
            }
        }
        if (storyWithLocation.isNotEmpty()) {
            val latLng = LatLng(storyWithLocation[0].lat!!, storyWithLocation[0].lon!!)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, INITIAL_ZOOM
                )
            )
        }

        mMap.setOnMarkerClickListener { marker ->
            val description = marker.tag as? String
            if (description != null) {
                marker.title?.let { showMarkerInfoDialog(it, description) }
            }
            true
        }
    }

    private fun showMarkerInfoDialog(title: String, description: String) {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        const val TAG = "MapsActivity"
        const val INITIAL_ZOOM = 6f
        const val EXTRA_STORIES = "extra_stories"
    }
}