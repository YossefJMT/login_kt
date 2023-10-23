import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yossefjm.dam2_3.db.UserClass

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "LoginApp.db"

        // Definir las tablas y columnas
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_BIRTHDATE = "birthdate"
        private const val COLUMN_DNI = "dni"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla de usuarios en la base de datos
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FIRST_NAME TEXT," +
                "$COLUMN_LAST_NAME TEXT," +
                "$COLUMN_USERNAME TEXT UNIQUE," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_BIRTHDATE TEXT," +
                "$COLUMN_DNI TEXT UNIQUE)")
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar la tabla si existe y recrearla en una nueva versión de la base de datos
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Método para agregar un nuevo usuario al registro
    fun addUser(user: UserClass): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_FIRST_NAME, user.firstName)
        values.put(COLUMN_LAST_NAME, user.lastName)
        values.put(COLUMN_USERNAME, user.username)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_BIRTHDATE, user.birthdate)
        values.put(COLUMN_DNI, user.dni)
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    // Método para actualizar un usuario
    fun updateUser(user: UserClass): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_FIRST_NAME, user.firstName)
        values.put(COLUMN_LAST_NAME, user.lastName)
        values.put(COLUMN_USERNAME, user.username)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_BIRTHDATE, user.birthdate)

        // Se define el whereClause para actualizar el registro correcto
        val whereClause = "$COLUMN_DNI = ?"
        // Se utiliza el dni como argumento para el whereClause
        val whereArgs = arrayOf(user.dni)

        // Se actualiza el registro y se retorna el número de filas actualizadas
        val numRowsUpdated = db.update(TABLE_USERS, values, whereClause, whereArgs)

        db.close()

        return numRowsUpdated
    }

    fun deleteUser(dni : String): Int {
        val db = this.writableDatabase

        // Se define el whereClause para eliminar el registro correcto
        val whereClause = "$COLUMN_DNI = ?"
        // Se utiliza el dni como argumento para el whereClause
        val whereArgs = arrayOf(dni)

        // Se elimina el registro y se retorna el número de filas eliminadas
        val numRowsDeleted = db.delete(TABLE_USERS, whereClause, whereArgs)

        db.close()

        return numRowsDeleted
    }

    // Método para verificar las credenciales de inicio de sesión
    @SuppressLint("Range")
    fun checkLogin(username: String, password: String): UserClass? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        if (cursor.moveToFirst()) {
            // Si se encontró un registro con las credenciales proporcionadas, crea un objeto UserClass
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME))
            val birthdate = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDATE))
            val dni = cursor.getString(cursor.getColumnIndex(COLUMN_DNI))

            cursor.close()
            db.close()

            return UserClass(id, firstName, lastName, username, password, birthdate, dni)
        } else {
            cursor.close()
            db.close()
            return null // Retorna null si las credenciales son incorrectas
        }
    }

}
