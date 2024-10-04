package com.ivan.data.sites

import com.ivan.data.models.NewsSiteFilter
import retrofit2.http.GET

interface NewsSitesService {
    @GET("v4/info")
    suspend fun getNewsSites(): NewsSiteFilter
}