package mx.edu.utng.aplicacioncrudii

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyListAdapter
    private lateinit var databaseHandler: DatabaseHandler
    private var originalData: List<EmpModelClass> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar la base de datos
        databaseHandler = DatabaseHandler(this)

        // Cargar datos iniciales
        loadData()

        // Configurar el botón de búsqueda
        val searchInput = findViewById<EditText>(R.id.search_input)
        val searchButton = findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                filterList(query)
            } else {
                Toast.makeText(this, "Ingrese un término de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Cargar datos desde la base de datos
    private fun loadData() {
        originalData = databaseHandler.viewEmployee()
        adapter = MyListAdapter(this, originalData)
        recyclerView.adapter = adapter
    }

    // Filtrar la lista según la consulta de búsqueda
    private fun filterList(query: String) {
        val filteredRecords = originalData.filter {
            it.userName.contains(query, ignoreCase = true)
        }

        if (filteredRecords.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
        }

        adapter.updateData(filteredRecords)
    }

    // Método para guardar registros en la base de datos
    fun saveRecord(view: View) {
        val u_id = findViewById<EditText>(R.id.u_id)
        val u_name = findViewById<EditText>(R.id.u_name)
        val u_artist = findViewById<EditText>(R.id.u_artist)
        val u_genero = findViewById<EditText>(R.id.u_genero)
        val u_fechadelanzamiento = findViewById<EditText>(R.id.u_fechadelanzamiento)
        val u_precio = findViewById<EditText>(R.id.u_precio)
        val u_imagen = findViewById<EditText>(R.id.u_imagen)

        val id = u_id.text.toString()
        val name = u_name.text.toString().trim()
        val artist = u_artist.text.toString().trim()
        val genero = u_genero.text.toString().trim()
        val fechadelanzamiento = u_fechadelanzamiento.text.toString().trim()
        val precioStr = u_precio.text.toString().trim()
        val imagen = u_imagen.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || artist.isEmpty() || genero.isEmpty() ||
            fechadelanzamiento.isEmpty() || precioStr.isEmpty() || imagen.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val idInt = id.toIntOrNull()
        val precioFloat = precioStr.toFloatOrNull()

        if (idInt == null) {
            Toast.makeText(this, "ID debe ser un número válido", Toast.LENGTH_LONG).show()
            return
        }
        if (precioFloat == null) {
            Toast.makeText(this, "Precio debe ser un número válido", Toast.LENGTH_LONG).show()
            return
        }

        if (id.trim() != "" && name.trim() != "" && artist.trim() != "" && genero.trim() != "" &&
            fechadelanzamiento.trim() != "" && precioStr.trim() != "" && imagen.trim() != "") {
            val status = databaseHandler.addEmployee(
                EmpModelClass(
                    idInt,
                    name,
                    artist,
                    genero,
                    fechadelanzamiento,
                    precioFloat,
                    imagen
                )
            )
            Log.d("DB", "Registro insertado con ID: $status")
            if (status > -1) {
                Toast.makeText(applicationContext, "Registro guardado", Toast.LENGTH_LONG).show()
                u_id.text.clear()
                u_name.text.clear()
                u_artist.text.clear()
                u_genero.text.clear()
                u_fechadelanzamiento.text.clear()
                u_precio.text.clear()
                u_imagen.text.clear()
                loadData() // Recargar datos después de guardar
            }
        } else {
            Toast.makeText(applicationContext, "Ningún campo debe estar vacío", Toast.LENGTH_LONG).show()
        }
    }

    // Método para leer registros de la base de datos
    fun viewRecord(view: View) {
        loadData()
    }

    // Configurar el menú de búsqueda
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterList(it)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
    fun updateRecord(view: View) {
        val u_id = findViewById<EditText>(R.id.u_id)
        val u_name = findViewById<EditText>(R.id.u_name)
        val u_artist = findViewById<EditText>(R.id.u_artist)
        val u_genero = findViewById<EditText>(R.id.u_genero)
        val u_fechadelanzamiento = findViewById<EditText>(R.id.u_fechadelanzamiento)
        val u_precio = findViewById<EditText>(R.id.u_precio)
        val u_imagen = findViewById<EditText>(R.id.u_imagen)

        val id = u_id.text.toString()
        val name = u_name.text.toString().trim()
        val artist = u_artist.text.toString().trim()
        val genero = u_genero.text.toString().trim()
        val fechadelanzamiento = u_fechadelanzamiento.text.toString().trim()
        val precioStr = u_precio.text.toString().trim()
        val imagen = u_imagen.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || artist.isEmpty() || genero.isEmpty() ||
            fechadelanzamiento.isEmpty() || precioStr.isEmpty() || imagen.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val idInt = id.toIntOrNull()
        val precioFloat = precioStr.toFloatOrNull()

        if (idInt == null) {
            Toast.makeText(this, "ID debe ser un número válido", Toast.LENGTH_LONG).show()
            return
        }
        if (precioFloat == null) {
            Toast.makeText(this, "Precio debe ser un número válido", Toast.LENGTH_LONG).show()
            return
        }

        // Crear un objeto EmpModelClass con los datos actualizados
        val emp = EmpModelClass(
            userId = idInt,
            userName = name,
            userArtist = artist,
            userGenero = genero,
            userFechadelanzamiento = fechadelanzamiento,
            userPrecio = precioFloat,
            userImagen = imagen
        )

        // Llamar al método updateEmployee de DatabaseHandler
        val status = databaseHandler.updateEmployee(emp)

        if (status > -1) {
            Toast.makeText(applicationContext, "Registro actualizado", Toast.LENGTH_LONG).show()
            u_id.text.clear()
            u_name.text.clear()
            u_artist.text.clear()
            u_genero.text.clear()
            u_fechadelanzamiento.text.clear()
            u_precio.text.clear()
            u_imagen.text.clear()
            loadData() // Recargar datos después de actualizar
        } else {
            Toast.makeText(applicationContext, "Error al actualizar el registro", Toast.LENGTH_LONG).show()
        }
    }

    // Método para borrar registros de la base de datos
    fun deleteRecord(view: View) {
        val u_id = findViewById<EditText>(R.id.u_id)
        val id = u_id.text.toString()

        if (id.isEmpty()) {
            Toast.makeText(this, "El campo ID es obligatorio", Toast.LENGTH_LONG).show()
            return
        }

        val idInt = id.toIntOrNull()
        if (idInt == null) {
            Toast.makeText(this, "ID debe ser un número válido", Toast.LENGTH_LONG).show()
            return
        }

        // Crear un objeto EmpModelClass con solo el ID (los demás campos no son necesarios para borrar)
        val emp = EmpModelClass(
            userId = idInt,
            userName = "",
            userArtist = "",
            userGenero = "",
            userFechadelanzamiento = "",
            userPrecio = 0f,
            userImagen = ""
        )

        // Llamar al método deleteEmployee de DatabaseHandler
        val status = databaseHandler.deleteEmployee(emp)

        if (status > -1) {
            Toast.makeText(applicationContext, "Registro borrado", Toast.LENGTH_LONG).show()
            u_id.text.clear()
            loadData() // Recargar datos después de borrar
        } else {
            Toast.makeText(applicationContext, "Error al borrar el registro", Toast.LENGTH_LONG).show()
        }
    }


}