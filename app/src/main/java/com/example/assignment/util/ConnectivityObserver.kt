package com.example.assignment.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityObserver
@Inject constructor(@ApplicationContext private val appContext: Context) {


    enum class Status { Available, Unavailable }

    fun observe(): Flow<Status> = callbackFlow {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(Status.Available).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(Status.Unavailable).isSuccess
            }

            override fun onUnavailable() {
                trySend(Status.Unavailable).isSuccess
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, callback)

        awaitClose {
            cm.unregisterNetworkCallback(callback)
        }
    }
}





