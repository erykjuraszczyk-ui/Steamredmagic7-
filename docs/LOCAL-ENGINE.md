# Local Gaming Engine — Steam Droid 0.5.0

## Zakres wersji

Steam Droid 0.5.0 dodaje most do osobno zainstalowanego Winlatora. Aplikacja:
- sprawdza standardowy pakiet Android `com.winlator`;
- pokazuje wykrytą wersję;
- otwiera Winlator przez jego główną aktywność;
- otwiera oficjalną stronę wydań, jeśli Winlator nie jest zainstalowany;
- zapisuje profil lokalnej zgodności dla wybranej gry.

## Konfiguracja Winlator

1. Otwórz zakładkę **Local Engine**.
2. Sprawdź, czy aplikacja została wykryta.
3. Jeżeli nie, przejdź do oficjalnych wydań projektu: https://github.com/brunodev85/winlator/releases
4. Po zainstalowaniu uruchom Winlator i poczekaj na jego własną konfigurację.
5. W Winlatorze utwórz kontener oraz skrót do legalnie posiadanej aplikacji/gry.
6. Uruchamiaj ją z poziomu Winlatora.

## Profil gry w Steam Droid

Dla wpisu z trybem **Local Windows** naciśnij **Konfiguruj profil lokalny** i zapisz:
- wynik testu: nie testowano, uruchamia się, grywalna, problemy, nie uruchamia się;
- preferencję profilu Box64: Stability, Balanced albo Performance;
- notatkę z testu.

Wartości profilu są notatkami i nie są automatycznie ustawiane w Winlatorze. Zmiana statusu na „Grywalna” oznacza tylko, że użytkownik ręcznie tak oznaczył test.

## Czego 0.5.0 nie robi

- Nie otwiera wybranego pliku EXE bezpośrednio.
- Nie tworzy ani nie konfiguruje kontenerów.
- Nie przekazuje ustawień Box64/DXVK/Turnip do Winlatora.
- Nie instaluje klienta Steam w Winlatorze.
- Nie zapewnia zgodności z każdą grą Windows lub Steam.
- Nie omija DRM, anti-cheat ani innych zabezpieczeń.

W standardowym Winlatorze pakiet aplikacji to `com.winlator`. Forki mogą używać innego identyfikatora pakietu i nie będą automatycznie wykrywane w tej wersji.
