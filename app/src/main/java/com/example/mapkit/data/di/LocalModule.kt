package com.example.mapkit.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mapkit.data.local.AppDatabase
import com.example.mapkit.data.unit.APP_CACHE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    @AppCacheQualifier
    fun provideAppCacheSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        try {
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            return EncryptedSharedPreferences.create(
                context,
                APP_CACHE,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return context.getSharedPreferences(APP_CACHE, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppRoomDb(app: Application): AppDatabase {
        return AppDatabase(app)
    }

}