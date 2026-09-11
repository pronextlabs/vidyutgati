package com.vidyutgati

import org.junit.Assert.assertNotNull
import org.junit.Test

class ManifestClassIntegrityTest {

    @Test
    fun testManifestApplicationClassExists() {
        val appClass = Class.forName("com.vidyutgati.VidyutGatiApp")
        assertNotNull("VidyutGatiApp class must be loadable", appClass)
    }

    @Test
    fun testManifestMainActivityClassExists() {
        val activityClass = Class.forName("com.vidyutgati.MainActivity")
        assertNotNull("MainActivity class must be loadable", activityClass)
    }
}
