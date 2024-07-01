package com.challenge.felipeajc.pixabaysearch.data.remote

import com.challenge.felipeajc.pixabaysearch.BuildConfig
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImagesSearchResponse
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RemotePixabayContract {
    @GET("api/")
    suspend fun search(
        @Query("q") query: String,
        @Query("image_type") imageType: String = ImageType.PHOTO.key,
    ): PixabayImagesSearchResponse
}

fun pixabayApi(
    baseUrl: HttpUrl = BuildConfig.BASE_URL.toHttpUrl(),
    client: () -> OkHttpClient = { createOkHttpClient() },
): RemotePixabayContract {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client())
        .build()

    return retrofit.create(RemotePixabayContract::class.java)
}

fun createOkHttpClient(
    logging: () -> Interceptor = { loggingInterceptor() },
    authorization: () -> Interceptor = { apiKeyInterceptor() },
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(logging())
        .addInterceptor(authorization())
        .build()

private fun apiKeyInterceptor() = Interceptor {
    val url: HttpUrl = it.request().url
        .newBuilder()
        .addQueryParameter(
            com.challenge.felipeajc.pixabaysearch.BuildConfig.API_KEY_PARAMETER_NAME,
            com.challenge.felipeajc.pixabaysearch.BuildConfig.API_KEY
        )
        .build()
    val request: Request = it.request().newBuilder().url(url).build()
    it.proceed(request)
}

private fun loggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }


enum class ImageType(val key: String) {
    PHOTO("photo")
}
