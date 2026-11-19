package pl.steamdroid.app

import org.json.JSONArray
import org.json.JSONObject

object LibraryJsonTransfer {
    private const val FORMAT_VERSION = 1
    private const val MAX_IMPORT_BYTES = 2_000_000
    private const val MAX_GAMES = 1_000

    fun export(games: List<GameEntity>): String {
        val items = JSONArray()
        games.forEach { game ->
            items.put(
                JSONObject()
                    .put("title", game.title)
                    .put("steamAppId", game.steamAppId ?: JSONObject.NULL)
                    .put("launchMode", game.launchMode)
                    .put("notes", game.notes)
                    .put("isFavorite", game.isFavorite)
                    .put("addedAt", game.addedAt)
                    .put("storeName", game.storeName ?: JSONObject.NULL)
                    .put("shortDescription", game.shortDescription ?: JSONObject.NULL)
                    .put("headerImageUrl", game.headerImageUrl ?: JSONObject.NULL)
                    .put("genres", game.genres)
                    .put("developers", game.developers)
                    .put("releaseDate", game.releaseDate ?: JSONObject.NULL)
                    .put("requiredAge", game.requiredAge)
                    .put("metadataUpdatedAt", game.metadataUpdatedAt ?: JSONObject.NULL)
            )
        }

        return JSONObject()
            .put("format", "steam-droid-library")
            .put("formatVersion", FORMAT_VERSION)
            .put("exportedAt", System.currentTimeMillis())
            .put("games", items)
            .toString(2)
    }

    fun decode(json: String): List<GameEntity> {
        require(json.toByteArray(Charsets.UTF_8).size <= MAX_IMPORT_BYTES) {
            "Plik kopii zapasowej jest zbyt duży."
        }

        val root = JSONObject(json)
        require(root.optString("format") == "steam-droid-library") {
            "To nie jest kopia biblioteki Steam Droid."
        }
        require(root.optInt("formatVersion", -1) == FORMAT_VERSION) {
            "Nieobsługiwana wersja formatu kopii zapasowej."
        }

        val items = root.optJSONArray("games")
            ?: throw IllegalArgumentException("Plik nie zawiera listy gier.")
        require(items.length() <= MAX_GAMES) {
            "Kopia zapasowa zawiera zbyt wiele wpisów."
        }

        return buildList {
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index)
                    ?: throw IllegalArgumentException("Nieprawidłowy wpis gry w kopii zapasowej.")

                val title = item.optString("title", "").trim()
                val appId = if (item.isNull("steamAppId")) {
                    null
                } else {
                    item.optString("steamAppId", "").trim().takeIf { it.isNotBlank() }
                }

                val validation = GameInputValidator.validate(title, appId.orEmpty())
                require(validation == null) {
                    validation ?: "Nieprawidłowy wpis gry."
                }

                val requiredAge = item.optInt("requiredAge", 0).coerceAtLeast(0)
                require(requiredAge < 18) {
                    "Kopia zawiera wpis z ograniczeniem wiekowym 18+; import został zatrzymany."
                }

                val mode = item.optString("launchMode", GameLaunchMode.PC_STREAMING.name)
                    .takeIf {
                        it == GameLaunchMode.PC_STREAMING.name || it == GameLaunchMode.LOCAL.name
                    } ?: GameLaunchMode.PC_STREAMING.name

                val metadataUrl = SteamStoreRepository.trustedImageUrl(
                    if (item.isNull("headerImageUrl")) null else item.optString("headerImageUrl")
                )

                add(
                    GameEntity(
                        id = 0,
                        title = title,
                        steamAppId = appId,
                        launchMode = mode,
                        notes = item.optString("notes", "").take(1_000),
                        isFavorite = item.optBoolean("isFavorite", false),
                        addedAt = item.optLong("addedAt", System.currentTimeMillis())
                            .takeIf { it > 0 } ?: System.currentTimeMillis(),
                        storeName = nullableString(item, "storeName")?.take(120),
                        shortDescription = nullableString(item, "shortDescription")?.take(700),
                        headerImageUrl = metadataUrl,
                        genres = item.optString("genres", "").take(500),
                        developers = item.optString("developers", "").take(500),
                        releaseDate = nullableString(item, "releaseDate")?.take(100),
                        requiredAge = requiredAge.coerceAtMost(17),
                        metadataUpdatedAt = item.optLong("metadataUpdatedAt", 0L)
                            .takeIf { it > 0L }
                    )
                )
            }
        }
    }

    private fun nullableString(item: JSONObject, key: String): String? {
        return if (item.isNull(key)) null else item.optString(key, "").takeIf { it.isNotBlank() }
    }
}
