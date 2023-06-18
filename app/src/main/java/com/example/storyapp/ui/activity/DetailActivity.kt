package com.example.storyapp.ui.activity

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryPagingResponse
import com.example.storyapp.databinding.ActivityDetailBinding
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryPagingResponse>(EXTRA_STORY) as ListStoryPagingResponse
        getDetailStory(story)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.user_detail_title, story.name)
    }

    private fun getDetailStory(story: ListStoryPagingResponse) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
        }
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)
        if (story.lat != null && story.lon != null) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(story.lat, story.lon, 1)
            val address = addresses!![0].getAddressLine(0)

            binding.tvDetailLocation.text = address
        } else {
            binding.tvDetailLocation.text = getString(R.string.no_location)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

}