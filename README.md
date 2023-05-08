# Cómo crear y abrir un archivo CSV en Kotlin

Los archivos CSV son una forma común de almacenar y compartir datos tabulares en formato de texto plano. En Kotlin, crear y abrir archivos CSV es una tarea fácil gracias a las clases y funciones disponibles.

## Requisitos

- Android Studio Flamingo | 2022.2.1 Patch 1 o superior.
- Gradle 8.0.1 o superior.
- Kotlin 1.8.20 o superior.

## Crear un archivo CSV en Kotlin

Para crear un archivo CSV en Kotlin, podemos utilizar la clase OutputStreamWriter y FileOutputStream para escribir datos en un archivo.

```kotlin
private fun crearCsv() {
    try {
        //crear el archivo CSV en la carpeta descargas
        val csvArchivo = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Data_${System.currentTimeMillis()}.csv"
        )

        val osw = OutputStreamWriter(FileOutputStream(csvArchivo))

        // Escribir encabezados
        osw.append("Nombre;Apellido;Edad;\n")
        // Escribir datos
        osw.append("Juana;López;21;\n")
        osw.append("María;Pena;30;\n")
        osw.append("Julieta;Ramos;40;\n")

        // Limpiar (vaciar) el escritor
        osw.flush()
        // Cierra el escritor
        osw.close()

        mostrarMensaje(
            "EXITO", csvArchivo.absolutePath
        )
    } catch (e: Exception) {
        mostrarMensaje(
            "ERROR", e.message.toString()
        )
    }
}
```

En este ejemplo, estamos creando un archivo llamado Data_${System.currentTimeMillis()}.csv con tres columnas: Nombre, Edad y Ciudad. Luego, agregamos dos filas con información ficticia. Finalmente, cerramos el escritor para guardar los cambios y mostrar un mensaje de exito.

## Abrir un archivo CSV en Kotlin

Para abrir un archivo CSV en Kotlin, podemos utilizar la clase InputStreamReader, BufferedReader y la función readLines() para leer el archivo. La ubicación del archivo la obtendremos de la Uri.

```kotlin
    fun leerCsv(uri: Uri): List<String> {
        val csvFile = contentResolver.openInputStream(uri)
        val isr = InputStreamReader(csvFile)
        return BufferedReader(isr).readLines()
    }
```

## Conclusión

En resumen, crear y abrir archivos CSV en Kotlin es una tarea sencilla gracias a las bibliotecas disponibles en el lenguaje. Con un poco de conocimiento de programación, puedes utilizar estas herramientas para manejar datos tabulares de manera efectiva en tus proyectos.
