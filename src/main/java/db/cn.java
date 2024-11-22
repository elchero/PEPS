package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Executors;

public class cn {

    private Connection con;

    public cn() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://pepsinventario.c3miuy84i8m2.us-east-2.rds.amazonaws.com:3306/peps?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "Elsalvador35");
//            String url = "jdbc:mysql://localhost:3306/inventariopeps";
//            con = DriverManager.getConnection(url, "root", "");

            con.setNetworkTimeout(Executors.newSingleThreadExecutor(), 30000);

            if (con == null) {
                System.err.println("No se pudo establecer la conexión a la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getCon() {
        if (con == null) {
            System.err.println("La conexión es nula.");
        }
        return con;
    }
    //        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            //con = DriverManager.getConnection("jdbc:mysql://pepsinventario.c3miuy84i8m2.us-east-2.rds.amazonaws.com:3306/peps?serverTimeZone=UTC", "root", "Elsalvador35");
//           // con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventariopeps", "root", "");
//            con = DriverManager.getConnection("jdbc:mysql://34.56.90.179:3306/peps", "root", "Elsalvador35");
//             con.setNetworkTimeout(Executors.newSingleThreadExecutor(), 30000);
//            if (con == null) {
//                System.err.println("No se pudo establecer la conexión a la base de datos.");
//            }
//        } catch (Exception e) {
//            System.err.println("Error en la conexión: " + e.getMessage());
//            e.printStackTrace(); // Imprime el stack trace para depuración
//        }
}
