package com.ivan.spaceflightnews

import androidx.test.platform.app.InstrumentationRegistry
import com.ivan.spaceflightnews.screens.utils.GreetingParser
import org.junit.Test

class GreetingParserTest {
    @Test
    fun testGreeting() {
        assert(
            GreetingParser.getGreeting(InstrumentationRegistry.getInstrumentation().targetContext, 0, "A").contains("Night")
        )
        assert(
            GreetingParser.getGreeting(InstrumentationRegistry.getInstrumentation().targetContext, 6, "A").contains("Morning")
        )
        assert(
            GreetingParser.getGreeting(InstrumentationRegistry.getInstrumentation().targetContext, 12, "A").contains("Afternoon")
        )
        assert(
            GreetingParser.getGreeting(InstrumentationRegistry.getInstrumentation().targetContext, 16, "A").contains("Evening")
        )
        assert(
            GreetingParser.getGreeting(InstrumentationRegistry.getInstrumentation().targetContext, 19, "A").contains("Night")
        )
    }
}