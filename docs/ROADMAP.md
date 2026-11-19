# Steam Droid — roadmapa

## 0.1.0 — Foundation (ukończona)
- [x] Bazowy projekt Android / Kotlin / Compose.
- [x] Ciemny interfejs i nawigacja.
- [x] Odczyt informacji o urządzeniu.
- [x] Uruchamianie Steam Link lub otwarcie sklepu.
- [x] Instrukcja parowania PC.
- [x] CI budujące debug APK.
- [ ] Test kompilacji na fizycznym REDMAGIC 7.

## 0.2.0 — PC Connect Core (implementacja)
- [x] Kontrola obecności Steam Link przez Android package visibility.
- [x] Diagnostyka aktywnego transportu sieciowego.
- [x] Rozróżnienie aktywnej sieci, dostępu do internetu i statusu taryfowego.
- [x] Wykrywanie fizycznych kontrolerów gamepad / joystick / D-pad przez Android InputDevice API.
- [x] Skrót do ustawień Bluetooth i ręczne odświeżanie diagnostyki.
- [x] Wyjaśnienie granic diagnostyki — bez pozorowania wykrycia komputera Steam.
- [ ] Test na fizycznym REDMAGIC 7 i kontrolerach Bluetooth/USB.

## 0.3.0 — Library
- [ ] Projekt modelu biblioteki i lokalnej bazy Room.
- [ ] Import metadanych gier bez przechowywania danych logowania.
- [ ] Obsługa pustych, błędnych i prywatnych odpowiedzi.
- [ ] Oznaczanie trybu PC Streaming / Local.

## 0.4.0 — Local Runtime research
- [ ] Prototyp integracji z zewnętrznym runtime (bez bundlowania cudzych binariów bez licencji).
- [ ] Testy zgodności na REDMAGIC 7.
- [ ] Profile per gra i raportowanie ograniczeń.
- [ ] Brak automatycznego pobierania gier z pominięciem oficjalnego klienta.

## 1.0.0 — Stable
- [ ] Stabilna nawigacja i obsługa błędów.
- [ ] Testy na Androidzie 13+.
- [ ] Testy sieci, sterowania i cyklu życia aplikacji.
- [ ] Podpisany release APK / AAB.
- [ ] Dokumentacja użytkownika i polityka prywatności.
