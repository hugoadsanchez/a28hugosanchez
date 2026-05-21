# Laboratorio Bloque 4 - Parque Turistico de Dinosaurios

## Descripcion

Este proyecto es parte del laboratorio del Bloque 4 de la capacitacion de Java.

La idea del proyecto fue hacer una simulacion sencilla de un parque turistico de dinosaurios. En la simulacion se manejan turistas, dinosaurios, trabajadores, zonas del parque, eventos, ingresos, gastos y un monitoreo basico en consola.

El proyecto fue desarrollado como practica para aplicar varios temas vistos en clase, principalmente:

- Java
- Programacion orientada a objetos
- Maven
- Git y GitHub
- Pruebas unitarias
- Patrones de diseno
- Persistencia basica
- Buenas practicas de codigo

No es un sistema real de produccion, sino una simulacion hecha para practicar los conceptos del modulo.

## Herramientas utilizadas

Para desarrollar el proyecto utilice:

- Java 17
- Maven
- Git
- GitHub
- Visual Studio Code
- JUnit 5
- JaCoCo

Versiones usadas en mi equipo:

```text
Java 17.0.12
Apache Maven 3.9.14
Git 2.54.0.windows.1
VS Code 1.120.0
```

## Como ejecutar el proyecto

Primero se debe clonar el repositorio y entrar a la carpeta del proyecto.

Para compilar el proyecto:

```bash
mvn compile
```

Para ejecutar la simulacion:

```bash
mvn exec:java
```

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## Configuracion

La configuracion principal del proyecto se encuentra en el archivo:

```text
src/main/resources/park.properties
```

En este archivo se pueden cambiar algunos valores de la simulacion, por ejemplo:

```properties
simulation.seed=42
simulation.totalSteps=100
simulation.arrivalBatchSize=5

tourists=50
dinosaurs.carnivores=5
dinosaurs.herbivores=15

workers.guards=3
workers.technicians=2
workers.dailySalary=150.0
```

La semilla `simulation.seed` sirve para que los eventos se puedan repetir de forma similar cada vez que se ejecuta el programa.

## Estructura general

El proyecto esta separado en varios paquetes para mantener un poco mas ordenado el codigo:

```text
src/main/java/com/axity/dinosaurpark
```

Paquetes principales:

- `config`: carga la configuracion del archivo `park.properties`.
- `model`: contiene las clases principales como turistas, dinosaurios y trabajadores.
- `zone`: contiene las zonas del parque.
- `event`: contiene los eventos de la simulacion.
- `simulation`: contiene el motor principal de la simulacion.
- `persistence`: contiene la escritura de archivos CSV.
- `monitoring`: muestra informacion del parque en consola.

Las pruebas estan en:

```text
src/test/java/com/axity/dinosaurpark
```

## Funcionamiento general

De forma resumida, la simulacion hace lo siguiente:

1. Carga la configuracion del parque.
2. Crea turistas, dinosaurios, trabajadores y zonas.
3. Los turistas entran al parque por la zona de arribo.
4. Se registran ingresos por boletos y visitas.
5. Los turistas visitan zonas como el recinto central, banos y recintos de observacion.
6. Se generan algunos eventos como tormentas, apagones o escapes de dinosaurios.
7. Los trabajadores ayudan a controlar problemas del parque.
8. Se muestra un monitoreo en consola.
9. Se generan archivos CSV con ingresos, gastos y eventos.

## Archivos generados

Cuando se ejecuta la simulacion, se crea la carpeta:

```text
output/
```

Dentro se generan estos archivos:

```text
revenues.csv
expenses.csv
events.csv
```

Estos archivos guardan informacion de:

- Ingresos del parque.
- Gastos del parque.
- Eventos ocurridos durante la simulacion.

La carpeta `output/` no se sube al repositorio porque son archivos generados al ejecutar el programa.

## Pruebas unitarias

El proyecto tiene pruebas unitarias con JUnit.

Se probaron clases como:

- `Tourist`
- `Dinosaur`
- `PowerPlant`
- Zonas del parque
- Eventos
- Persistencia CSV
- Estado de la simulacion

El comando para ejecutar pruebas es:

```bash
mvn test
```

Resultado obtenido:

```text
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Tambien se usa JaCoCo para revisar la cobertura del codigo. La cobertura minima configurada es de 45%, y el proyecto ya cumple con esa validacion.

## Patrones de diseno usados

### Singleton

Use el patron Singleton en la clase:

```text
ParkConfig
```

Esta clase se encarga de cargar la configuracion del archivo `park.properties`. Lo hice asi para tener una sola instancia de configuracion durante la ejecucion.

### Strategy

Use el patron Strategy en los eventos de simulacion.

La interfaz principal es:

```text
SimulationEvent
```

Y algunas implementaciones son:

```text
DinosaurEscapeEvent
BlackoutEvent
StormEvent
```

La idea es que todos los eventos se puedan ejecutar de forma parecida, aunque cada uno tenga una logica diferente.

## Clases principales

Algunas clases importantes del proyecto son:

- `Main`: punto de entrada del programa.
- `SimulationEngine`: controla la simulacion principal.
- `ParkConfig`: carga configuracion.
- `ParkMonitor`: muestra el estado del parque.
- `CsvWriter`: escribe los archivos CSV.
- `Tourist`: representa un turista.
- `Dinosaur`: clase base para dinosaurios.
- `Guard` y `Technician`: trabajadores del parque.
- `PowerPlant`: representa la planta de energia.

## Buenas practicas aplicadas

Durante el desarrollo intente aplicar buenas practicas vistas en clase, como:

- Separar el codigo por paquetes.
- Usar nombres claros en clases y metodos.
- Evitar clases demasiado grandes cuando fue posible.
- Usar encapsulamiento con atributos privados.
- Usar enums para algunos estados.
- Agregar validaciones basicas.
- Agregar comentarios en partes importantes.
- Crear pruebas unitarias.
- Hacer commits pequenos y descriptivos.

Todavia hay cosas que se podrian mejorar, pero el proyecto cumple con el objetivo del laboratorio y me ayudo a practicar los temas vistos.

## Repositorio

Repositorio publico del proyecto:

```text
https://github.com/hugoadsanchez/a28hugosanchez
```

## Autor

Hugo Adrian Sanchez Ruiz  
Lista: A28





















