# Slutuppgift - *To-Do List Management System*


## Om projektet
Detta projekt är en fullstack-applikation som låter användare hantera sina dagliga uppgifter genom en To-Do lista. 
Applikationen består av en frontend byggd med JavaFX för att skapa ett grafiskt användargränssnitt (GUI) och en backend som är byggd med Spring Boot för att hantera affärslogik och API-anrop.

#### *Projektets funktionalitet:*
* Lägg till nya uppgifter med titel, beskrivning och tidsperiod
* Visa uppgifter för olika delar av dagen: morgon, eftermiddag, kväll eller hela dagen
* Ändra en befintlig uppgifts titel, beskrivning och tidsperiod
* Radera en uppgift
* Markera en uppgift som färdig/utförd

## Så här kör du applikationen
För att köra hela applikationen behöver du både backend- och frontend-delen. Följ dessa steg:

#### *Backend (Spring Boot)*
1. Klona eller ladda ner backend-projektet
2. Öppna backend-projektet i en IDE (t.ex. IntelliJ IDEA)
3. Kör BackendApplication.java-klassen för att starta Spring Boot-servern
* Servern kommer att köras på http://localhost:8080

#### *Frontend (JavaFX)*
1. Klona eller ladda ner frontend-projektet
2. Öppna frontend-projektet i en IDE (t.ex. IntelliJ IDEA)
3. Kör FrontendApplication.java-klassen för att starta JavaFX-applikationen
* Frontend-applikationen kommer att kommunicera med backend-servern via HTTP-anrop

## Instruktioner för användning
När applikationen är igång kommer du att se nedanstående alternativ i användargränssnittet:

#### *"Add new task"*<br>
Fyll i titeln och beskrivningen för uppgiften i formuläret.<br>
Välj om uppgiften ska tillhöra morgonen, eftermiddagen eller kvällen genom att markera rätt radioknapp.<br>
Klicka på knappen "Add task" för att lägga till uppgiften.

#### *"Show lists"*<br>
*Morgonuppgifter:* Klicka på knappen "Morning tasks" för att visa alla uppgifter som är planerade för morgonen.<br>
*Eftermiddagsuppgifter:* Klicka på knappen "Afternoon tasks" för att visa alla uppgifter för eftermiddagen.<br>
*Kvällsuppgifter:* Klicka på knappen "Evening tasks" för att visa alla uppgifter som är planerade för kvällen.<br>
*Alla uppgifter:* Klicka på knappen "All tasks" för att visa alla uppgifter oavsett tidpunkt.

* För att *redigera en uppgift*, tryck på bildiconen under uppgiften som föreställer en penna (bildiconen längst till vänster i raden). Följ därefter instruktionerna i popup-rutorna som dyker upp.
* För att *radera en uppgift*, tryck på bildiconen under uppgiften som föreställer ett X (bildiconen i mitten av ikonerna). Följ därefter instruktionerna i popup-rutoran som dyker upp.
* För att *markera en uppgift som färdig/åtgärdad*, tryck på bildiconen under uppgiften som föreställer en "avcheckning" (bildiconen längst till höger i raden). Uppgiften kommer därefter strykas över. Om check-iknonen trycks på igen, kommer överstrykningen tas bort.

## Exempel på API-anrop

#### *GET /api/tasks*<br>
[<br>
  {<br>
  "id": 1,<br>
    "title": "Morgonträning",<br>
    "description": "Träna på gymmet",<br>
    "type": "Morning",<br>
    "checked": false<br>
  }<br>
  {<br>
    "id": 2,<br>
    "title": "Lunchmöte",<br>
    "description": "Möte med teamet",<br>
    "type": "Afternoon",<br>
    "checked": true<br>
  }<br>
]<br>

#### *POST /api/tasks*<br>
Content-Type: application/json<br>
Request:<br>
{<br>
  "title": "Kvällspromenad",<br>
  "description": "Ta en promenad i parken",<br>
  "type": "Evening",<br>
  "checked": false<br>
}<br>
Response:<br>
{<br>
  "id": 3,<br>
  "title": "Kvällspromenad",<br>
  "description": "Ta en promenad i parken",<br>
  "type": "Evening",<br>
  "checked": false<br>
}<br>

#### *PUT /api/tasks/3*<br>
Content-Type: application/json<br>
Request:<br>
{<br>
  "title": "Kvällspromenad",<br>
  "description": "Ta en promenad i parken",<br>
  "type": "Evening",<br>
  "checked": true<br>
}<br>
Response:<br>
{<br>
  "id": 3,<br>
  "title": "Kvällspromenad",<br>
  "description": "Ta en promenad i parken",<br>
  "type": "Evening",<br>
  "checked": true<br>
}<br>

#### *DELETE /api/tasks/3* <br>
{<br>
  "message": "Task with ID 3 was successfully deleted."<br>
}<br>


## Projektstruktur
Applikationen är uppdelad i två huvudsakliga delar:

#### *Backend*
Backend-delen är byggd med Spring Boot och består av följande klasser:
* BackendApplication: Startar Spring Boot-servern
* TaskController: Hanterar API-anrop för uppgifter (GET, POST, PUT, DELETE)
* TaskService: Hanterar all logik
* Task: Modelklass som representerar uppgifterna med egenskaper som titel, beskrivning och tidsperiod

#### *Frontend*
Den grafiska användargränssnittet är byggt med JavaFX och består av följande klasser:
* FrontendApplication: Startar JavaFX applikationen
* TaskController: Hanterar användarinteraktion och visning
* TaskManager: Hanterar logik för att hantera task-data
* TaskManagerInterface: Interface för TaskManager
* TaskService: Interagerar med backend och hanterar API-kommunikation
* Task: Abstrakt-klass för Task-objekten
* AfternoonTask: Task-objekt för "Afternoon"
* EveningTask: Task-objekt för "Evening"
* MorningTask: Task-objekt för "Morning"
* TaskFactory: Fabrik för att skapa olika typer av Task-objekt

## Övrigt
* Applikationen är skapad i IntelliJ IDEA 2024.2.3 med följande teknikstack:
  * Backend: Spring Boot 2.x
  * Frontend: JavaFX 23.0.1
* Backend-servern körs på http://localhost:8080
* Utskrifterna i programmet är på engelska
