package pl.steamdroid.app

object SteamStoreUrl {
    private val appIdPattern = Regex("^[0-9]{1,18}$")

    fun buildRequestUrl(appId: String): String {
        require(appIdPattern.matches(appId) && appId.toLongOrNull()?.let { it > 0L } == true) {
            "Steam App ID musi być dodatnią liczbą."
        }
        return "https://store.steampowered.com/api/appdetails?appids=" +
            appId + "&cc=pl&l=polish"
    }
}
