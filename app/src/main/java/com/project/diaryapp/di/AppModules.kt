package com.project.diaryapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.project.diaryapp.Const
import com.project.diaryapp.data.lib.HeaderInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://diary-test.ifdenewhallaid.com/"

val networkModule = module {
    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor(get()))
            .addInterceptor(
                ChuckerInterceptor.Builder(get())
                .collector(ChuckerCollector(get()))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build())
            .build()

        val client = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        client
    }
}

private fun getHeaderInterceptor(sharedPreferences: SharedPreferences): Interceptor {
    val headers = HashMap<String, String>()
    return HeaderInterceptor(headers, sharedPreferences)
}

val sharedPreferenceModule = module {
    single {
        getSharedPrefs(androidApplication())
    }
    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences{
    return androidApplication.getSharedPreferences(Const.PREFERENCE_NAME,  Context.MODE_PRIVATE)
}

//val authModule = module {
//    single { ApiService.createReactiveService(AuthApiService::class.java, get()) }
//    single<AuthRepository> { AuthDataStore(get()) }
//    single<AuthUseCase> { AuthInteractor(get(), get()) }
//    viewModel { AuthViewModel(get()) }
//}
//
//val storyModule = module {
//    single { ApiService.createReactiveService(StoryApiService::class.java, get()) }
//    single<StoryRepository> { StoryDataStore(get()) }
//    single<StoryUseCase> { StoryInteractor(get()) }
//    viewModel { StoryViewModel(get()) }
//}


