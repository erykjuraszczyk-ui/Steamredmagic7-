# Steam Droid — architektura

## Zasady nadrzędne

Steam Droid jest fanowskim launcherem i katalogiem gier. Nie implementuje własnego protokołu Steam Remote Play, nie pobiera zaszyfrowanych depotów Steam i nie zastępuje klienta Valve.

## UI
- Kotlin, Jetpack Compose i Material 3.
- Zakładki: Start, Biblioteka, Połączenie, Ustawienia.
- Ciemny motyw z czerwonym akcentem.

## PC Connect Core (0.2.0)
- Android ConnectivityManager / NetworkCapabilities.
- Wykrywanie podłączonych kontrolerów wejściowych.
- Steam Link odpowiada za wykrycie komputera, parowanie i streaming.
- Diagnostyka nie twierdzi, że telefon jest już połączony z komputerem.

## Game Library Core (0.3.0)
- Room 2.8.5 over SQLite.
- GameEntity przechowuje tytuł, Steam App ID, profil uruchamiania, notatki, ulubione i datę dodania.
- DAO udostępnia Flow i operacje CRUD.
- Katalog ręczny nie jest weryfikowaną biblioteką zakupów Steam.

## Steam Metadata & Artwork Core (0.4.0)

### Źródło metadanych
- Ręczne żądanie po numerycznym Steam App ID.
- Publiczny endpoint JSON strony sklepu Steam z ustawieniami regionu PL i języka polskiego.
- Endpoint nie jest traktowany jako stabilny kontrakt API dla zewnętrznych aplikacji.
- Timeouty i obsługa odpowiedzi HTTP; brak logowania, haseł, cookies i weryfikacji zakupu.
- Wpisy z ograniczeniem 18+ nie są importowane do metadanych.

### Persistencja
- Room DB przechodzi z wersji 1 do 2.
- Migracja dodaje pola metadanych bez usuwania istniejących wierszy.
- Grafiki są akceptowane wyłącznie z HTTPS i subdomen `steamstatic.com`.

### Grafiki
- Coil Compose 3 obsługuje pobieranie i buforowanie obrazów.
- Brak grafiki nie blokuje wyświetlania karty gry.

### Import i eksport
- JSON wybierany przez Android Storage Access Framework.
- Import limituje rozmiar i liczbę wpisów, waliduje nazwę oraz App ID, pomija duplikaty i nie nadpisuje katalogu.
- Dane nie są wysyłane do usługi zewnętrznej.

## Local Gaming — etap przyszły

Steam Droid → lokalny runtime zgodności → środowisko Windows.

Ta ścieżka jest eksperymentalna. Wymaga osobnych testów ARM64, translacji x86/x64, sterowników GPU, anty-cheat, licencji i konkretnej zgodności gier.

## Bezpieczeństwo
- Bez haseł Steam w aplikacji.
- Bez obchodzenia DRM lub anti-cheat.
- Bez nieautoryzowanego pobierania gier.
- Minimalne uprawnienia Android.
- Integracje zewnętrzne uruchamiane przez jawne intenty.
