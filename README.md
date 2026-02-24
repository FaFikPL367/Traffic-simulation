# Traffic Simulation System
Jest to aplikacja przedstawiająca symulację **inteligentnych** świateł na skrzyżowaniu drogowym. Symulacja ta działa na zasadzie
analizowania natężenia ruchu na poszczególnych pasach skrzyżowania. Na podstawie tych danych oraz algorytmu wyznaczane jest
optymalne oraz dynamiczne zmienianie cykli świateł drogowych.

## Kluczowe funkcjonalności
1. Tworzenie własnych skrzyżowań z różnymi liczbami pasów, priorytetami pasów itp.
2. Dynamiczne wyznaczanie optymalnego cyklu światła na skrzyżowaniu 
3. Możliwość włączenia **auto symulacji** gdzie kolejne jej kroki są losowane

## Struktura projektu
```text
AdaptiveTrafficSignalControl
```text
AdaptiveTrafficSignalControl
|-- src
|   |-- main
|   |   |-- java
|   |   |   `-- org.project
|   |   |   |   |-- enums
|   |   |   |   |-- factory
|   |   |   |   |-- model
|   |   |   |   |   |-- command
|   |   |   |   |   |-- config
|   |   |   |   |   |-- dto
|   |   |   |   |   |-- Junction.java
|   |   |   |   |   |-- Lane.java
|   |   |   |   |   `-- Vehicle.java
|   |   |   |   |-- util
|   |   |   |   |   |-- ConflictFinder.java
|   |   |   |   |   |-- JsonParser.java
|   |   |   |   |   `-- TrafficController.java
|   |   |   |   |-- Main.java
|   |   |   |   `-- Simulation.java
|   |-- test
|-- pom.xml
|-- README.md
```

## Zasada działania
Podczas uruchamiania symulacji podajemy plik wejściowy oraz wyjściowy. Plik wejściowy zawiera opis konstrukcji skrzyżowania
oraz listę komend symulacji do wykonania. W pliku wyjściowym znajdują się wyniki symulacji po wykonaniu każdego jej kroku.

Algorytm decydujący o zmianie cykli świateł opiera się na kilku rzeczach:
1. Dla każdego pasa (droga może mieć wiele pasów) wyliczamy jego **koszt**. Wzór na koszt to: $(S * SW) + (V * VW)$.
    * **S** - to suma czasów oczekiwania wszystkich samochodów na danym pasie. Od momentu dodania samochodu do pasa wzrasta jego czas oczekiwania na opuszczenie skrzyżowania aż do momentu jego opuszczenia.
    * **SW** - to **waga** dla wartości **S**. Wartość z przedziału (0.0, 1.0).
    * **V** - to liczba samochodów na danym pasie.
    * **VW** - to **waga** dla wartości **V**. Wartość z przedziału (0.0, 1.0).
2. Każdy pas posiada również informacje czy ma on **priorytet**. W konfiguracji skrzyżowania określamy dla każdego pasa czy ma priorytet czy nie.

Algorytm opiera się na działaniu **kolejki priorytetowej** skonfigurowanej w taki sposób, aby brała ona pod uwagę parametry danego pasa w odpowiedni sposób.
Najważniejsza rzecz dla kolejki to **kiedy pas miał ostatnio zielone światło**. Pasy, które miały dawno zielone światło mają największe pierwszeństwo.

Następnie jeżeli czas kiedy pasy miały zielone światło jest taki sam, algorytm patrzy na ich wyliczone **koszty**. Koszt dla każdego pasa wyliczany jest
tak jak zostało to opisane wyżej. Obydwie wagi podajemy w pliku wejściowym. Ich wpływ ma znaczenie takie, że możemy 
chcieć, aby skrzyżowanie było bardziej sprawiedliwe, albo żeby pasy ze sporą ilością pojazdów miały pierwszeństwo.

Ostatnia rzecz, na którą patrzy algorytm w razie remisu to **priorytet** pasa podanego w pliku wejściowym. W każdej innej
sytuacji bierzemy pierwszy możliwy pas.

Oprócz tego powyższe rozwiązanie jest zoptymalizowane w taki sposób, że nie dajemy zielonego światła tylko dla znalezionego
"najlepszego" pasa. Z kolejki priorytetowej pobieramy najważniejszy pas, ale do niego staramy się znaleźć wszystkie
pasy **niekolizyjne** z nim. Niekolizyjny czyli takie, że możemy na każdym pasie dać zielone światło i nie ma mowy o wystąpieniu
kolizji na skrzyżowaniu.

Ostatnia rzecz, którą zapewnia algorytm to to, że na podstawie wszystkich znalezionych pasów gdzie żaden nie jest 
kolizyjny z innym, wyznaczamy ile **minimalnie** samochodów puszczamy na danym cyklu świateł zielonych. Dokładnie jest
to **połowa maksymalnej liczby samochodów na pasach**. Dzięki temu zapewniamy, że cykle świateł nie będą zmieniać się po
przejechaniu jednego samochodu (bo wagi pasów się zmienią) mimo, że możemy puścić ich więcej.

## Opis pliku wejściowego (JSON)
Poniżej znajduje się przykład jak taki plik powinien wyglądać:
```json
{
  "junction": {
  "roads": [
    {
      "roadOrientation": "BOTTOM",
      "lanes": [
        {
          "laneDirection": "STRAIGHT",
          "hasPriority": 0
        }
      ]
    }
  ]
  },
  "wages": {
    "waitingTimeWage": 0.5,
    "vehicleCountWage": 0.5
  },
  "command": {
  "commandList": [
    {
      "type": "addVehicle",
      "startRoadOrientation": "TOP",
      "endRoadOrientation": "RIGHT"
    },
    {
      "type": "step"
    },
    {
      "type": "autoSimulation",
      "numberOfSimulationSteps": 50,
      "minNumberOfVehicleToGenerate": 10,
      "maxNumberOfVehicleToGenerate": 15,
      "minSimulationStepInRow": 5,
      "maxSimulationStepInRow": 8
    }
  ]
  }
}
```
1. **Junction** - to część pliku gdzie podajemy jak dane skrzyżowanie ma wyglądać (dokładnie ile ma mieć dróg oraz jakie to drogi
oraz pasy dla każdej podanej drogi). Jesteśmy wstanie stworzyć różne konfiguracje gdzie np. odnoga skrzyżowania to droga jednokierunkowa,
mamy wiele pasów do jazdy w tym samym kierunku, różne kształty itp.
   * **roadOrientation** - to parametr mówiący nam o tym jaki będzie kształt skrzyżowania. To pole może mieć 4 wartości:
   BOTTOM, RIGHT, TOP, LEFT - gdzie każda reprezentuje inną odnogę skrzyżownia.
   * **lanes** - to lista pasów dla każdej odnogi skrzyżowania. Kierunek pas to 4 możliwe wartości: STRAIGHT.
   RIGHT, LEFT, BACKWARD - oraz musimy określić dla każdego pasa czy ma on priorytet (0 lub 1).
2. **Wages** - tutaj podajemy wartości dwóch wag z przedziału od 0.0 do 1.0 (bez wartości granicznych). Ważne jest to, że muszą się sumować do 1.0.
3. **Command** - tutaj podajemy **commandList**, która zawiera listę komend do wykonania. Komende rozpoznajemy po polu **type**:
   * **addVehicle** - dodajemy pojazd do skrzyżowania oraz podajemy z jakiej drogi i na którą chce on wjechać.
   * **step** - to wykonanie kroku symulacji i po każdym takim korku zapisujemy jakie pojazdy opuściły skrzyżowanie podczas tego kroku.
   * **autoSimulation** - pozwala nam na wygenerowanie listy komend na podstawie podanych parametrów:
     * **numberOfSimulationSteps** - ile komend **step** ma się wykonać (dokładnie tyle się wykona).
     * **minNumberOfVehicleToGenerate** i **maxNumberOfVehicleToGenerate** - określają one przedział, z którego będzie losowana liczba
      oznaczające ile samochodów z rzędu mamy dodać do skrzyżowania.
     * **minSimulationStepInRow** i **maxSimulationStepInRow** - określają one przedział, z którego będzie losowana liczba
         oznaczające ile komend **step** z rzędu mamy dodać do list komend do wykonania.

## Opis pliku wyjściowego (JSON)
```json
{
  "stepStatuses" : [ {
    "leftVehicles" : [ "1", "2", "13", "4" ],
    "stepId" : 1,
    "totalNumberOfVehicleOnJunction" : 10
  }, {
    "leftVehicles" : [ "5", "9" ],
    "stepId" : 2,
    "totalNumberOfVehicleOnJunction" : 8
  }, {
    "leftVehicles" : [ "6", "7", "8" ],
    "stepId" : 3,
    "totalNumberOfVehicleOnJunction" : 5
  }
  ]
}
```
1. **leftVehicles** - to lista ID pojazdów, które opuściły skrzyżowanie w danym kroku symulacji.
2. **stepId** - to unikatowy ID kroku symulacji.
3. **totalNumberOfVehicleOnJunction** - to liczba pojazdów, które jeszcze zostały na skrzyżowaniu po wykonaniu kroku symulacji.

## Technologie i narzędzia
Symulacja została napisana w **Java 25**, narzędziem wykorzystanym do budowania projektu i zarządzania zależnościami jest **Maven**.
Narzędziem do mapowania i walidacji plików JSON jest **Jackson**. Do testów został wykorzystany **Junit 5**. Dodatkowym narzędziem
użytym do usunięcia powtarzającego się kodu jest **Lombok**.

## Uruchomienie projektu
Projekt musi zostać rozpakowany i w folderze z projektem wpisujemy następujące komendy:

1. Budowanie pliku wykonywalnego (.jar wraz ze wszystkimi zależnościami)
```bash
mvn clean package
```

2. Uruchomienie symulacji (plik wejściowy musi znajdować się w folderze, w którym wykonujemy symulację)
```bash
java -jar target/traffic-simulation.jar input.json output.json
```
