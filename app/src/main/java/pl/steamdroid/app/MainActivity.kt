package pl.steamdroid.app

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

private val SteamRed = Color(0xFFE04444)
private val SteamBackground = Color(0xFF0C0F13)
private val SteamSurface = Color(0xFF171C23)
private val SteamSurfaceAlt = Color(0xFF202731)
private val SteamText = Color(0xFFF3F5F7)
private val SteamMuted = Color(0xFFA2ABB7)
private const val STEAM_LINK_PACKAGE = "com.valvesoftware.steamlink"
private const val STEAM_LINK_STORE = "https://play.google.com/store/apps/details?id=com.valvesoftware.steamlink"
private const val STEAM_REMOTE_PLAY = "https://store.steampowered.com/remoteplay"
private const val STEAM_LINK_HELP = "https://help.steampowered.com/en/faqs/view/7112-CD02-7B57-59F8"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SteamDroidTheme {
                SteamDroidApp()
            }
        }
    }
}

private enum class AppTab(val label: String, val symbol: String) {
    HOME("Start", "⌂"),
    LIBRARY("Biblioteka", "▦"),
    CONNECT("Połączenie", "⌁"),
    LOCAL("Local Engine", "▣"),
    SETTINGS("Ustawienia", "⚙")
}

@Composable
private fun SteamDroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = SteamRed,
            onPrimary = Color.White,
            secondary = Color(0xFF7EA8E8),
            background = SteamBackground,
            surface = SteamSurface,
            onSurface = SteamText
        ),
        content = content
    )
}

@Composable
private fun SteamDroidApp() {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME) }

    Scaffold(
        containerColor = SteamBackground,
        bottomBar = {
            NavigationBar(containerColor = SteamSurface) {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Text(tab.symbol, fontSize = 20.sp) },
                        label = { Text(tab.label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SteamRed,
                            selectedTextColor = SteamText,
                            indicatorColor = SteamRed.copy(alpha = 0.16f),
                            unselectedIconColor = SteamMuted,
                            unselectedTextColor = SteamMuted
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            AppTab.HOME -> HomeScreen(padding)
            AppTab.LIBRARY -> LibraryScreen(padding)
            AppTab.CONNECT -> ConnectScreen(padding)
            AppTab.LOCAL -> LocalEngineScreen(padding)
            AppTab.SETTINGS -> SettingsScreen(padding)
        }
    }
}

@Composable
private fun ScreenColumn(
    padding: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        content()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun BrandHeader(subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "STEAM DROID",
            color = SteamText,
            fontSize = 27.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
        )
        Text(
            text = subtitle.uppercase(),
            color = SteamRed,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )
    }
}

