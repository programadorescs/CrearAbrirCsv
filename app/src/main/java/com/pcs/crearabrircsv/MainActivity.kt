package com.pcs.crearabrircsv

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pcs.crearabrircsv.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCrearCsv.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                if (applicationContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    crearCsv()
                }  else {
                    pedirPermisoEscritura.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            } else {
                crearCsv()
            }
        }

        binding.btLeerCsv.setOnClickListener {
            if (applicationContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intento: Intent

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    intento = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intento.addCategory(Intent.CATEGORY_OPENABLE)
                    intento.type = "text/*"
                } else {
                    intento = Intent(Intent.ACTION_GET_CONTENT)
                    intento.type = "text/*"
                }

                leerCsv.launch(intento)
            } else {
                pedirPermisoLectura.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val pedirPermisoLectura =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intento: Intent

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    intento = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intento.addCategory(Intent.CATEGORY_OPENABLE)
                    intento.type = "text/*"
                } else {
                    intento = Intent(Intent.ACTION_GET_CONTENT)
                    intento.type = "text/*"
                }

                leerCsv.launch(intento)
            } else {
                Toast.makeText(this, "Necesita otorgar permiso de lectura al sd", Toast.LENGTH_LONG)
                    .show()
            }
        }

    private val pedirPermisoEscritura =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                crearCsv()
            } else {
                Toast.makeText(this, "Necesita otorgar permiso de escritura al sd", Toast.LENGTH_LONG)
                    .show()
            }
        }

    fun leerCsv(uri: Uri): List<String> {
        val csvFile = contentResolver.openInputStream(uri)
        val isr = InputStreamReader(csvFile)
        return BufferedReader(isr).readLines()
    }

    private val leerCsv =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.tvContenido.text = leerCsv(it.data?.data!!).joinToString(separator = "\n")
            }
        }

    private fun crearCsv() {
        try {
            //crear el archivo PDF dentro de la carpeta PDF
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

    private fun generarArchivoCsv() {
        try {
            val archivoCsv = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
                "Data_${System.currentTimeMillis()}.csv"
            )

            val writer = FileWriter(archivoCsv)
            // Escribir encabezados
            writer.append("Nombre;Apellido;Edad;\n")
            // Escribir datos
            writer.append("Juana;López;21;\n")
            writer.append("María;Pena;30;\n")
            writer.append("Julieta;Ramos;40;\n")

            // Limpiar (vaciar) el escritor
            writer.flush()
            // Cierra el escritor
            writer.close()

            mostrarMensaje(
                "EXITO", archivoCsv.absolutePath
            )
        } catch (e: Exception) {
            mostrarMensaje(
                "ERROR", e.message.toString()
            )
        }
    }

    private fun mostrarMensaje(titulo: String, mensaje: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setCancelable(false)
            .setPositiveButton("OK", null)
            .show()
    }
}