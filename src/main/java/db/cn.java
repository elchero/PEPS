package db;

import java.sql.Connection;
import java.sql.DriverManager;
public class cn {

    private Connection con;

    public cn() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection("jdbc:mysql://conta1.c3miuy84i8m2.us-east-2.rds.amazonaws.com:3306/conta1", "root", "Elsalvador35");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/peps", "root", "");
            if (con == null) {
                System.err.println("No se pudo establecer la conexión a la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace para depuración
        }
    }

    public Connection getCon() {
        if (con == null) {
            System.err.println("La conexión es nula.");
        }
        return con;
    }
}
