# Steam Droid — roadmapa

## 0.1.0 — Foundation (ukończona)
- [x] Bazowy projekt Android / Kotlin / Compose.
- [x] Ciemny interfejs i nawigacja.
- [x] Odczyt informacji o urządzeniu.
- [x] Uruchamianie Steam Link lub otwarcie sklepu.
- [x] CI budujące debug APK.

## 0.2.0 — PC Connect Core (ukończona)
- [x] Kontrola obecności Steam Link.
- [x] Diagnostyka aktywnego transportu sieciowego i dostępu do internetu.
- [x] Wykrywanie fizycznych kontrolerów gamepad / joystick / D-pad.
- [x] Skrót do ustawień Bluetooth.
- [x] GitHub Actions: pomyślny build debug APK.

## 0.3.0 — Game Library Core (implementacja)
- [x] Lokalna baza Room/SQLite.
- [x] Model GameEntity i DAO oparte na Flow.
- [x] Dodawanie i usuwanie wpisów biblioteki.
- [x] Opcjonalny Steam App ID i notatki.
- [x] Ulubione, wyszukiwanie i filtry.
- [x] Profile PC Streaming / Local Windows.
- [x] Walidacja danych i testy jednostkowe.
- [ ] Test ręczny CRUD i zachowania po restarcie na REDMAGIC 7.
- [ ] Synchronizacja biblioteki Steam (oddzielny etap bezpieczeństwa i API).

## 0.4.0 — Steam Metadata
- [ ] Bezpieczne wzbogacanie wpisów o metadane publiczne.
- [ ] Okładki gier i buforowanie obrazów.
- [ ] Import App ID / eksport biblioteki.
- [ ] Brak przechowywania hasła Steam i obchodzenia DRM.

## 0.5.0 — Local Runtime research
- [ ] Prototyp integracji z zewnętrznym runtime (bez bundlowania cudzych binariów bez licencji).
- [ ] Testy zgodności na REDMAGIC 7.
- [ ] Profile per gra i raportowanie ograniczeń.

## 1.0.0 — Stable
- [ ] Stabilna nawigacja i obsługa błędów.
- [ ] Testy na Androidzie 13+.
- [ ] Testy sieci, sterowania i cyklu życia aplikacji.
- [ ] Podpisany release APK / AAB.
- [ ] Dokumentacja użytkownika i polityka prywatności.
