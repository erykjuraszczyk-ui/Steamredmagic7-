package pl.steamdroid.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SteamStoreUrlTest {
    @Test
    fun buildsPolishStoreMetadataUrl() {
        assertEquals(
            "https://store.steampowered.com/api/appdetails?appids=570&cc=pl&l=polish",
            SteamStoreUrl.buildRequestUrl("570")
        )
    }

    @Test
    fun rejectsInjectedQueryParameters() {
        assertThrows(IllegalArgumentException::class.java) {
            SteamStoreUrl.buildRequestUrl("570&cc=us")
        }
    }

    @Test
    fun rejectsZeroAppId() {
        assertThrows(IllegalArgumentException::class.java) {
            SteamStoreUrl.buildRequestUrl("0")
        }
    }
}
