# Laboratorio Bloque 4 - Parque Turistico de Dinosaurios

## Descripcion general

Este proyecto corresponde al laboratorio del Bloque 4 del programa de capacitacion Java.

El sistema simula la operacion basica de un parque turistico de dinosaurios. Durante la simulacion se administran turistas, dinosaurios, trabajadores, zonas del parque, eventos aleatorios, ingresos, gastos y monitoreo general.

El objetivo principal es aplicar los temas vistos durante la formacion, como Java, Programacion Orientada a Objetos, Maven, pruebas unitarias, patrones de diseno, persistencia basica y buenas practicas de codigo.

## Tecnologias utilizadas

- Java 17
- Maven 3.9.14
- JUnit 5
- Mockito
- JaCoCo
- Git
- GitHub
- Visual Studio Code

## Requisitos previos

Para ejecutar el proyecto se requiere tener instalado:

```bash
java -version
mvn -version
git --version

Versiones usadas durante el desarrollo:

Java 17.0.12
Apache Maven 3.9.14
Git 2.54.0.windows.1
VS Code 1.120.0



Estructura del proyecto

src
├── main
│   ├── java
│   │   └── com.axity.dinosaurpark
│   │       ├── config
│   │       ├── event
│   │       ├── model
│   │       ├── monitoring
│   │       ├── persistence
│   │       ├── simulation
│   │       └── zone
│   └── resources
│       └── park.properties
└── test
    └── java
        └── com.axity.dinosaurpark

 La configuracion principal se encuentra en:

 src/main/resources/park.properties


 Ejemplo de parametros configurables:

 simulation.seed=42
simulation.totalSteps=100
simulation.arrivalBatchSize=5

tourists=50
dinosaurs.carnivores=5
dinosaurs.herbivores=15

workers.guards=3
workers.technicians=2
workers.dailySalary=150.0       

La semilla simulation.seed permite que la simulacion sea determinista. Esto significa que, usando la misma configuracion, los eventos se programan de forma repetible.

Ejecucion del proyecto

Para compilar:

mvn compile

Para ejecutar la simulacion:

mvn exec:java

Al ejecutar el sistema se muestra en consola el monitoreo del parque por cada paso de simulacion.


Archivos generados

La simulacion genera archivos CSV en la carpeta:

output/

Archivos generados:

revenues.csv
expenses.csv
events.csv

Estos archivos registran:

Ingresos por boletos, souvenirs, SPA y recintos.
Gastos por salarios, mantenimiento, reparaciones y eventos.
Eventos ocurridos durante la simulacion.

La carpeta output/ no se sube al repositorio porque contiene archivos generados en tiempo de ejecucion.


Pruebas unitarias

Para ejecutar las pruebas:

mvn test

El proyecto incluye pruebas unitarias para:

Turistas
Dinosaurios
Planta de energia
Zonas del parque
Eventos
Persistencia CSV
Estado de simulacion
Trabajadores
Registros auxiliares

Resultado esperado:

Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

Cobertura de pruebas

El proyecto utiliza JaCoCo para validar cobertura de codigo.

La cobertura minima configurada es: 45%

El comando mvn test valida automaticamente que se cumpla este porcentaje.

Patrones de diseno aplicados
Singleton

Se aplica en la clase:

ParkConfig

Esta clase centraliza la lectura del archivo park.properties y evita crear multiples instancias de configuracion durante la ejecucion.

Strategy

Se aplica mediante la interfaz:

SimulationEvent

Y sus implementaciones:

DinosaurEscapeEvent
BlackoutEvent
StormEvent

Cada evento tiene una forma distinta de ejecutarse, pero todos pueden ser tratados de manera uniforme por el motor de simulacion.

Componentes principales
Modelos

Representan las entidades principales del parque:

Tourist
Dinosaur
CarnivoreDinosaur
HerbivoreDinosaur
Worker
Guard
Technician
Ticket
SatisfactionSurvey
Zonas

Representan espacios dentro del parque:

ArrivalZone
CentralHub
BathroomZone
ObservationEnclosure
PowerPlant
Simulacion

El flujo principal se coordina desde:

SimulationEngine

Este componente crea las entidades, procesa llegadas, mueve turistas por zonas, ejecuta eventos, registra gastos e ingresos, y muestra el monitoreo del parque.

Monitoreo

La clase:

ParkMonitor

muestra en consola informacion como:

Paso actual
Turistas activos
Dinosaurios en recintos
Energia disponible
Estado de la planta
Ingresos acumulados
Gastos acumulados
Buenas practicas aplicadas

Durante el desarrollo se procuro aplicar buenas practicas vistas en el modulo de calidad de codigo:

Nombres claros en clases, metodos y variables.
Clases con responsabilidades separadas.
Encapsulamiento de atributos.
Uso de enums para evitar valores de texto libres.
Validaciones basicas en constructores.
Separacion entre modelo, simulacion, persistencia, eventos y monitoreo.
Uso de pruebas unitarias.
Uso de .gitignore para evitar subir archivos generados.
Commits pequenos y descriptivos.
Flujo general de la simulacion
Se carga la configuracion desde park.properties.
Se crean turistas, dinosaurios, trabajadores y zonas.
Los turistas entran por la zona de arribo.
Se registra venta de boletos.
Los turistas visitan recinto central, banos y recintos de observacion.
Se generan ingresos y gastos.
Se ejecutan eventos programados de forma determinista.
Los trabajadores atienden problemas del parque.
Se muestra monitoreo por consola.
Se generan archivos CSV al finalizar.
Repositorio

Repositorio publico utilizado para la validacion del laboratorio:

https://github.com/hugoadsanchez/a28hugosanchez
Autor

Hugo Adrian Sanchez Ruiz
Lista: A28






















