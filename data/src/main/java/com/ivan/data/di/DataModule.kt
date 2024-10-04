package com.ivan.data.di

import com.ivan.data.common.ItemsDataSource
import com.ivan.data.articles.ArticlesDataSourceImpl
import com.ivan.data.articles.ArticlesService
import com.ivan.data.blogs.BlogsDataSourceImpl
import com.ivan.data.blogs.BlogsService
import com.ivan.data.common.ItemsRepository
import com.ivan.data.common.ItemsRepositoryImpl
import com.ivan.data.reports.ReportsDataSourceImpl
import com.ivan.data.reports.ReportsService
import com.ivan.data.sites.NewsSitesDataSource
import com.ivan.data.sites.NewsSitesDataSourceImpl
import com.ivan.data.sites.NewsSitesService
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

fun provideArticlesService(retrofit: Retrofit): ArticlesService = retrofit.create()
fun provideBlogsService(retrofit: Retrofit): BlogsService = retrofit.create()
fun provideReportsService(retrofit: Retrofit): ReportsService = retrofit.create()
fun provideNewsSitesService(retrofit: Retrofit): NewsSitesService = retrofit.create()

val dataModule = module {
    // Just to be safe. I know I already added the pin-set in network_security_config.xml.
    single<CertificatePinner> {
        CertificatePinner.Builder()
            .add("api.spaceflightnewsapi.net", "sha256/aCu9gReMs18ofcr9sqEY/0D9FdKAARMOd+CZMmhv4Jo=")
            .build()
    }
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .certificatePinner(get())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/")
            .client(get())
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    factory<ArticlesService> { provideArticlesService(get()) }
    factory<BlogsService> { provideBlogsService(get()) }
    factory<ReportsService> { provideReportsService(get()) }
    factory<NewsSitesService> { provideNewsSitesService(get()) }

    factory<ItemsDataSource>(named("articlesDataSource")) { ArticlesDataSourceImpl(get()) }
    factory<ItemsDataSource>(named("blogsDataSource")) { BlogsDataSourceImpl(get()) }
    factory<ItemsDataSource>(named("reportsDataSource")) { ReportsDataSourceImpl(get()) }

    factory<ItemsRepository>(named("articlesRepo")) { ItemsRepositoryImpl(get(named("articlesDataSource"))) }
    factory<ItemsRepository>(named("blogsRepo")) { ItemsRepositoryImpl(get(named("blogsDataSource"))) }
    factory<ItemsRepository>(named("reportsRepo")) { ItemsRepositoryImpl(get(named("reportsDataSource"))) }

    factory<NewsSitesDataSource> { NewsSitesDataSourceImpl(get()) }
}