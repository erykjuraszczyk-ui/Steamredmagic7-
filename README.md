# Steam Droid

**Fanowski launcher Steam dla Androida 13+ i REDMAGIC 7.**  
Wersja projektu: **0.3.0 — Game Library Core**

Steam Droid ma być centrum uruchamiania gier Steam na telefonie. Projekt rozwijany jest w dwóch kierunkach:

1. **PC Streaming** — uruchamianie gry na komputerze i przesyłanie obrazu oraz sterowania na telefon przez oficjalny Steam Link / Steam Remote Play.
2. **Local Gaming** — przyszła integracja z lokalnymi środowiskami zgodności Windows na Androidzie (np. Winlator). To funkcja eksperymentalna; nie każda gra i nie każda wersja klienta Steam będzie działać.

## Co działa w 0.3.0

- Natywna aplikacja Android napisana w Kotlinie i Jetpack Compose.
- Interfejs w ciemnym motywie z czerwonym akcentem.
- Ekrany Start, Biblioteka, Połączenie i Ustawienia.
- Rozpoznawanie modelu urządzenia, wersji Androida, ABI i szacunkowej pamięci RAM.
- Przycisk otwierający zainstalowany Steam Link; jeśli aplikacji nie ma, otwiera jej kartę w sklepie.
- Diagnostyka aktywnej sieci: typ transportu, informacja o dostępie do internetu i statusie połączenia taryfowego.
- Wykrywanie podłączonych fizycznych kontrolerów gamepad / joystick / D-pad.
- Kontrola dostępności aplikacji Steam Link.
- Instrukcja konfiguracji Steam Remote Play.
- Lokalna biblioteka gier w Room/SQLite.
- Dodawanie i usuwanie pozycji, ulubione, filtrowanie i wyszukiwanie.
- Opcjonalny Steam App ID i notatki do gry.
- Profile PC Streaming i Local Windows (profil lokalny nie uruchamia jeszcze gry).
- Walidacja nazwy i Steam App ID oraz testy jednostkowe.
- GitHub Actions: automatyczna kompilacja debug APK i testy.

## Ważne ograniczenia

Steam Droid nie pobiera samodzielnie gier z serwerów Steam, nie zastępuje oficjalnego klienta Steam i nie omija DRM ani anti-cheat. W trybie PC gra jest instalowana i uruchamiana na komputerze. Telefon odbiera strumień przez Steam Link.

Wersja 0.3.0 nie loguje się do konta Steam i nie pobiera automatycznie listy zakupionych gier. Użytkownik dodaje pozycje ręcznie; są one zapisywane lokalnie w Room/SQLite. Profil PC Streaming otwiera oficjalny Steam Link, gdzie grę należy wybrać na komputerze. Profil Local Windows jest obecnie wpisem konfiguracyjnym — lokalny runtime nie jest jeszcze zintegrowany. Diagnostyka nie skanuje sieci lokalnej i nie potwierdza osiągalności konkretnego komputera — parowanie wykonuje Steam Link.

Steam i Steam Link są znakami towarowymi Valve. Steam Droid jest nieoficjalnym projektem fanowskim i nie jest powiązany z Valve.

## Wymagania

- Android Studio z obsługą Gradle 8.13.
- JDK 17.
- Android SDK Platform 36.
- Android 13 (API 33) lub nowszy do uruchomienia aplikacji.
- REDMAGIC 7 jest głównym urządzeniem testowym projektu.

## Kompilacja

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/erykjuraszczyk-ui/Steamredmagic7-.git
   ```
2. Otwórz katalog projektu w Android Studio.
3. Upewnij się, że Android Studio używa JDK 17 i ma zainstalowany Android SDK 36.
4. Zbuduj aplikację z menu **Build → Make Project** albo z terminala:
   ```bash
   gradle --no-daemon assembleDebug
   ```
5. APK znajdziesz w:
   `app/build/outputs/apk/debug/app-debug.apk`.

## Pierwsze uruchomienie

1. Zainstaluj aplikację Steam Link z Google Play lub oficjalnej strony Steam.
2. Na komputerze uruchom Steam i zaloguj się.
3. W Steam włącz **Ustawienia → Remote Play**.
4. Połącz REDMAGIC 7 i komputer z tą samą siecią Wi-Fi (zalecane 5 GHz); komputer najlepiej podłącz przewodowo do routera.
5. W Steam Droid otwórz zakładkę **Połączenie** i uruchom Steam Link.
6. Sparuj telefon z komputerem, a następnie wybierz grę w Steam Link.

## Struktura

- `app/` — aplikacja Android.
- `docs/ARCHITECTURE.md` — architektura i granice integracji.
- `.github/workflows/android.yml` — kompilacja CI.

## Plan rozwoju

Zobacz [docs/ROADMAP.md](docs/ROADMAP.md).

## Licencja

Kod projektu jest udostępniany na licencji MIT. Zobacz [LICENSE](LICENSE).
