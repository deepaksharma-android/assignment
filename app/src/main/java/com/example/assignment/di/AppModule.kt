package com.example.assignment.di

import com.example.assignment.util.AppConstant
import com.example.assignment.util.ConnectivityObserver
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun provideGson(): Gson = GsonBuilder().create()

	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient {
		//TODO Remove Logging Interceptor before release
		val logging = HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
		return OkHttpClient.Builder()
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.addInterceptor(logging)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
		.baseUrl(AppConstant.BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create(gson))
		.build()

	@Provides
	fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        ConnectivityObserver(context.applicationContext)
}


