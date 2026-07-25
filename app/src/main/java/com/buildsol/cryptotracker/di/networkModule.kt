package com.buildsol.cryptotracker.di

import com.buildsol.cryptotracker.BuildConfig
import com.buildsol.cryptotracker.data.remote.api.CoinGeckoApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("x-cg-demo-api-key", BuildConfig.API_KEY)
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    single<CoinGeckoApi> {
        get<Retrofit>().create(CoinGeckoApi::class.java)
    }
}