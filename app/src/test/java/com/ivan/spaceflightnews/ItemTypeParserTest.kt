package com.ivan.spaceflightnews

import com.ivan.data.common.ItemsRepository
import com.ivan.spaceflightnews.common.ItemType
import com.ivan.spaceflightnews.screens.utils.ItemTypeParser
import org.junit.Test
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get


class ItemTypeParserTest: KoinTest {
    @Test
    fun itemTypeParser_correctStrings() {
        assert(ItemTypeParser.getRepoName(ItemType.Articles) == "articlesRepo")
        assert(ItemTypeParser.getRepoName(ItemType.Blogs) == "blogsRepo")
        assert(ItemTypeParser.getRepoName(ItemType.Reports) == "reportsRepo")
    }

    // In case someone forgets to put ArticlesDataSourceImpl back
    /*
    @Test
    fun itemTypeParser_correctDataSources() {
        val testRepo: ItemsRepository = get(named(ItemTypeParser.getRepoName(ItemType.Articles)))
        assert(testRepo.)
    }
    */
}