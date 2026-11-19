# Steam Droid — architektura 0.1.0

## Zasada nadrzędna

Steam Droid jest launcherem i centrum nawigacji. Nie implementuje własnego protokołu Steam Remote Play, nie pobiera zaszyfrowanych depotów Steam i nie próbuje zastępować klienta Valve.

## Moduły

### UI
- Jetpack Compose + Material 3.
- Zakładki: Start, Biblioteka, Połączenie, Ustawienia.
- Ciemny motyw z czerwonym akcentem.

### Device Info
- Odczyt publicznych właściwości urządzenia przez Android SDK.
- Bez roota i bez uprawnień do odczytu danych innych aplikacji.

### Steam Link Launcher
- Sprawdza, czy pakiet `com.valvesoftware.steamlink` jest dostępny.
- Gdy jest zainstalowany, otwiera go przez launch intent.
- Gdy go nie ma, otwiera stronę aplikacji w Google Play.
- Nie przekazuje danych logowania Steam.

## Docelowy przepływ PC Streaming

REDMAGIC 7 → Steam Droid → Steam Link → komputer z uruchomionym Steam → gra PC.

Komputer wykonuje grę. Steam Link przesyła strumień audio/wideo do telefonu i przekazuje wejście sterujące do komputera.

## Docelowy przepływ Local Gaming

Steam Droid → lokalny runtime zgodności → środowisko gry Windows.

Ta ścieżka jest eksperymentalna. Przed jej wdrożeniem należy osobno sprawdzić licencje, stabilność runtime, obsługę ARM64, translację x86/x64, sterowniki GPU, wymagania anti-cheat oraz ograniczenia Android Storage Access Framework. Nie wolno deklarować zgodności bez testów konkretnej gry.

## Bezpieczeństwo

- Bez haseł Steam w aplikacji.
- Bez obchodzenia DRM lub anti-cheat.
- Bez pobierania nieautoryzowanych kopii gier.
- Minimalne uprawnienia Android.
- Integracje zewnętrzne uruchamiane przez jawne intenty.


## PC Connect Core (0.2.0)

### Network diagnostics
- Uses Android ConnectivityManager and NetworkCapabilities.
- Reports active transport (Wi-Fi, Ethernet, cellular, Bluetooth, VPN or other).
- Separates NET_CAPABILITY_INTERNET from NET_CAPABILITY_VALIDATED.
- Shows metered/unmetered status where Android exposes it.
- Requires only ACCESS_NETWORK_STATE (normal permission).
- Does not read SSID, password, GPS location or scan the local network.

### Controller diagnostics
- Uses InputDevice.getDeviceIds() and physical input source flags.
- Lists non-virtual devices exposing gamepad, joystick or D-pad sources.
- No Bluetooth scan permission is requested; only devices already recognized by Android are listed.
- A refresh action re-reads the device list. A device may need reconnecting or app refresh to appear.

### PC reachability boundary
The app does not claim it has discovered or connected to the gaming PC. Steam Link handles computer discovery, pairing, authentication and streaming. Network diagnostics only describe the phone's active Android network.
