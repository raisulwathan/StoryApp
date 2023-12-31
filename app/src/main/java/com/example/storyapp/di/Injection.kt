package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.db.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}