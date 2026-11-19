# Steam Droid — architektura

## Zasady nadrzędne

Steam Droid jest fanowskim launcherem i katalogiem. Nie implementuje protokołu Steam Remote Play, nie pobiera zaszyfrowanych depotów Steam i nie zastępuje klienta Valve.

## UI
- Kotlin, Jetpack Compose i Material 3.
- Zakładki: Start, Biblioteka, Połączenie, Local Engine, Ustawienia.

## PC Connect Core
- Android ConnectivityManager / NetworkCapabilities.
- Wykrywanie urządzeń wejściowych Android.
- Steam Link odpowiada za wykrycie komputera, parowanie i streaming.

## Game Library Core
- Room over SQLite.
- Katalog jest zarządzany ręcznie przez użytkownika; nie stanowi weryfikacji własności gry.

## Steam Metadata & Artwork Core
- Publiczne metadane pobierane ręcznie po Steam App ID.
- Coil ładuje grafiki z zaufanych HTTPS URL.
- Import/eksport JSON przez Android Storage Access Framework.

## Local Gaming Engine (0.5.0)

### Winlator bridge
- Sprawdza standardowy pakiet `com.winlator` przez Android PackageManager.
- Widoczność pakietu zadeklarowana w `<queries>`.
- Otwiera główną aktywność Winlatora przez launch intent.
- Jeśli pakietu brak, prowadzi do oficjalnej strony wydań.
- Nie dołącza binariów Winlatora, Wine, Box64, Mesa, Turnip ani DXVK.
- Nie próbuje uruchamiać EXE przez nieudokumentowane intent extras.

### Local profile model
- `localCompatibility`: ręczny wynik testu.
- `localRuntimePreset`: preferencja/notatka użytkownika (nie jest wysyłana do Winlatora).
- `localTestNotes`: notatka testowa.
- `localTestedAt`: czas ręcznego oznaczenia wyniku.
- Migracja Room 2 → 3 dodaje pola z wartościami domyślnymi i zachowuje istniejące rekordy.

### Boundaries
- Steam Droid może otworzyć aplikację Winlatora, lecz nie steruje kontenerami ani skrótami.
- Konfiguracja EXE i kontenerów pozostaje w Winlatorze.
- Status zgodności jest wpisem użytkownika i nie jest automatycznym benchmarkiem.
- Steam, DRM i anti-cheat zachowują własne wymagania; Steam Droid ich nie omija.

## Bezpieczeństwo
- Bez haseł Steam.
- Bez obchodzenia DRM lub anti-cheat.
- Bez nieautoryzowanego pobierania gier.
- Minimalne uprawnienia Android.
- Zewnętrzne runtime'y instalowane oddzielnie z oficjalnego źródła.
