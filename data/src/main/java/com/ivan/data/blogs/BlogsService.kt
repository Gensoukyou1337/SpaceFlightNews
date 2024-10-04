package com.ivan.data.blogs

import com.ivan.data.models.Item
import com.ivan.data.models.ItemsListPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BlogsService {

    @GET("v4/blogs")
    suspend fun getBlogs(
        @Query("search") searchTerm: String? = null,
        @Query("news_site") newsSiteFilter: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ItemsListPage

    @GET("v4/blogs/{id}")
    suspend fun getSpecificBlog(@Path("id") id: Int): Item

}