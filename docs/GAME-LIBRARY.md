# Game Library Core — Steam Droid 0.3.0

## Dodawanie gry
1. Otwórz zakładkę **Biblioteka**.
2. Naciśnij **Dodaj**.
3. Wpisz nazwę gry. To pole jest wymagane.
4. Opcjonalnie podaj numeryczny Steam App ID.
5. Wybierz profil PC Streaming lub Local Windows.
6. Możesz dodać własną notatkę.
7. Zapisz pozycję.

## Co jest przechowywane
Wpisy są zapisywane lokalnie w bazie Room/SQLite w prywatnym katalogu aplikacji. Nie są wysyłane na serwer. Odinstalowanie aplikacji usuwa jej prywatne dane, dlatego ważne wpisy warto w przyszłości eksportować.

## Wyszukiwanie i filtry
- Pole wyszukiwania sprawdza nazwę gry oraz Steam App ID.
- Filtry: wszystkie, PC Streaming, lokalne i ulubione.
- Ulubione wyświetlane są na początku listy.
- Można usunąć wpis.

## Uruchamianie
### PC Streaming
Przycisk Uruchom otwiera oficjalny Steam Link. Wybierz komputer i uruchom grę z biblioteki klienta Steam na PC. Steam Droid nie wysyła komendy startu konkretnej gry do Steam Link.

### Local Windows
Wpis lokalny przechowuje wyłącznie preferowany profil. Uruchamianie Windows przez Winlator nie jest jeszcze podłączone w wersji 0.3.0.

## Steam App ID
App ID musi zawierać dodatnią liczbę. Opcjonalny przycisk Steam otwiera publiczną kartę sklepu dla wpisanego identyfikatora. Sam identyfikator nie potwierdza zakupu ani prawa do gry.

## Prywatność
Nie ma logowania do konta Steam, nie ma hasła Steam, nie ma automatycznego importu prywatnej biblioteki. Katalog gier należy do użytkownika i jest przechowywany lokalnie.
