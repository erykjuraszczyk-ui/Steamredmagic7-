# Steam Droid — roadmapa

## 0.1.0 — Foundation (ukończona)
- [x] Bazowy projekt Android / Kotlin / Compose.
- [x] Ciemny interfejs i nawigacja.
- [x] Odczyt informacji o urządzeniu.
- [x] Uruchamianie Steam Link.

## 0.2.0 — PC Connect Core (ukończona)
- [x] Kontrola obecności Steam Link.
- [x] Diagnostyka sieci i wykrywanie kontrolerów.
- [x] Skrót do ustawień Bluetooth.
- [x] Pomyślny build debug APK.

## 0.3.0 — Game Library Core (ukończona)
- [x] Lokalna baza Room/SQLite.
- [x] Dodawanie, usuwanie, ulubione, wyszukiwanie i filtry.
- [x] Steam App ID, notatki i profile uruchamiania.
- [x] Walidacja danych i testy jednostkowe.
- [x] Build APK i testy GitHub Actions.

## 0.4.0 — Steam Metadata & Artwork Core (ukończona)
- [x] Ręczne pobieranie publicznych metadanych sklepu po Steam App ID.
- [x] Okładki, krótki opis, gatunki i twórcy.
- [x] Buforowanie obrazów Coil.
- [x] Migracja Room 1 → 2.
- [x] Import i eksport JSON.
- [x] Build APK i testy GitHub Actions.

## 0.5.0 — Local Gaming Engine (implementacja)
- [x] Wykrywanie standardowego pakietu Winlator `com.winlator`.
- [x] Uruchamianie aplikacji Winlator lub otwarcie oficjalnych wydań.
- [x] Osobny ekran Local Engine.
- [x] Rejestr statusu ręcznego testu każdej gry.
- [x] Preferencja profilu Box64 i notatka testowa.
- [x] Migracja Room 2 → 3.
- [x] Zachowanie profili w kopii JSON.
- [x] Testy jednostkowe reguł profili.
- [ ] Ręczne testy na REDMAGIC 7: wykrycie Winlatora, start runtime, aktualizacja Room.
- [ ] Test osobnych tytułów i kontenerów; nie deklarować zgodności bez pomiarów.
- [ ] Nie ma bezpośredniego API uruchamiania EXE ani konfiguracji kontenerów.

## 0.6.0 — Launch Profiles
- [ ] Badanie wspieranego, udokumentowanego interfejsu integracji runtime.
- [ ] Bezpieczne przypinanie lokalnych skrótów do wpisów biblioteki, jeśli runtime to umożliwi.
- [ ] Diagnostyka kontenera i raporty wydajności tylko z wiarygodnych danych.
- [ ] Rozdzielenie metadanych Steam od lokalnie znalezionych aplikacji.

## 1.0.0 — Stable
- [ ] Stabilna nawigacja i obsługa błędów.
- [ ] Testy na Androidzie 13+ i REDMAGIC 7.
- [ ] Testy migracji bazy oraz kopii JSON.
- [ ] Podpisany release APK / AAB.
- [ ] Dokumentacja użytkownika i polityka prywatności.
