Descripcion:
Sistema desarrollado en Java para la gestion de una mediateca que permite administrar libros, revistas, CDs y DVDs con almacenamiento persistente en base de datos MySQL.

Funcionalidades Implementadas
Operaciones Principales
Agregar nuevos materiales (libros, revistas, CDs, DVDs)

Modificar informacion de materiales existentes

Listar materiales disponibles por categoria

Eliminar materiales del sistema

Buscar materiales por codigo interno

Tipos de Materiales Gestionados
Libros: titulo, autor, paginas, editorial, ISBN, año, unidades

Revistas: titulo, editorial, periodicidad, fecha, unidades

CDs: titulo, artista, genero, duracion, canciones, unidades

DVDs: titulo, director, duracion, genero, unidades

Estructura Tecnica
Arquitectura del Sistema
Frontend: Interfaz grafica con Java Swing

Backend: Logica de negocio en Java

Caracteristicas Tecnicas
Programacion Orientada a Objetos
Implementacion de herencia: Clase Material como padre

Encapsulacion de atributos

Polimorfismo en el tratamiento de materiales

Base de Datos
Tablas separadas para cada tipo de material

Codigos unicos generados automaticamente

Validacion de integridad referencial

Interfaz de Usuario
Menu de navegacion con 6 opciones principales

Diálogos interactivos para entrada de datos

Visualizacion organizada por categorias

Configuracion Requerida
Prerrequisitos
Java Development Kit 8 o superior

Servidor MySQL

Controlador JDBC para MySQL

Instalacion
Crear la base de datos mediateca_db

Ejecutar el script de creacion de tablas

Configurar los parametros de conexion

Compilar y ejecutar la aplicacion

Convenciones de Codigo
Generacion de IDs
LIB00001, LIB00002... para libros

REV00001, REV00002... para revistas

CDA00001, CDA00002... para CDs

DVD00001, DVD00002... para DVDs

Validaciones Implementadas
Campos obligatorios

Formatos correctos de datos

Unidades no negativas

IDs unicos

Cumplimiento de Requisitos
El sistema cumple con todos los requisitos especificados:

Gestion completa de cuatro tipos de materiales

Generacion automatica de codigos unicos

Almacenamiento persistente en base de datos

Interfaz grafica con Java Swing

Implementacion de POO con herencia

Validaciones de datos de entrada
Base de Datos: MySQL para persistencia

Patrones: DAO para acceso a datos, Singleton para conexiones
