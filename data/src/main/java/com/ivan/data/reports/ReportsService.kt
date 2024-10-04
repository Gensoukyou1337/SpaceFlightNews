package com.ivan.data.reports

import com.ivan.data.models.Item
import com.ivan.data.models.ItemsListPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportsService {

    @GET("v4/reports")
    suspend fun getReports(
        @Query("search") searchTerm: String? = null,
        @Query("news_site") newsSiteFilter: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ItemsListPage

    @GET("v4/reports/{id}")
    suspend fun getSpecificReport(@Path("id") id: Int): Item

}