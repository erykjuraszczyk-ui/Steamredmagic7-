package pl.steamdroid.app

object LocalRuntimeProfileRules {
    const val NOT_TESTED = "NOT_TESTED"
    const val STARTS = "STARTS"
    const val PLAYABLE = "PLAYABLE"
    const val ISSUES = "ISSUES"
    const val DOES_NOT_START = "DOES_NOT_START"

    const val STABILITY = "STABILITY"
    const val BALANCED = "BALANCED"
    const val PERFORMANCE = "PERFORMANCE"

    val compatibilityStatuses = setOf(
        NOT_TESTED, STARTS, PLAYABLE, ISSUES, DOES_NOT_START
    )

    val runtimePresets = setOf(STABILITY, BALANCED, PERFORMANCE)

    fun normalizeStatus(value: String?): String =
        value?.takeIf { it in compatibilityStatuses } ?: NOT_TESTED

    fun normalizePreset(value: String?): String =
        value?.takeIf { it in runtimePresets } ?: BALANCED

    fun isValidNotes(value: String): Boolean = value.length <= 1_000
}
