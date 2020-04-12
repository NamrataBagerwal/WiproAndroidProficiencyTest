package com.androidtestapp.wipro.repository.remotedatastore.networking.retrofit.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalResponse = chain.proceed(request)
        val cacheControl = originalResponse.header("Cache-Control")

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")
        ) {


            val cc = CacheControl.Builder()
                .maxStale(1, TimeUnit.DAYS)
                .build()

//            Alternatively set the cache in the header as:
            /*return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-stale=" + 60 * 60 * 24)
                    .build();*/


            request = request.newBuilder()
                .cacheControl(cc)
                .build()

            return chain.proceed(request)

        } else {
            return originalResponse
        }
    }
}