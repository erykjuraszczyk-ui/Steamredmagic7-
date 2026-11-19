package pl.steamdroid.app

import androidx.core.text.HtmlCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

data class SteamStoreMetadata(
    val name: String,
    val shortDescription: String,
    val headerImageUrl: String?,
    val genres: List<String>,
    val developers: List<String>,
    val releaseDate: String?,
    val requiredAge: Int
)

class AgeRestrictedStoreMetadataException : IOException(
    "Metadane tego tytułu nie są dostępne w katalogu Steam Droid."
)

object SteamStoreRepository {
    suspend fun fetch(appId: String): SteamStoreMetadata = withContext(Dispatchers.IO) {
        val requestUrl = SteamStoreUrl.buildRequestUrl(appId)
        val connection = (URL(requestUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 12_000
            instanceFollowRedirects = true
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "SteamDroid/0.4.0 (Android)")
        }

        try {
            val status = connection.responseCode
            if (status !in 200..299) {
                throw IOException("Steam Store zwrócił HTTP " + status + ". Spróbuj ponownie później.")
            }

            val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            if (body.length > 2_000_000) {
                throw IOException("Odpowiedź ze sklepu jest zbyt duża.")
            }

            val root = JSONObject(body)
            val appResult = root.optJSONObject(appId)
                ?: throw IOException("Nie znaleziono danych dla tego Steam App ID.")
            if (!appResult.optBoolean("success", false)) {
                throw IOException("Steam Store nie udostępnił danych dla tego App ID.")
            }

            val data = appResult.optJSONObject("data")
                ?: throw IOException("Nieprawidłowa odpowiedź Steam Store.")
            val requiredAge = data.optInt("required_age", 0)
            if (requiredAge >= 18) {
                throw AgeRestrictedStoreMetadataException()
            }

            val name = data.optString("name", "").trim()
            if (name.isBlank()) {
                throw IOException("Steam Store nie zwrócił nazwy gry.")
            }

            val rawDescription = data.optString("short_description", "")
            val description = HtmlCompat.fromHtml(
                rawDescription,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString().trim().take(700)

            val genres = readObjectStringArray(data.optJSONArray("genres"), "description")
            val developers = readStringArray(data.optJSONArray("developers"))
            val releaseDate = data.optJSONObject("release_date")
                ?.optString("date", "")
                ?.trim()
                ?.takeIf { it.isNotBlank() }

            SteamStoreMetadata(
                name = name.take(120),
                shortDescription = description,
                headerImageUrl = trustedImageUrl(data.optString("header_image", "")),
                genres = genres.take(12),
                developers = developers.take(8),
                releaseDate = releaseDate,
                requiredAge = requiredAge.coerceIn(0, 17)
            )
        } finally {
            connection.disconnect()
        }
    }

    fun trustedImageUrl(value: String?): String? {
        if (value.isNullOrBlank()) return null
        return try {
            val uri = URI(value.trim())
            val host = uri.host?.lowercase() ?: return null
            if (uri.scheme == "https" && host.endsWith(".steamstatic.com")) {
                uri.toASCIIString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun readObjectStringArray(
        array: org.json.JSONArray?,
        field: String
    ): List<String> {
        if (array == null) return emptyList()
        return buildList {
            for (index in 0 until array.length()) {
                val value = array.optJSONObject(index)?.optString(field, "")?.trim()
                if (!value.isNullOrBlank()) add(value.take(60))
            }
        }
    }

    private fun readStringArray(array: org.json.JSONArray?): List<String> {
        if (array == null) return emptyList()
        return buildList {
            for (index in 0 until array.length()) {
                val value = array.optString(index, "").trim()
                if (value.isNotBlank()) add(value.take(80))
            }
        }
    }
}
