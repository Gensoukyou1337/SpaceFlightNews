package com.ivan.spaceflightnews.screens.utils

import com.ivan.spaceflightnews.common.ItemType

// My Koin config has 3 ItemRepositoryImpls with different named() parameters
// this is to determine which named Repository is taken.
// Each of them has a different DataSource, also named() in Koin.
object ItemTypeParser {
    fun getRepoName(itemType: ItemType): String {
        return when(itemType) {
            ItemType.Articles -> "articlesRepo"
            ItemType.Blogs -> "blogsRepo"
            ItemType.Reports -> "reportsRepo"
        }
    }
}