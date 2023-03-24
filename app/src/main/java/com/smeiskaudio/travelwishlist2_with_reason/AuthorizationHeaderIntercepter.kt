package com.smeiskaudio.travelwishlist2_with_reason

import com.smeiskaudio.travelwishlist.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationHeaderInterceptor: Interceptor {
    // Interceptor intercepts http requests before they happen and do things with them
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeaders = chain.request().newBuilder()
            .addHeader("Authorization", "Token ${BuildConfig.PLACES_TOKEN}")
            .build()

        return chain.proceed(requestWithHeaders)
    }
}