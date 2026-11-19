# Steam Droid — roadmapa

## 0.1.0 — Foundation (ukończona)
- [x] Bazowy projekt Android / Kotlin / Compose.
- [x] Ciemny interfejs i nawigacja.
- [x] Odczyt informacji o urządzeniu.
- [x] Uruchamianie Steam Link.

## 0.2.0 — PC Connect Core (ukończona)
- [x] Kontrola obecności Steam Link.
- [x] Diagnostyka aktywnego transportu sieciowego i dostępu do internetu.
- [x] Wykrywanie kontrolerów gamepad / joystick / D-pad.
- [x] Skrót do ustawień Bluetooth.
- [x] Pomyślny build debug APK.

## 0.3.0 — Game Library Core (ukończona)
- [x] Lokalna baza Room/SQLite.
- [x] Dodawanie i usuwanie wpisów.
- [x] Steam App ID, notatki, ulubione, wyszukiwanie i filtry.
- [x] Profile PC Streaming / Local Windows.
- [x] Walidacja danych i testy jednostkowe.
- [x] Build APK i testy GitHub Actions.

## 0.4.0 — Steam Metadata & Artwork Core (implementacja)
- [x] Ręczne pobieranie publicznych metadanych sklepu po Steam App ID.
- [x] Okładki, krótki opis, gatunki, twórcy i data premiery.
- [x] Buforowanie obrazów Coil.
- [x] Migracja Room 1 → 2 bez kasowania istniejących gier.
- [x] Import i eksport JSON przez Android Storage Access Framework.
- [x] Odrzucanie metadanych wpisów oznaczonych jako 18+.
- [ ] Test ręczny importu/eksportu i migracji na REDMAGIC 7.
- [ ] Weryfikacja zachowania endpointu Store przy ograniczeniach zapytań.

## 0.5.0 — Local Runtime research
- [ ] Prototyp integracji z zewnętrznym runtime (bez bundlowania cudzych binariów bez licencji).
- [ ] Testy zgodności na REDMAGIC 7.
- [ ] Profile per gra i raportowanie ograniczeń.
- [ ] Jasny podział PC Streaming / Local Runtime.

## 1.0.0 — Stable
- [ ] Stabilna nawigacja i obsługa błędów.
- [ ] Testy na Androidzie 13+.
- [ ] Testy sieci, sterowania i cyklu życia aplikacji.
- [ ] Podpisany release APK / AAB.
- [ ] Dokumentacja użytkownika i polityka prywatności.
