# Steam Metadata & Artwork Core — Steam Droid 0.4.0

## Pobieranie metadanych
1. Dodaj grę do biblioteki z dodatnim Steam App ID.
2. Na karcie gry naciśnij **OPIS**.
3. Steam Droid pobierze publiczne dane strony sklepu.
4. Zapisze nazwę sklepową, krótki opis, grafikę nagłówkową, gatunki, twórców i datę premiery w Room.
5. Kolejne naciśnięcie **ODŚWIEŻ** ponownie pobierze dane.

Pobieranie jest ręczne, aby ograniczyć liczbę zapytań. Publiczny endpoint może zwrócić błąd, ograniczyć żądania albo zmienić format. Steam Droid pokazuje komunikat zamiast usuwać istniejące dane.

## Okładki
Grafika nagłówkowa jest ładowana przez Coil. Do bazy akceptowane są wyłącznie adresy HTTPS z domeny `steamstatic.com`. Jeżeli obraz nie jest dostępny, karta gry nadal pokazuje nazwę i dane tekstowe.

## Ograniczenia wiekowe
Jeżeli publiczne dane sklepu oznaczają tytuł jako 18+, aplikacja nie importuje jego metadanych. Import kopii JSON zatrzyma się, jeśli plik zawiera wpis z oznaczeniem 18+.

## Import i eksport
- Eksport: przycisk **EKSPORT**, wybierz miejsce zapisania pliku JSON.
- Import: przycisk **IMPORT**, wybierz wcześniejszą kopię JSON.
- Duplikaty App ID lub nazwy bez App ID są pomijane.
- Import nie nadpisuje istniejących pozycji.
- Kopia nie jest wysyłana do chmury.

## Migracja bazy
Aktualizacja bazy Room z wersji 1 do 2 dodaje kolumny metadanych. Istniejące wpisy biblioteki zostają zachowane.
