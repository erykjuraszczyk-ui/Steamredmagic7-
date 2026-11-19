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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            title = "Gry lokalne Windows",
            status = "W PRZYGOTOWANIU",
            description = "Integracja z warstwą zgodności Android/Windows nie jest jeszcze częścią wersji 0.1.0."
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
    ScreenColumn(padding) {
        BrandHeader("Twoje gry")
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamSurface),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("▦", color = SteamRed, fontSize = 42.sp)
                Text("Biblioteka jest jeszcze pusta", color = SteamText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "Wersja 0.1.0 nie łączy się jeszcze z kontem Steam ani nie pobiera listy zakupionych gier. Możesz otworzyć Steam Link i korzystać z biblioteki na swoim komputerze.",
                    color = SteamMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { openSteamLink(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
                ) {
                    Text("OTWÓRZ STEAM LINK")
                }
            }
        }
        SectionTitle("PLANOWANE FUNKCJE")
        StatusCard("Import biblioteki", "PLANOWANE", "Pobieranie publicznych informacji o grach z legalnych źródeł i synchronizacja po bezpiecznym uwierzytelnieniu.")
        StatusCard("Profile gier", "PLANOWANE", "Osobne ustawienia sterowania i uruchamiania dla każdej gry.")
    }
}

@Composable
private fun ConnectScreen(padding: PaddingValues) {
    val context = LocalContext.current
    ScreenColumn(padding) {
        BrandHeader("Połącz telefon z komputerem")
        StatusCard(
            title = "Status połączenia",
            status = "NIEPOŁĄCZONO",
            description = "Połączenie i parowanie wykonuje oficjalna aplikacja Steam Link."
        )
        Button(
            onClick = { openSteamLink(context) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SteamRed)
        ) {
            Text("OTWÓRZ STEAM LINK", fontWeight = FontWeight.Bold)
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
            "Uwaga: gra działa na komputerze. Telefon odbiera obraz i wysyła sterowanie — nie pobiera w ten sposób plików gry na Androida.",
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
        StatusCard("Wersja", "0.1.0 FOUNDATION", "Pierwsza wersja bazowa. Funkcje konta Steam i lokalnego uruchamiania są poza zakresem tego wydania.")
        SectionTitle("PRYWATNOŚĆ I BEZPIECZEŃSTWO")
        StatusCard("Dane logowania", "NIE SĄ WYMAGANE", "Steam Droid nie prosi o hasło Steam i nie przechwytuje danych logowania.")
        StatusCard("Połączenie", "OFICJALNY STEAM LINK", "Parowanie z komputerem i streaming odbywają się w aplikacji Valve.")
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