@Composable
private fun HomeScreen(padding: PaddingValues) {
    val context = LocalContext.current
    ScreenColumn(padding) {
        BrandHeader("Fanowski launcher • REDMAGIC edition")

        Card(
            colors = CardDefaults.cardColors(containerColor = SteamSurfaceAlt),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("GRAJ W GRY PC NA TELEFONIE", color = SteamText, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Uruchom grę na swoim komputerze i przesyłaj obraz oraz sterowanie na REDMAGIC 7 przez Steam Link.",
                    color = SteamMuted,
                    fontSize = 14.sp
                )
                Button(
                    onClick = { openSteamLink(context) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
                ) {
                    Text("URUCHOM STEAM LINK", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { openWebPage(context, STEAM_REMOTE_PLAY) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ZOBACZ STEAM REMOTE PLAY", color = SteamText)
                }
            }
        }

        SectionTitle("SZYBKIE INFORMACJE")
        StatusCard(
            title = "PC Streaming",
            status = "GOTOWE DO KONFIGURACJI",
            description = "Steam Link działa jako osobna, oficjalna aplikacja. Steam Droid otwiera ją jednym przyciskiem."
        )
        StatusCard(
            title = "Biblioteka gier",
            status = "LOKALNA BAZA ROOM",
            description = "Zapisuj pozycje, wyszukuj gry i wybieraj profil PC Streaming lub Local Windows."
        )

        SectionTitle("TWOJE URZĄDZENIE")
        DeviceCard()
        Text(
            "Steam Droid nie jest oficjalnym produktem Valve. Nie wymaga podawania hasła do Steam.",
            color = SteamMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun LibraryScreen(padding: PaddingValues) {
    val context = LocalContext.current
    val dao = remember(context) { SteamDroidDatabase.getInstance(context).gameDao() }
    val games by dao.observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("ALL") }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var editingLocalProfile by remember { mutableStateOf<GameEntity?>(null) }
    var metadataBusyId by rememberSaveable { mutableStateOf<Long?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    val json = withContext(Dispatchers.IO) {
                        LibraryJsonTransfer.export(dao.getAll())
                    }
                    withContext(Dispatchers.IO) {
                        val output = context.contentResolver.openOutputStream(uri)
                            ?: throw IllegalStateException("Nie można otworzyć pliku do zapisu.")
                        output.use { it.write(json.toByteArray(Charsets.UTF_8)) }
                    }
                    Toast.makeText(context, "Kopia biblioteki została wyeksportowana.", Toast.LENGTH_LONG).show()
                } catch (error: Exception) {
                    Toast.makeText(
                        context,
                        error.message ?: "Eksport nie powiódł się.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    val importedGames = withContext(Dispatchers.IO) {
                        val input = context.contentResolver.openInputStream(uri)
                            ?: throw IllegalStateException("Nie można otworzyć wskazanego pliku.")
                        val json = input.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        LibraryJsonTransfer.decode(json)
                    }

                    var added = 0
                    var skipped = 0
                    importedGames.forEach { imported ->
                        val duplicate = if (imported.steamAppId != null) {
                            dao.countBySteamAppId(imported.steamAppId) > 0
                        } else {
                            dao.countByTitle(imported.title) > 0
                        }
                        if (duplicate) {
                            skipped++
                        } else {
                            dao.insert(imported.copy(id = 0))
                            added++
                        }
                    }
                    Toast.makeText(
                        context,
                        "Import zakończony. Dodano: " + added + ", pominięto duplikaty: " + skipped + ".",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (error: Exception) {
                    Toast.makeText(
                        context,
                        error.message ?: "Import nie powiódł się.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val filteredGames = games.filter { game ->
        val displayTitle = game.storeName ?: game.title
        val matchesSearch = searchQuery.isBlank() ||
            displayTitle.contains(searchQuery, ignoreCase = true) ||
            game.title.contains(searchQuery, ignoreCase = true) ||
            game.steamAppId.orEmpty().contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (filter) {
            "PC_STREAMING" -> game.launchMode == GameLaunchMode.PC_STREAMING.name
            "LOCAL" -> game.launchMode == GameLaunchMode.LOCAL.name
            "FAVORITES" -> game.isFavorite
            else -> true
        }
        matchesSearch && matchesFilter
    }

    ScreenColumn(padding) {
        BrandHeader("Twoja biblioteka gier")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(games.size.toString() + " gier", color = SteamText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Room • metadane • kopia zapasowa", color = SteamMuted, fontSize = 11.sp)
            }
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
            ) {
                Text("+ DODAJ")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { exportLauncher.launch("steam-droid-library.json") },
                modifier = Modifier.weight(1f)
            ) {
                Text("EKSPORT", color = SteamText)
            }
            OutlinedButton(
                onClick = { importLauncher.launch(arrayOf("application/json", "text/*")) },
                modifier = Modifier.weight(1f)
            ) {
                Text("IMPORT", color = SteamText)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Szukaj gry lub Steam App ID") }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(selected = filter == "ALL", onClick = { filter = "ALL" }, label = { Text("Wszystkie") })
            FilterChip(selected = filter == "PC_STREAMING", onClick = { filter = "PC_STREAMING" }, label = { Text("PC") })
            FilterChip(selected = filter == "LOCAL", onClick = { filter = "LOCAL" }, label = { Text("Lokalne") })
            FilterChip(selected = filter == "FAVORITES", onClick = { filter = "FAVORITES" }, label = { Text("★") })
        }

        if (filteredGames.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteamSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("▦", color = SteamRed, fontSize = 40.sp)
                    Text(
                        if (games.isEmpty()) "Twoja biblioteka jest pusta" else "Nie znaleziono gier",
                        color = SteamText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        if (games.isEmpty()) {
                            "Dodaj grę ręcznie lub zaimportuj kopię biblioteki Steam Droid."
                        } else {
                            "Zmień wyszukiwanie lub filtr, aby zobaczyć inne pozycje."
                        },
                        color = SteamMuted,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                    if (games.isEmpty()) {
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
                        ) {
                            Text("DODAJ PIERWSZĄ GRĘ")
                        }
                    }
                }
            }
        } else {
            filteredGames.forEach { game ->
                GameCard(
                    game = game,
                    metadataLoading = metadataBusyId == game.id,
                    onFavorite = {
                        scope.launch { dao.update(game.copy(isFavorite = !game.isFavorite)) }
                    },
                    onDelete = { scope.launch { dao.delete(game) } },
                    onEditLocalProfile = { editingLocalProfile = game },
                    onLaunch = {
                        when (game.launchMode) {
                            GameLaunchMode.PC_STREAMING.name -> {
                                Toast.makeText(
                                    context,
                                    "Wybierz " + game.title + " w bibliotece Steam Link na komputerze.",
                                    Toast.LENGTH_LONG
                                ).show()
                                openSteamLink(context)
                            }
                            else -> {
                                if (WinlatorBridge.launch(context)) {
                                    Toast.makeText(
                                        context,
                                        "Winlator uruchomiony. Wybierz kontener i skrót gry w aplikacji runtime.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Nie wykryto Winlatora. Otwieram oficjalne wydania.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    openWebPage(context, WinlatorBridge.OFFICIAL_RELEASES_URL)
                                }
                            }
                        }
                    },
                    onDetails = {
                        game.steamAppId?.let { appId ->
                            openWebPage(context, "https://store.steampowered.com/app/" + appId + "/")
                        }
                    },
                    onFetchMetadata = {
                        val appId = game.steamAppId
                        if (appId != null) {
                            scope.launch {
                                metadataBusyId = game.id
                                try {
                                    val metadata = SteamStoreRepository.fetch(appId)
                                    dao.update(
                                        game.copy(
                                            storeName = metadata.name,
                                            shortDescription = metadata.shortDescription,
                                            headerImageUrl = metadata.headerImageUrl,
                                            genres = metadata.genres.joinToString(" • "),
                                            developers = metadata.developers.joinToString(", "),
                                            releaseDate = metadata.releaseDate,
                                            requiredAge = metadata.requiredAge,
                                            metadataUpdatedAt = System.currentTimeMillis()
                                        )
                                    )
                                    Toast.makeText(context, "Pobrano metadane gry.", Toast.LENGTH_SHORT).show()
                                } catch (_: AgeRestrictedStoreMetadataException) {
                                    Toast.makeText(
                                        context,
                                        "Metadane tytułów z ograniczeniem 18+ nie są pobierane.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (error: Exception) {
                                    Toast.makeText(
                                        context,
                                        error.message ?: "Nie udało się pobrać metadanych.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } finally {
                                    metadataBusyId = null
                                }
                            }
                        }
                    }
                )
            }
        }

        Text(
            "Metadane pobierane są na żądanie z publicznej strony sklepu Steam. Ten endpoint nie jest gwarantowanym interfejsem dla aplikacji zewnętrznych i może ograniczać zapytania. Nie potwierdza własności gry.",
            color = SteamMuted,
            fontSize = 11.sp
        )
    }

    if (showAddDialog) {
        AddGameDialog(
            onDismiss = { showAddDialog = false },
            onSave = { newGame ->
                scope.launch {
                    val appId = newGame.steamAppId
                    val duplicate = appId != null && dao.countBySteamAppId(appId) > 0
                    if (duplicate) {
                        Toast.makeText(
                            context,
                            "Gra z tym Steam App ID jest już w bibliotece.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        dao.insert(newGame)
                    }
                }
                showAddDialog = false
            }
        )
    }

    editingLocalProfile?.let { game ->
        LocalRuntimeProfileDialog(
            game = game,
            onDismiss = { editingLocalProfile = null },
            onSave = { updated ->
                scope.launch { dao.update(updated) }
                editingLocalProfile = null
            }
        )
    }
}

@Composable
private fun GameCard(
    game: GameEntity,
    metadataLoading: Boolean,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onLaunch: () -> Unit,
    onDetails: () -> Unit,
    onFetchMetadata: () -> Unit,
    onEditLocalProfile: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteamSurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            if (game.headerImageUrl != null) {
                AsyncImage(
                    model = game.headerImageUrl,
                    contentDescription = "Grafika gry " + game.title,
                    modifier = Modifier.fillMaxWidth().height(104.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = SteamRed.copy(alpha = 0.18f)
                ) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        Text(
                            game.title.take(1).uppercase(),
                            color = SteamRed,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        game.storeName ?: game.title,
                        color = SteamText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        game.steamAppId?.let { "Steam App ID: " + it } ?: "Bez Steam App ID",
                        color = SteamMuted,
                        fontSize = 11.sp
                    )
                }
                TextButton(onClick = onFavorite) {
                    Text(if (game.isFavorite) "★" else "☆", color = SteamRed, fontSize = 20.sp)
                }
            }

            Surface(
                color = if (game.launchMode == GameLaunchMode.PC_STREAMING.name) Color(0xFF183B58) else Color(0xFF3B2B48),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    if (game.launchMode == GameLaunchMode.PC_STREAMING.name) "PC STREAMING" else "LOCAL WINDOWS",
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    color = SteamText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!game.shortDescription.isNullOrBlank()) {
                Text(
                    game.shortDescription,
                    color = SteamMuted,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (game.genres.isNotBlank()) {
                Text("Gatunki: " + game.genres, color = SteamMuted, fontSize = 11.sp)
            }
            if (game.developers.isNotBlank()) {
                Text("Twórcy: " + game.developers, color = SteamMuted, fontSize = 11.sp)
            }
            if (!game.releaseDate.isNullOrBlank()) {
                Text("Premiera: " + game.releaseDate, color = SteamMuted, fontSize = 11.sp)
            }
            if (game.requiredAge > 0) {
                Text("Minimalny wiek wg danych sklepu: " + game.requiredAge + "+", color = SteamMuted, fontSize = 10.sp)
            }
            if (game.notes.isNotBlank()) {
                Text(game.notes, color = SteamMuted, fontSize = 12.sp)
            }
            if (game.launchMode == GameLaunchMode.LOCAL.name) {
                Text(
                    "Zgodność: " + compatibilityLabel(game.localCompatibility) +
                        " • Profil: " + runtimePresetLabel(game.localRuntimePreset),
                    color = SteamMuted,
                    fontSize = 11.sp
                )
                if (game.localTestNotes.isNotBlank()) {
                    Text("Test: " + game.localTestNotes, color = SteamMuted, fontSize = 11.sp)
                }
                TextButton(onClick = onEditLocalProfile) {
                    Text("KONFIGURUJ PROFIL LOKALNY", color = SteamMuted, fontSize = 11.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onLaunch,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
                ) {
                    Text("URUCHOM")
                }
                if (game.steamAppId != null) {
                    OutlinedButton(
                        onClick = onFetchMetadata,
                        enabled = !metadataLoading
                    ) {
                        Text(
                            if (metadataLoading) "..." else if (game.metadataUpdatedAt == null) "OPIS" else "ODŚWIEŻ",
                            color = SteamText
                        )
                    }
                }
                TextButton(onClick = onDelete) {
                    Text("USUŃ", color = SteamMuted)
                }
            }
            if (game.steamAppId != null) {
                TextButton(onClick = onDetails) {
                    Text("OTWÓRZ STRONĘ STEAM", color = SteamMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun AddGameDialog(
    onDismiss: () -> Unit,
    onSave: (GameEntity) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var appId by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var selectedMode by rememberSaveable { mutableStateOf(GameLaunchMode.PC_STREAMING.name) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj grę do biblioteki") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; error = null },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Nazwa gry *") }
                )
                OutlinedTextField(
                    value = appId,
                    onValueChange = { appId = it; error = null },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Steam App ID (opcjonalnie)") }
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    label = { Text("Notatka (opcjonalnie)") }
                )
                Text("TRYB URUCHAMIANIA", color = SteamMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedMode == GameLaunchMode.PC_STREAMING.name,
                        onClick = { selectedMode = GameLaunchMode.PC_STREAMING.name },
                        label = { Text("PC Streaming") }
                    )
                    FilterChip(
                        selected = selectedMode == GameLaunchMode.LOCAL.name,
                        onClick = { selectedMode = GameLaunchMode.LOCAL.name },
                        label = { Text("Lokalny") }
                    )
                }
                error?.let { Text(it, color = SteamRed, fontSize = 12.sp) }
                Text(
                    "Tryb lokalny zapisuje tylko profil. Uruchamianie gier Windows lokalnie nie jest jeszcze dostępne.",
                    color = SteamMuted,
                    fontSize = 11.sp
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val validation = GameInputValidator.validate(title, appId)
                    if (validation != null) {
                        error = validation
                    } else {
                        onSave(
                            GameEntity(
                                title = title.trim(),
                                steamAppId = appId.trim().ifBlank { null },
                                launchMode = selectedMode,
                                notes = notes.trim()
                            )
                        )
                    }
                }
            ) {
                Text("ZAPISZ", color = SteamRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ANULUJ", color = SteamMuted)
            }
        }
    )
}

@Composable
private fun ConnectScreen(padding: PaddingValues) {
    val context = LocalContext.current
    var diagnostics by remember { mutableStateOf(DeviceDiagnostics.read(context)) }

    ScreenColumn(padding) {
        BrandHeader("Połącz telefon z komputerem")

        SectionTitle("DIAGNOSTYKA SIECI")
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamSurface),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoLine("Typ połączenia", diagnostics.network.transportName)
                InfoLine("Aktywna sieć", if (diagnostics.network.connected) "TAK" else "NIE")
                InfoLine("Dostęp do internetu", when {
                    diagnostics.network.internetValidated -> "POTWIERDZONY"
                    diagnostics.network.hasInternetCapability -> "NIEPOTWIERDZONY"
                    else -> "BRAK"
                })
                InfoLine(
                    "Sieć taryfowa",
                    diagnostics.network.isMetered?.let { if (it) "TAK" else "NIE" } ?: "NIEZNANA"
                )
                Text(
                    diagnostics.network.note,
                    color = SteamMuted,
                    fontSize = 12.sp
                )
                OutlinedButton(
                    onClick = { diagnostics = DeviceDiagnostics.read(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ODŚWIEŻ DIAGNOSTYKĘ", color = SteamText)
                }
            }
        }

        SectionTitle("STEAM LINK")
        StatusCard(
            title = "Oficjalna aplikacja Valve",
            status = if (diagnostics.steamLinkInstalled) "ZAINSTALOWANA" else "NIEZAINSTALOWANA",
            description = if (diagnostics.steamLinkInstalled) {
                "Steam Link został wykryty na tym urządzeniu. Uruchom go, aby sparować telefon z komputerem."
            } else {
                "Nie wykryto Steam Link. Otwórz sklep Google Play, zainstaluj aplikację i wróć tutaj."
            }
        )
        Button(
            onClick = { openSteamLink(context) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
        ) {
            Text(
                if (diagnostics.steamLinkInstalled) "URUCHOM STEAM LINK" else "ZAINSTALUJ STEAM LINK",
                fontWeight = FontWeight.Bold
            )
        }

        SectionTitle("KONTROLERY I URZĄDZENIA WEJŚCIOWE")
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamSurface),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoLine("Wykryte kontrolery", diagnostics.controllers.size.toString())
                if (diagnostics.controllers.isEmpty()) {
                    Text(
                        "Nie wykryto kontrolera gamepad/joystick. Sparuj kontroler Bluetooth lub podłącz go przez USB-C, a następnie odśwież diagnostykę.",
                        color = SteamMuted,
                        fontSize = 12.sp
                    )
                } else {
                    diagnostics.controllers.forEachIndexed { index, controller ->
                        Text(
                            "${index + 1}. ${controller.name} — ${controller.inputType}",
                            color = SteamText,
                            fontSize = 13.sp
                        )
                    }
                }
                OutlinedButton(
                    onClick = { openBluetoothSettings(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("USTAWIENIA BLUETOOTH", color = SteamText)
                }
            }
        }

        SectionTitle("KONFIGURACJA KOMPUTERA")
        StepCard("1", "Uruchom Steam", "Na komputerze uruchom klienta Steam i zaloguj się na swoje konto.")
        StepCard("2", "Włącz Remote Play", "Otwórz Steam → Ustawienia → Remote Play i włącz tę funkcję.")
        StepCard("3", "Przygotuj sieć", "Połącz telefon z Wi-Fi 5 GHz. Komputer najlepiej podłącz do routera przewodem Ethernet.")
        StepCard("4", "Sparuj urządzenia", "W Steam Link wybierz komputer. Potwierdź kod PIN wyświetlony na ekranie.")
        StepCard("5", "Uruchom grę", "Wybierz grę z biblioteki Steam na komputerze i rozpocznij strumieniowanie.")

        OutlinedButton(
            onClick = { openWebPage(context, STEAM_LINK_HELP) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OFICJALNA POMOC STEAM LINK", color = SteamText)
        }
        Text(
            "Diagnostyka sprawdza połączenie sieciowe telefonu i rozpoznane urządzenia wejściowe. Nie skanuje sieci lokalnej ani nie potwierdza, że komputer Steam jest osiągalny — parowanie wykonuje Steam Link.",
            color = SteamMuted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SettingsScreen(padding: PaddingValues) {
    val context = LocalContext.current
    ScreenColumn(padding) {
        BrandHeader("Konfiguracja aplikacji")
        SectionTitle("INFORMACJE")
        StatusCard("Nazwa", "STEAM DROID", "Fanowski launcher dla urządzeń Android 13+.")
        StatusCard("Wersja", "0.5.0 LOCAL GAMING ENGINE", "Integracja z zewnętrznym Winlatorem, profile lokalnej zgodności i rejestr ręcznych testów gier.")
        SectionTitle("PRYWATNOŚĆ I BEZPIECZEŃSTWO")
        StatusCard("Dane logowania", "NIE SĄ WYMAGANE", "Steam Droid nie prosi o hasło Steam. Metadane pobierane są osobno po wpisaniu App ID.")
        StatusCard("Połączenie", "STEAM LINK + DIAGNOSTYKA", "Steam Link odpowiada za parowanie i streaming. Steam Droid pokazuje stan sieci telefonu i wykryte kontrolery.")
        OutlinedButton(
            onClick = { openWebPage(context, STEAM_REMOTE_PLAY) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("STRONA STEAM REMOTE PLAY", color = SteamText)
        }
        Text(
            "Steam i Steam Link są znakami towarowymi Valve Corporation. Steam Droid nie jest powiązany z Valve Corporation.",
            color = SteamMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun DeviceCard() {
    val context = LocalContext.current
    val model = Build.MODEL.ifBlank { "Nieznany model" }
    val manufacturer = Build.MANUFACTURER.ifBlank { "Nieznany producent" }
    val androidVersion = Build.VERSION.RELEASE ?: "Nieznana"
    val apiLevel = Build.VERSION.SDK_INT
    val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "Nieznane"
    val ramText = remember {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(memoryInfo)
        String.format("%.1f GB", memoryInfo.totalMem / (1024.0 * 1024.0 * 1024.0))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SteamSurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            InfoLine("Urządzenie", "$manufacturer $model")
            InfoLine("Android", "$androidVersion (API $apiLevel)")
            InfoLine("Architektura", abi)
            InfoLine("Pamięć RAM", ramText)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = SteamMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )
}

@Composable
private fun StatusCard(title: String, status: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteamSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(title, color = SteamText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(status, color = SteamRed, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
            Text(description, color = SteamMuted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun StepCard(number: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SteamSurface, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(10.dp),
            color = SteamRed.copy(alpha = 0.18f)
        ) {
            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                Text(number, color = SteamRed, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = SteamText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(description, color = SteamMuted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = SteamMuted, fontSize = 12.sp)
        Text(value, color = SteamText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun openBluetoothSettings(context: Context) {
    try {
        context.startActivity(Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Nie można otworzyć ustawień Bluetooth.", Toast.LENGTH_LONG).show()
    }
}

private fun openSteamLink(context: Context) {
    val launchIntent = context.packageManager.getLaunchIntentForPackage(STEAM_LINK_PACKAGE)
    if (launchIntent != null) {
        context.startActivity(launchIntent)
        return
    }

    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$STEAM_LINK_PACKAGE")))
    } catch (_: ActivityNotFoundException) {
        openWebPage(context, STEAM_LINK_STORE)
    }
}

private fun openWebPage(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Nie znaleziono aplikacji do otwarcia linku.", Toast.LENGTH_LONG).show()
    }
}
