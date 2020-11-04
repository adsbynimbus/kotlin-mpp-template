package template.mpp.core

import kotlin.test.Test
import kotlin.test.assertTrue

class IosGreetingTest {

    @Test
    fun testExample() {
        assertTrue(Platform().platform.contains("iOS"), "Check iOS is mentioned")
    }
}