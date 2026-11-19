# Steam Droid

**Fanowski launcher Steam dla Androida 13+ i REDMAGIC 7.**  
Wersja projektu: **0.4.0 — Steam Metadata & Artwork Core**.

Steam Droid jest centrum biblioteki gier i dostępu do Steam na telefonie. Projekt rozwijany jest w dwóch kierunkach:

1. **PC Streaming** — gra uruchamia się na komputerze, a obraz i sterowanie są przesyłane na telefon przez oficjalny Steam Link / Steam Remote Play.
2. **Local Gaming** — przyszła integracja z lokalnymi środowiskami zgodności Windows na Androidzie (np. Winlator). To funkcja eksperymentalna i zależna od konkretnego tytułu.

## Co działa w 0.4.0

- Natywna aplikacja Android napisana w Kotlinie i Jetpack Compose.
- Ciemny interfejs z czerwonym akcentem.
- Ekrany Start, Biblioteka, Połączenie i Ustawienia.
- Informacje o urządzeniu oraz diagnostyka sieci.
- Wykrywanie kontrolerów gamepad / joystick / D-pad.
- Uruchamianie oficjalnego Steam Link.
- Lokalna biblioteka gier w Room/SQLite.
- Dodawanie, usuwanie, ulubione, wyszukiwanie i filtrowanie.
- Profile PC Streaming i Local Windows.
- Pobieranie publicznych metadanych sklepu Steam po wpisaniu App ID: nazwa, krótki opis, grafika, gatunki, twórcy i data premiery.
- Buforowanie obrazów przez Coil.
- Migracja bazy Room 1 → 2 zachowująca istniejące wpisy.
- Import i eksport biblioteki do JSON przez systemowy wybór plików.
- Testy jednostkowe i GitHub Actions budujące APK.

## Ważne ograniczenia

Steam Droid nie pobiera samodzielnie gier z serwerów Steam, nie zastępuje oficjalnego klienta i nie omija DRM ani anti-cheat. W trybie PC gra działa na komputerze, a telefon odbiera strumień.

Aplikacja nie loguje się do konta Steam i nie pobiera prywatnej listy zakupionych gier. Użytkownik dodaje pozycje do własnego katalogu. Metadane pobierane są ręcznie z publicznego endpointu strony sklepu Steam. To nie jest gwarantowany, udokumentowany kontrakt API dla aplikacji zewnętrznych: może ograniczać zapytania lub zmienić format odpowiedzi. Aplikacja pokazuje błąd i nie usuwa starych danych, gdy pobieranie się nie powiedzie.

Tytuły oznaczone przez dane sklepu jako 18+ nie są pobierane do metadanych ani importowane z kopii JSON. Wpis z App ID nie stanowi potwierdzenia zakupu ani prawa do gry.

Steam i Steam Link są znakami towarowymi Valve. Steam Droid jest nieoficjalnym projektem fanowskim i nie jest powiązany z Valve.

## Wymagania

- Android Studio z obsługą Gradle 8.13.
- JDK 17.
- Android SDK Platform 36.
- Android 13 (API 33) lub nowszy.
- REDMAGIC 7 jest głównym urządzeniem testowym.

## Kompilacja

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/erykjuraszczyk-ui/Steamredmagic7-.git
   ```
2. Otwórz katalog w Android Studio.
3. Ustaw JDK 17 i zainstaluj Android SDK 36.
4. Zbuduj aplikację:
   ```bash
   gradle --no-daemon testDebugUnitTest
   gradle --no-daemon assembleDebug
   ```
5. APK znajdziesz w:
   `app/build/outputs/apk/debug/app-debug.apk`.

## Pierwsze uruchomienie

1. Zainstaluj Steam Link z Google Play.
2. Na komputerze uruchom Steam i włącz **Ustawienia → Remote Play**.
3. Połącz telefon z Wi-Fi 5 GHz; komputer najlepiej przewodowo z routerem.
4. W Steam Droid otwórz zakładkę **Połączenie** i uruchom Steam Link.
5. Sparuj telefon z komputerem i wybierz grę w Steam Link.

## Import i eksport

W zakładce **Biblioteka** użyj przycisków **EKSPORT** i **IMPORT**. Dane są przetwarzane lokalnie. Import pomija duplikaty i nie nadpisuje istniejących wpisów. Kopię zapasową przechowuj w bezpiecznym miejscu.

## Dokumentacja

- `docs/ARCHITECTURE.md` — architektura aplikacji.
- `docs/GAME-LIBRARY.md` — katalog i profile gier.
- `docs/GAME-METADATA.md` — metadane, grafiki, kopie JSON i migracja.
- `.github/workflows/android.yml` — kompilacja CI i testy.

## Licencja

Kod projektu jest udostępniany na licencji MIT. Zobacz [LICENSE](LICENSE).
