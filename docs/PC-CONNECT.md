# PC Connect Core — Steam Droid 0.2.0

## Co pokazuje diagnostyka

### Sieć
Aplikacja odczytuje aktywną sieć Androida przez ConnectivityManager:
- typ transportu (Wi-Fi, Ethernet, sieć komórkowa, Bluetooth, VPN, inne),
- czy istnieje aktywna sieć,
- czy sieć deklaruje możliwość dostępu do internetu,
- czy Android zweryfikował łączność internetową,
- czy sieć jest taryfowa.

To nie jest test prędkości ani pomiar opóźnień. Stan `VALIDATED` oznacza, że Android potwierdził dostęp do internetu; nie dowodzi jakości streamingu ani dostępności komputera PC.

### Kontrolery
Aplikacja odczytuje urządzenia wejściowe widoczne dla Androida i filtruje fizyczne urządzenia zgłaszające źródła GAMEPAD, JOYSTICK lub DPAD. Wykrywanie nie gwarantuje zgodności każdego przycisku z każdą grą. Mapowanie wejścia w trakcie streamingu pozostaje odpowiedzialnością Steam Link.

### Steam Link
Aplikacja sprawdza, czy pakiet `com.valvesoftware.steamlink` jest widoczny i ma aktywność uruchomieniową. Jeśli nie, przycisk otwiera stronę instalacji. Parowanie i streaming odbywają się w Steam Link.

## Prywatność

Wersja 0.2.0 używa wyłącznie uprawnienia `ACCESS_NETWORK_STATE` do odczytu transportu i stanu sieci. Nie prosi o lokalizację, Bluetooth scan, hasło Steam ani dostęp do plików.

## Ograniczenia

- Nie skanuje podsieci.
- Nie wykrywa automatycznie hosta Steam.
- Nie mierzy ping do komputera.
- Nie odczytuje SSID ani hasła Wi-Fi.
- Nie steruje ani nie konfiguruje Steam Link w tle.
- Nie potwierdza, że komputer jest sparowany lub gotowy do streamingu.

To celowe: wykrywanie hosta i parowanie powinny zostać wykonane przez oficjalną aplikację Steam Link, zamiast tworzyć nieudokumentowany lub zawodny zamiennik.
