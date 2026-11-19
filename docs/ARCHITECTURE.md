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
