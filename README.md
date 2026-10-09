## Creadores
<a href="https://github.com/anmaribaphomet"> @anmaribaphomet</a><br>
<a href="https://github.com/MushCay"> @MushCay</a><br>
<a href="https://github.com/MushCay"> @IsHectron</a><br>
# Gaming League
## Descripcion
Sistema de gestión diseñado para administrar información relacionada con torneos de videojuegos, 
permitiendo organizar datos de jugadores, equipos, ligas, juegos, encuentros y clasificaciones.
El sistema utiliza una arquitectura organizada por responsabilidades, separando la interfaz gráfica, el manejo de eventos y el acceso a los datos.
Proyecto desarrollado para la materia **Base de Datos**, durante el semestre 2025-2.

## Interfaz Gráfica
<img width="921" height="689" alt="image" src="https://github.com/user-attachments/assets/88fe0962-e5ea-4a0e-af22-2453ca2fac27" />
<img width="921" height="187" alt="image" src="https://github.com/user-attachments/assets/3400f862-8803-4c0a-899b-e2d8a47d2c94" />
<img width="497" height="377" alt="image" src="https://github.com/user-attachments/assets/009b79f1-1f0b-4554-938a-a8b18270886b" />
<img width="763" height="402" alt="image" src="https://github.com/user-attachments/assets/14b0a043-b077-4e6c-8adb-73f166c80302" />


##  Funcionalidades principales
* **Partidas (Matches):** consulta y gestión de partidas, fechas, juegos y resultados.
* **Jugadores (Players):** visualización, registro y actualización de información de los jugadores.
* **Rankings:** consulta y administración de las clasificaciones de los jugadores por videojuego.
* **Videojuegos (Games):** gestión del catálogo de juegos, incluyendo nombre, descripción, plataforma y categoría.
* **Ligas (Leagues):** consulta y administración de ligas registradas.
* **Ligas y videojuegos:** gestión de las asociaciones entre ligas y juegos.
* **Equipos (Teams):** administración de equipos, fechas, integrantes y estadísticas.
* **Jugadores por equipo:** gestión de las relaciones entre jugadores y equipos, incluyendo sus periodos de participación.

### Operaciones disponibles

* **SELECT:** consulta y visualización de registros.
* **INSERT:** incorporación de nuevos registros.
* **UPDATE:** modificación de información existente.
* **DELETE:** eliminación de registros seleccionados o de relaciones, según el módulo.

## Tecnologías utilizadas

| Tecnología        | Uso                                                 |
| ----------------- | --------------------------------------------------- |
| Java              | Lenguaje de programación principal.                 |
| Java Swing        | Desarrollo de la interfaz gráfica de escritorio.    |
| PostgreSQL        | Sistema gestor de base de datos relacional.         |
| JDBC              | Comunicación entre Java y PostgreSQL.               |
| Gradle            | Gestión de dependencias y compilación del proyecto. |
| PreparedStatement | Ejecución de consultas SQL parametrizadas.          |

##  Arquitectura del sistema

El proyecto organiza sus responsabilidades en diferentes clases:

* **`Main`:** inicializa la ventana principal y construye los menús de navegación.
* **`Eventos`:** coordina las acciones del usuario, las consultas SQL y las operaciones sobre los datos.
* **`Database`:** centraliza la conexión con PostgreSQL y la ejecución de consultas.
* **`JDBCTableAdapter`:** transforma los resultados de las consultas SQL en un modelo compatible con las tablas de Swing.
* **`TablaSelects`:** muestra los resultados en ventanas internas y coordina las acciones de inserción, actualización y eliminación.
* **Formularios `FrmInsert...` y `FrmUpdate...`:** permiten registrar y modificar información mediante formularios gráficos.
* **`EstiloForm`:** proporciona elementos visuales uniformes para los formularios.

La interfaz utiliza componentes como `JFrame`, `JDesktopPane`, `JInternalFrame` y `JTable` para organizar la navegación y la visualización de información.


##  Requisitos previos

Antes de ejecutar el proyecto, se necesita:

* Tener instalado un JDK compatible con la configuración de Gradle del proyecto.
* Contar con Gradle o utilizar el *Gradle Wrapper*, si está incluido en el repositorio.
* Tener instalado y en ejecución PostgreSQL.
* Disponer de la base de datos y las tablas requeridas por la aplicación.
* Configurar correctamente los parámetros de conexión a la base de datos.

## 🚀 Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. Accede a la carpeta del proyecto:

   ```bash
   cd Gaming_League
   ```

3. Configura la conexión a PostgreSQL en la clase `Database`, utilizando la URL de conexión, el usuario y la contraseña correspondientes a tu entorno.

4. Verifica que la base de datos esté disponible y que su estructura sea compatible con las consultas de la aplicación.

5. Compila y ejecuta el proyecto mediante Gradle. Si el repositorio incluye el Gradle Wrapper, puedes utilizar:

   **Windows**

   ```bash
   gradlew.bat build
   gradlew.bat run
   ```

   **Linux/macOS**

   ```bash
   ./gradlew build
   ./gradlew run
   ```

   Estos comandos dependen de que el proyecto tenga configurado el plugin de aplicación de Gradle y el Wrapper correspondiente. Como alternativa, ejecuta la clase principal `com.example.Main` desde el IDE configurado para trabajar con Gradle.

> **Nota:** Los comandos de ejecución, la versión de Java y la configuración de la base de datos deben verificarse con los archivos reales del repositorio antes de utilizarse.

## 🗄️ Base de datos

El sistema trabaja con PostgreSQL y utiliza tablas relacionadas para organizar la información. Entre las entidades y relaciones principales se encuentran:

* `matches`: partidas y resultados.
* `players`: información de los jugadores.
* `games`: catálogo de videojuegos.
* `players_game_ranking`: rankings por jugador y videojuego.
* `leagues`: información de las ligas.
* `leagues_game`: relación entre ligas y videojuegos.
* `teams`: información y estadísticas de los equipos.
* `team_players`: relación entre equipos y jugadores.

Las relaciones entre estas tablas permiten consultar información combinada mediante operaciones SQL y cláusulas `JOIN`.

**Importante:** el esquema SQL, las restricciones, las claves foráneas y los datos iniciales deben corresponder a la versión de la base de datos utilizada por el proyecto.

## Estado del proyecto

El desarrollo contempla la consulta, inserción, actualización y eliminación de información, junto con la integración de formularios gráficos y mecanismos para mantener sincronizada la interfaz con los datos almacenados.



