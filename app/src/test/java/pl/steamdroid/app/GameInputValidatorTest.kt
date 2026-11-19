package pl.steamdroid.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameInputValidatorTest {
    @Test
    fun rejectsBlankTitle() {
        assertEquals("Podaj nazwę gry.", GameInputValidator.validate("   ", ""))
    }

    @Test
    fun acceptsTitleWithoutSteamAppId() {
        assertNull(GameInputValidator.validate("My Game", ""))
    }

    @Test
    fun rejectsNonNumericSteamAppId() {
        assertEquals(
            "Steam App ID może zawierać tylko cyfry.",
            GameInputValidator.validate("My Game", "12abc")
        )
    }

    @Test
    fun rejectsZeroSteamAppId() {
        assertEquals(
            "Steam App ID musi być liczbą większą od zera.",
            GameInputValidator.validate("My Game", "0")
        )
    }

    @Test
    fun acceptsPositiveSteamAppId() {
        assertNull(GameInputValidator.validate("My Game", "570"))
    }
}
