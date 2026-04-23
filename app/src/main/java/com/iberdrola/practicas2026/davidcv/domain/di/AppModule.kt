package com.iberdrola.practicas2026.davidcv.domain.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.local.database.BillDatabase
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryDelegate
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryRoom
import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryNetwork
import com.iberdrola.practicas2026.davidcv.data.repository.ContractRepositoryDelegate
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
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
    fun provideContractDao(database: BillDatabase): ContractDao = database.contractDao()

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
    fun provideOkHttpClient(): OkHttpClient {
        // 1. Crear un TrustManager que no valide la cadena de certificados
        val trustAllCerts = object : javax.net.ssl.X509TrustManager {
            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        }

        // 2. Instalar el TrustManager en un SSLContext
        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustAllCerts), java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts) // IMPORTANTE para certificados autofirmados
            .hostnameVerifier { _, _ -> true } // Ya lo tenías, valida que el host coincida
            .addInterceptor { chain ->
                val request = chain.request()
                val newHost = when (DataSourceConfig.connectionMode) {
                    ConnectionMode.EMULATOR -> "10.0.2.2"
                    ConnectionMode.ADB_REVERSE -> "127.0.0.1"
                    ConnectionMode.LOCAL_IP -> DataSourceConfig.pcIp
                }

                val newUrl = request.url.newBuilder()
                    .scheme("https") // Forzamos HTTPS por si Mockoon usa TLS
                    .host(newHost)
                    .build()

                chain.proceed(request.newBuilder().url(newUrl).build())
            }
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
            //.hostnameVerifier { _ , _ -> true }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://placeholder:3000/")
            .client(okHttpClient)
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

    @Provides
    @Singleton
    fun provideContractRepository(
        delegate: ContractRepositoryDelegate
    ): ContractRepositoryInterface = delegate
}
