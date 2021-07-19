# Tarea corta #3

Curso IC-6821 Diseño de software  
Profesor Diego Munguía Molina

## Objetivos ##

* Utilizar diagramas UML como guía de diseño para implementar software

## Guía de trabajo ##

Ya tenemos una propuesta de diseño para implementar nuestro juego de X/O. Esta propuesta ha sido plasmada en una serie
de diagramas UML que incluye diagramas de clase, de secuencia y de actividad.

El diagrama de clase describe la estructura general del juego, cuáles son sus distintos componentes (clases, interfaces 
y enumeraciones) y cómo se relacionan entre sí. 

El diagrama de secuencia describe el proceso de inyección de dependencias (estudiaremos más al respecto en el siguiente 
tema sobre principios de diseño): cómo se instancian y se enlazan los diferentes componentes. 

Hay diagramas de actividad para describir diferentes algoritmos que se utilizan en el juego: el ciclo principal del 
juego, el algoritmo de estrategia de juego automático aleatorio, y el algoritmo para intercambiar turnos entre 
jugadores. 

La propuesta de diseño está documentada en [DESIGN.md](DESIGN.md).

### Requerimiento 1 ###

Necesitamos implementar la propuesta de diseño.

Puesto que lo hemos venido trabajando en las tareas cortas anteriores, el repositorio ya contiene el código para 
`CountingBoard` y algunos componentes relacionados. En el paquete `board` sólo está pendiente de implementar la interfaz
`BoardState`.

También el repositorio ya tiene una implementación del ejecutable y ciclo principal del juego (`TicTacToe` y 
`TicTacToeOptions`).

Todos los demás componentes deben ser implementados.

## Aspectos operativos ##

- No modificar ningún archivo de código ya existente en el repositorio (excepto por `CountingBoard`).
- No modificar ninguna prueba automatizada.
- No modificar ningún chequeo de estilo. 
- No utilizar spanglish.
- Escribir código limpio.
- Las pruebas de código verifican todos los requerimientos funcionales.
- Como entrega se considera el último `push` de código al repositorio antes de la revisión del trabajo.
- Puede encontrar información sobre cómo corregir problemas identificados por los chequeos de estilo en el siguiente 
  enlace: https://checkstyle.sourceforge.io/checks.html 

## Rúbrica ##

#### Total 10 pts #### 

#### Entrega (2 pts) ####
- (2 pts) El historial del repositorio contiene commits significativos de la persona estudiante.
- (0 pts) El historial del repositorio no contiene commits significativos de la persona estudiante. Si no hay commits 
  significativos, el resto de criterios no serán aplicados.

#### Chequeo de estilo (2 pts) ####
- (2 pts) El código en el repositorio pasa todos los chequeos de estilo.
- (0 pts) El código en el repositorio no pasa todos los chequeos de estilo.

#### Chequeo de diseño (3 pts) ####
- (3 pts) El código en el repositorio pasa todos los chequeos de diseño.
- (1 pts) El código en el repositorio pasa la mayoría de los chequeos de diseño.
- (0 pts) El código en el repositorio no pasa los chequeos de diseño.

#### Pruebas de código (3 pts)
- (3 pts) El código en el repositorio pasa todas las pruebas de código.
- (1 pts) El código en el repositorio pasa la mayoría de las pruebas de código.
- (0 pts) El código en el repositorio no pasa las pruebas de código.

## Ambiente de desarrollo ##

Para correr todos los chequeos y pruebas automatizadas:

```bash
./gradlew clean check
```

Para generar el ejecutable

```bash
./gradlew clean installDist
```

Para correr el juego

```bash
build/install/IC-6821-TC3/bin/IC-6821-TC3 -n 1 -a2
```
