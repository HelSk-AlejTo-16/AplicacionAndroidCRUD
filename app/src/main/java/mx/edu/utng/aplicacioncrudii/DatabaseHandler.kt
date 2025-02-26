package mx.edu.utng.aplicacioncrudii
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_ARTISTA = "artista"
        private val KEY_GENERO = "genero"
        private val KEY_FECHADELANZAMIENTO = "fechadeLanzamiento"
        private val KEY_PRECIO = "precio"
        private val KEY_IMAGEN = "imagen"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_ARTISTA + " TEXT, "
                + KEY_GENERO + " TEXT, " // Agregado espacio antes de TEXT
                + KEY_FECHADELANZAMIENTO + " TEXT, " // Agregado espacio antes de TEXT
                + KEY_PRECIO + " FLOAT, " // Agregado espacio antes de FLOAT
                + KEY_IMAGEN + " TEXT" + ")") // Agregado espacio antes de TEXT
        db?.execSQL(CREATE_CONTACTS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }
    //method to insert data
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_ARTISTA,emp.userArtist ) // EmpModelClass Phone
        contentValues.put(KEY_GENERO, emp.userGenero)
        contentValues.put(KEY_FECHADELANZAMIENTO, emp.userFechadelanzamiento)
        contentValues.put(KEY_PRECIO, emp.userPrecio)
        contentValues.put(KEY_IMAGEN, emp.userImagen)
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewEmployee():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userArtist: String
        var userGenero: String
        var userFechadelanzamiento: String
        var userPrecio : Float
        var userImagen : String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userArtist = cursor.getString(cursor.getColumnIndex("artista"))
                userGenero = cursor.getString(cursor.getColumnIndex("genero"))
                userFechadelanzamiento = cursor.getString(cursor.getColumnIndex("fechadeLanzamiento"))
                userPrecio = cursor.getFloat(cursor.getColumnIndex("precio"))
                userImagen = cursor.getString(cursor.getColumnIndex("imagen"))

                val emp= EmpModelClass(userId = userId, userName = userName, userArtist = userArtist, userGenero = userGenero,
                    userFechadelanzamiento = userFechadelanzamiento, userPrecio = userPrecio, userImagen = userImagen)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_ARTISTA,emp.userArtist ) // EmpModelClass Email
        contentValues.put(KEY_GENERO, emp.userGenero)
        contentValues.put(KEY_FECHADELANZAMIENTO, emp.userFechadelanzamiento)
        contentValues.put(KEY_PRECIO, emp.userPrecio)
        contentValues.put(KEY_IMAGEN, emp.userImagen)

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}