# Steam Droid

**Fanowski launcher Steam dla Androida 13+ i REDMAGIC 7.**  
Wersja projektu: **0.5.0 — Local Gaming Engine**.

Steam Droid jest centrum biblioteki gier i dostępu do Steam na telefonie. Projekt posiada dwa odrębne sposoby pracy:

1. **PC Streaming** — gra działa na komputerze, a obraz i sterowanie są przesyłane na telefon przez oficjalny Steam Link / Steam Remote Play.
2. **Local Gaming** — Steam Droid wykrywa i otwiera osobno zainstalowany Winlator. Konfiguracja kontenerów i skrótów EXE odbywa się wewnątrz Winlatora.

## Co działa w 0.5.0

- Natywna aplikacja Android napisana w Kotlinie i Jetpack Compose.
- Interfejs w ciemnym motywie z czerwonym akcentem.
- Ekrany Start, Biblioteka, Połączenie, Local Engine i Ustawienia.
- Informacje o urządzeniu oraz diagnostyka sieci.
- Wykrywanie kontrolerów gamepad / joystick / D-pad.
- Uruchamianie oficjalnego Steam Link.
- Lokalna biblioteka gier w Room/SQLite.
- Dodawanie, usuwanie, ulubione, wyszukiwanie i filtrowanie.
- Metadane i grafiki pobierane na żądanie po Steam App ID.
- Import i eksport biblioteki do JSON.
- Wykrywanie standardowego pakietu Winlator `com.winlator`.
- Otwieranie Winlatora, jeśli jest zainstalowany, albo strony oficjalnych wydań.
- Profile lokalne per gra: status ręcznego testu, preferowany preset Box64 i notatka.
- Migracja Room 2 → 3 zachowująca dotychczasowe wpisy.
- Testy jednostkowe i GitHub Actions budujące APK.

## Ważne ograniczenia Local Engine

Steam Droid 0.5.0 **nie uruchamia konkretnego pliku EXE wewnątrz Winlatora** i nie steruje kontenerami. Nie ma tu nieudokumentowanej automatyzacji ani kopiowania binariów Winlatora. Aplikacja otwiera zainstalowany runtime, a użytkownik konfiguruje kontener i skrót w Winlatorze.

Profile Box64 w Steam Droid są notatkami/preferencjami dla użytkownika. Nie są automatycznie przekazywane do ustawień Winlatora. Status zgodności jest ręcznym wynikiem testu, a nie gwarancją działania.

Winlator uruchamia część aplikacji Windows x86/x64 przez Wine i Box86/Box64. Zgodność zależy od gry, sterownika, ustawień kontenera, wymagań DRM/anti-cheat i wersji runtime. Nie każda gra Steam, klient Steam ani usługa sieciowa będzie działać w środowisku zgodności.

Steam Droid nie pobiera gier z pominięciem oficjalnych źródeł i nie omija DRM ani anti-cheat. Nie loguje się do konta Steam i nie pobiera prywatnej listy zakupów. Wpisy w katalogu są zarządzane przez użytkownika.

Steam i Steam Link są znakami towarowymi Valve. Winlator jest osobnym projektem. Steam Droid nie jest powiązany z Valve ani autorami Winlatora.

## Wymagania

- Android Studio z obsługą Gradle 8.13.
- JDK 17.
- Android SDK Platform 36.
- Android 13 (API 33) lub nowszy.
- REDMAGIC 7 jest głównym urządzeniem testowym.

## Kompilacja

```bash
gradle --no-daemon testDebugUnitTest
gradle --no-daemon assembleDebug
```

APK:
`app/build/outputs/apk/debug/app-debug.apk`.

## Konfiguracja Local Engine

1. Otwórz zakładkę **Local Engine**.
2. Sprawdź status Winlatora.
3. Jeśli nie jest zainstalowany, otwórz oficjalne wydania: https://github.com/brunodev85/winlator/releases
4. Zainstaluj runtime i uruchom go pierwszy raz, aby zakończył własną konfigurację.
5. W Winlatorze utwórz kontener i skrót do legalnie posiadanej gry/aplikacji.
6. W Steam Droid otwórz profil danej gry i zapisz wynik testu oraz notatki.
7. Pamiętaj: Steam Droid przechowuje profil, ale nie zmienia ustawień kontenera.

## Kopie biblioteki

W zakładce **Biblioteka** użyj przycisków **EKSPORT** i **IMPORT**. Kopia JSON zawiera również profil Local Engine. Import pomija duplikaty i nie nadpisuje istniejących wpisów.

## Dokumentacja

- `docs/ARCHITECTURE.md` — architektura aplikacji.
- `docs/GAME-LIBRARY.md` — katalog i profile gier.
- `docs/GAME-METADATA.md` — metadane, grafiki i kopie JSON.
- `docs/LOCAL-ENGINE.md` — integracja Winlator i ograniczenia.
- `.github/workflows/android.yml` — kompilacja CI i testy.

## Licencja

Kod Steam Droid jest udostępniany na licencji MIT. Zobacz [LICENSE](LICENSE). Winlator oraz pozostałe komponenty zewnętrzne zachowują własne licencje.
