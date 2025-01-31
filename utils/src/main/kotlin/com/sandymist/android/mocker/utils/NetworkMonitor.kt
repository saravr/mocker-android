package com.scribd.android.mocker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

sealed class NetworkStatus {
    object Unknown : NetworkStatus()
    object Connected : NetworkStatus()
    object Disconnected : NetworkStatus()
}

@Singleton
class NetworkMonitor @Inject constructor(context: Context) {
    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE,
    ) as ConnectivityManager

    val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.d("Network: Connected")
                trySend(NetworkStatus.Connected)
            }

            override fun onUnavailable() {
                Timber.e("Network: Unavailable")
                trySend(NetworkStatus.Disconnected)
            }

            override fun onLost(network: Network) {
                Timber.e("Network: Disconnected")
                trySend(NetworkStatus.Disconnected)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        Timber.d("Network: register listener")
        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
}
