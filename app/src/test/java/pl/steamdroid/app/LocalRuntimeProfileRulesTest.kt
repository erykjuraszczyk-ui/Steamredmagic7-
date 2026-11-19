package pl.steamdroid.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalRuntimeProfileRulesTest {
    @Test
    fun acceptsKnownCompatibilityStates() {
        assertEquals(
            LocalRuntimeProfileRules.PLAYABLE,
            LocalRuntimeProfileRules.normalizeStatus(LocalRuntimeProfileRules.PLAYABLE)
        )
    }

    @Test
    fun defaultsUnknownCompatibilityState() {
        assertEquals(
            LocalRuntimeProfileRules.NOT_TESTED,
            LocalRuntimeProfileRules.normalizeStatus("MADE_UP")
        )
    }

    @Test
    fun defaultsUnknownRuntimePreset() {
        assertEquals(
            LocalRuntimeProfileRules.BALANCED,
            LocalRuntimeProfileRules.normalizePreset("ULTRA")
        )
    }

    @Test
    fun validatesProfileNotesLength() {
        assertTrue(LocalRuntimeProfileRules.isValidNotes("Short test note"))
        assertFalse(LocalRuntimeProfileRules.isValidNotes("x".repeat(1_001)))
    }
}
