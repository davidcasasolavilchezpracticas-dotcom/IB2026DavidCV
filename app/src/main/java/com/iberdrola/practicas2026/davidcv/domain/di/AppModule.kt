package com.iberdrola.practicas2026.davidcv.domain.di

import android.content.Context
import android.content.res.Resources
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.database.BillDatabase
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryImplementation
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryMock
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryNetwork
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Configuración de la fuente de datos
    var MOCK_ON = true      // true -> Datos estáticos del Mock
    var USE_NETWORK = true   // true -> Retrofit (Mockoon/API), false -> Room (Local)

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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // URL para acceder a Mockoon desde el emulador
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideBillRepository(
        dao: BillDao,
        apiService: ApiService,
        mockRepository: BillRepositoryMock
    ): BillRepositoryInterface {
        return when {
            MOCK_ON -> mockRepository
            USE_NETWORK -> BillRepositoryNetwork(apiService)
            else -> BillRepositoryImplementation(dao)
        }
    }
}
