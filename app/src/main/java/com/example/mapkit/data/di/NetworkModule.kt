package com.example.mapkit.data.di


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.mapkit.data.local.AppCache
import com.example.mapkit.data.unit.AuthRefreshTokenInterceptor
import com.example.mapkit.data.unit.HostFileInterceptor
import com.example.mapkit.data.unit.HostXaznaInterceptor
import com.example.mapkit.data.unit.MapKitFileInterceptor
import com.example.mapkit.data.unit.MapKitInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.tune.data.unit.*
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @FileUploadServiceQualifier
    @ExperimentalSerializationApi
    fun provideFileUploadRetrofitInstance(
        @FileOkHttpServiceQualifier okhttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {

        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("BASE_URL_FILE")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @XaznaServiceQualifier
    @ExperimentalSerializationApi
    fun provideRetrofitInstance(
        @XaznaOkHttpServiceQualifier okhttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("BASE_URL")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
//            .addConverterFactory(provideFactory())
            .build()
    }

    @Provides
    fun gson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    fun gsonConvertFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }


    @Provides
    @FileOkHttpServiceQualifier
    fun okHttpClientFile(
        hostInterceptor: HostFileInterceptor,
        xaznaInterceptor: MapKitFileInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        @ApplicationContext context: Context,
        refreshTokenInterceptor: AuthRefreshTokenInterceptor,
        noInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .retryOnConnectionFailure(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(xaznaInterceptor)
            .addInterceptor(hostInterceptor)
//            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(refreshTokenInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(noInterceptor)
//            .addInterceptor(PlutoInterceptor())
//            .cache(cache)
            .setTrustManager(context)
            .build()
    }

    @Provides
    @XaznaOkHttpServiceQualifier
    fun okHttpClient(
        cache: Cache,
        appCache: AppCache,
//        loginUserDSImp: LoginUserDSImp,
        hostInterceptor: HostXaznaInterceptor,
        mapKitInterceptor: MapKitInterceptor,
//        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        @ApplicationContext context: Context,
        refreshTokenInterceptor: AuthRefreshTokenInterceptor,
        noInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .retryOnConnectionFailure(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(mapKitInterceptor)
            .addInterceptor(hostInterceptor)
//            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(refreshTokenInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(noInterceptor)
//            .addInterceptor(PlutoInterceptor())
//            .cache(cache)
            .setTrustManager(context)
            .build()
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor {
//            Timber.d(it)
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    fun provideChuckerCollector(
        @ApplicationContext context: Context
    ): ChuckerCollector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_WEEK,
    )

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
        chuckerCollector: ChuckerCollector
    ): ChuckerInterceptor = ChuckerInterceptor(
        context = context,
        collector = chuckerCollector,
    )

    private fun generateSSLContext(context: Context): SSLContext {
        val cf = CertificateFactory.getInstance("X.509")

//        val ca: Certificate =
//            context.resources.openRawResource(R.raw.ziraatbank).use { cf.generateCertificate(it) }

        // Creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
//        keyStore.setCertificateEntry("ca", ca)

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    private fun OkHttpClient.Builder.setTrustManager(context: Context): OkHttpClient.Builder {
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )

        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            sslSocketFactory(
                sslContext.socketFactory,
                trustAllCerts[0] as X509TrustManager
            )
        } catch (e: Exception) {
        }

//        // Install the all-trusting trust manager
//        val sslContext = SSLContext.getInstance("SSL")
//        sslContext.init(null, trustAllCerts, SecureRandom())
//        // Create an ssl socket factory with our all-trusting manager
//        val sslSocketFactory = sslContext.socketFactory
//        val trustManagerFactory: TrustManagerFactory =
//            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//        trustManagerFactory.init(null as KeyStore?)
//        val trustManagers: Array<TrustManager> =
//            trustManagerFactory.trustManagers
//        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
//            "Unexpected default trust managers:" + trustManagers.contentToString()
//        }
//
//        val trustManager =
//            trustManagers[0] as X509TrustManager

        hostnameVerifier { _, _ -> true }
        return this
    }


    @Provides
    fun file(@ApplicationContext context: Context): File {
        val file = File(context.cacheDir, "HttpCache")
        file.mkdirs()
        return file
    }

    @Provides
    fun cache(file: File): Cache {
        return Cache(file, 20 * 100 * 100)//(20)MB
    }

}