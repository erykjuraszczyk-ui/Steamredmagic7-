package pl.steamdroid.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val RuntimeRed = Color(0xFFE04444)
private val RuntimeSurface = Color(0xFF171C23)
private val RuntimeSurfaceAlt = Color(0xFF202731)
private val RuntimeText = Color(0xFFF3F5F7)
private val RuntimeMuted = Color(0xFFA2ABB7)

@Composable
fun LocalEngineScreen(padding: PaddingValues) {
    val context = LocalContext.current
    var runtimeStatus by remember { mutableStateOf(WinlatorBridge.inspect(context)) }
    val hasArm64 = Build.SUPPORTED_ABIS.any { it == "arm64-v8a" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "STEAM DROID",
                color = RuntimeText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )
            Text(
                "LOKALNE URUCHAMIANIE WINDOWS",
                color = RuntimeRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = RuntimeSurfaceAlt),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("LOCAL GAMING ENGINE", color = RuntimeText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Steam Droid współpracuje z osobno zainstalowanym Winlatorem. Winlator uruchamia środowisko Windows; Steam Droid zapisuje profile i otwiera runtime.",
                    color = RuntimeMuted,
                    fontSize = 13.sp
                )
                RuntimeInfoLine("Winlator", if (runtimeStatus.installed) "WYKRYTY" else "NIEZAINSTALOWANY")
                if (runtimeStatus.installed) {
                    RuntimeInfoLine("Wersja", runtimeStatus.versionName ?: "Nieznana")
                }
                RuntimeInfoLine("ABI telefonu", Build.SUPPORTED_ABIS.firstOrNull() ?: "Nieznane")
                RuntimeInfoLine("ARM64", if (hasArm64) "DOSTĘPNE" else "BRAK")

                Button(
                    onClick = {
                        runtimeStatus = WinlatorBridge.inspect(context)
                        if (runtimeStatus.installed) {
                            val launched = WinlatorBridge.launch(context)
                            if (!launched) {
                                Toast.makeText(context, "Nie udało się uruchomić Winlatora.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            openWinlatorReleases(context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RuntimeRed)
                ) {
                    Text(
                        if (runtimeStatus.installed) "OTWÓRZ WINLATOR" else "OFICJALNE WYDANIA WINLATORA",
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(
                    onClick = { runtimeStatus = WinlatorBridge.inspect(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ODŚWIEŻ STATUS", color = RuntimeText)
                }
            }
        }

        RuntimeSectionTitle("JAK PRZYGOTOWAĆ GRĘ LOKALNĄ")
        RuntimeStep("1", "Zainstaluj runtime", "Zainstaluj Winlator z oficjalnego repozytorium projektu i uruchom go pierwszy raz, aby przygotował własne pliki.")
        RuntimeStep("2", "Utwórz kontener", "W Winlatorze utwórz kontener i dobierz ustawienia do konkretnej gry oraz układu graficznego.")
        RuntimeStep("3", "Dodaj skrót gry", "Skonfiguruj plik wykonywalny i skrót w samym Winlatorze, korzystając z legalnie posiadanych plików gry.")
        RuntimeStep("4", "Zapisz wynik testu", "Wróć do Biblioteki Steam Droid, otwórz profil lokalny i oznacz rezultat dopiero po własnym teście.")

        RuntimeSectionTitle("ZAKRES INTEGRACJI 0.5.0")
        RuntimeStatusCard(
            "Most do Winlatora",
            if (runtimeStatus.installed) "APLIKACJA WYKRYTA" else "OCZEKUJE NA INSTALACJĘ RUNTIME",
            "Steam Droid rozpoznaje standardowy pakiet Winlatora i może otworzyć jego ekran główny. Nie wysyła do niego komend uruchomienia konkretnego EXE ani nie steruje kontenerami."
        )
        RuntimeStatusCard(
            "Zgodność gier",
            "TESTY RĘCZNE",
            "Status gry zapisujesz sam po sprawdzeniu na urządzeniu. Sam profil nie jest dowodem, że gra działa ani gwarancją płynności."
        )
        Text(
            "Zgodność zależy od konkretnej gry, sterownika, ustawień kontenera i wersji runtime. Steam Droid nie omija zabezpieczeń i nie instaluje gier z pominięciem oficjalnych źródeł.",
            color = RuntimeMuted,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun LocalRuntimeProfileDialog(
    game: GameEntity,
    onDismiss: () -> Unit,
    onSave: (GameEntity) -> Unit
) {
    var status by rememberSaveable(game.id) {
        mutableStateOf(LocalRuntimeProfileRules.normalizeStatus(game.localCompatibility))
    }
    var preset by rememberSaveable(game.id) {
        mutableStateOf(LocalRuntimeProfileRules.normalizePreset(game.localRuntimePreset))
    }
    var notes by rememberSaveable(game.id) { mutableStateOf(game.localTestNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profil lokalny — " + game.title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("WYNIK TESTU", color = RuntimeMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = status == LocalRuntimeProfileRules.NOT_TESTED,
                        onClick = { status = LocalRuntimeProfileRules.NOT_TESTED },
                        label = { Text("Nie testowano") }
                    )
                    FilterChip(
                        selected = status == LocalRuntimeProfileRules.STARTS,
                        onClick = { status = LocalRuntimeProfileRules.STARTS },
                        label = { Text("Startuje") }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = status == LocalRuntimeProfileRules.PLAYABLE,
                        onClick = { status = LocalRuntimeProfileRules.PLAYABLE },
                        label = { Text("Grywalna") }
                    )
                    FilterChip(
                        selected = status == LocalRuntimeProfileRules.ISSUES,
                        onClick = { status = LocalRuntimeProfileRules.ISSUES },
                        label = { Text("Problemy") }
                    )
                }
                FilterChip(
                    selected = status == LocalRuntimeProfileRules.DOES_NOT_START,
                    onClick = { status = LocalRuntimeProfileRules.DOES_NOT_START },
                    label = { Text("Nie uruchamia się") }
                )

                Text("PROFIL BOX64 — NOTATKA UŻYTKOWNIKA", color = RuntimeMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = preset == LocalRuntimeProfileRules.STABILITY,
                        onClick = { preset = LocalRuntimeProfileRules.STABILITY },
                        label = { Text("Stability") }
                    )
                    FilterChip(
                        selected = preset == LocalRuntimeProfileRules.BALANCED,
                        onClick = { preset = LocalRuntimeProfileRules.BALANCED },
                        label = { Text("Balanced") }
                    )
                    FilterChip(
                        selected = preset == LocalRuntimeProfileRules.PERFORMANCE,
                        onClick = { preset = LocalRuntimeProfileRules.PERFORMANCE },
                        label = { Text("Performance") }
                    )
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it.take(1_000) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    label = { Text("Notatka z testu (opcjonalnie)") }
                )
                Text(
                    "Profil jest zapisywany w Steam Droid jako notatka. Nie zmienia automatycznie ustawień kontenera Winlatora.",
                    color = RuntimeMuted,
                    fontSize = 11.sp
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val timestamp = if (status == LocalRuntimeProfileRules.NOT_TESTED) {
                        null
                    } else {
                        System.currentTimeMillis()
                    }
                    onSave(
                        game.copy(
                            localCompatibility = status,
                            localRuntimePreset = preset,
                            localTestNotes = notes.trim(),
                            localTestedAt = timestamp
                        )
                    )
                }
            ) {
                Text("ZAPISZ PROFIL", color = RuntimeRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ANULUJ", color = RuntimeMuted)
            }
        }
    )
}

fun compatibilityLabel(value: String): String = when (value) {
    LocalRuntimeProfileRules.STARTS -> "Uruchamia się"
    LocalRuntimeProfileRules.PLAYABLE -> "Grywalna"
    LocalRuntimeProfileRules.ISSUES -> "Problemy"
    LocalRuntimeProfileRules.DOES_NOT_START -> "Nie uruchamia się"
    else -> "Nie testowano"
}

fun runtimePresetLabel(value: String): String = when (value) {
    LocalRuntimeProfileRules.STABILITY -> "Stability"
    LocalRuntimeProfileRules.PERFORMANCE -> "Performance"
    else -> "Balanced"
}

@Composable
private fun RuntimeInfoLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = RuntimeMuted, fontSize = 12.sp)
        Text(value, color = RuntimeText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RuntimeSectionTitle(value: String) {
    Text(value, color = RuntimeMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.1.sp)
}

@Composable
private fun RuntimeStatusCard(title: String, status: String, description: String) {
    Card(colors = CardDefaults.cardColors(containerColor = RuntimeSurface), shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(title, color = RuntimeText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(status, color = RuntimeRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(description, color = RuntimeMuted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun RuntimeStep(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(RuntimeSurface, RoundedCornerShape(16.dp)).padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(10.dp),
            color = RuntimeRed.copy(alpha = 0.18f)
        ) {
            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                Text(number, color = RuntimeRed, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = RuntimeText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(description, color = RuntimeMuted, fontSize = 12.sp)
        }
    }
}

private fun openWinlatorReleases(context: Context) {
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(WinlatorBridge.OFFICIAL_RELEASES_URL))
        )
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Nie można otworzyć oficjalnej strony wydań.", Toast.LENGTH_LONG).show()
    }
}
