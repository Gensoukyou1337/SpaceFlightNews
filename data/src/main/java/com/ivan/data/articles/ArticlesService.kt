package com.ivan.data.articles

import com.ivan.data.models.Item
import com.ivan.data.models.ItemsListPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticlesService {
    @GET("v4/articles")
    suspend fun getArticles(
        @Query("search") searchTerm: String? = null,
        @Query("news_site") newsSiteFilter: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ItemsListPage

    @GET("v4/articles/{id}")
    suspend fun getSpecificArticle(@Path("id") id: Int): Item
}