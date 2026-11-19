package pl.steamdroid.app

object GameInputValidator {
    fun validate(title: String, steamAppId: String): String? {
        val cleanedTitle = title.trim()
        val cleanedAppId = steamAppId.trim()

        if (cleanedTitle.isEmpty()) return "Podaj nazwę gry."
        if (cleanedTitle.length > 80) return "Nazwa gry może mieć maksymalnie 80 znaków."

        if (cleanedAppId.isNotEmpty()) {
            if (!cleanedAppId.all { it in '0'..'9' }) {
                return "Steam App ID może zawierać tylko cyfry."
            }
            val numericId = cleanedAppId.toLongOrNull()
            if (numericId == null || numericId <= 0L) {
                return "Steam App ID musi być liczbą większą od zera."
            }
        }
        return null
    }
}
