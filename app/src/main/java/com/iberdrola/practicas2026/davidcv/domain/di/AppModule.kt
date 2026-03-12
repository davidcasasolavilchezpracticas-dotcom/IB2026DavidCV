package com.iberdrola.practicas2026.davidcv.domain.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.database.BillDatabase
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryDelegate
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryRoom
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryNetwork
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    private const val ES_EMULADOR = false
    private const val IP_PC_LOCAL = "172.16.216.25"
    private const val USAR_ADB_REVERSE = true

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideBillDatabase(@ApplicationContext context: Context): BillDatabase = 
        BillDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideBillDao(database: BillDatabase): BillDao = database.billDao()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, formatter)
            })
            .registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { src, _, _ ->
                JsonPrimitive(src.format(formatter))
            })
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        val host = if (USAR_ADB_REVERSE) "127.0.0.1" else if (ES_EMULADOR) "10.0.2.2" else IP_PC_LOCAL
        return Retrofit.Builder()
            .baseUrl("http://$host:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideBillRepositoryRoom(dao: BillDao): BillRepositoryRoom = BillRepositoryRoom(dao)

    @Provides
    @Singleton
    fun provideBillRepositoryNetwork(apiService: ApiService): BillRepositoryNetwork = BillRepositoryNetwork(apiService)

    @Provides
    @Singleton
    fun provideBillRepository(
        delegate: BillRepositoryDelegate
    ): BillRepositoryInterface = delegate
}
