package template.mpp.core

import kotlin.test.Test
import kotlin.test.assertTrue

class AndroidGreetingTest {

    @Test
    fun testExample() {
        assertTrue(Platform().platform.contains("Android"), "Check Android is mentioned")
    }
}