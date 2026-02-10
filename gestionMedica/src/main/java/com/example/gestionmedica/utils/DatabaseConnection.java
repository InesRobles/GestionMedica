package com.example.gestionmedica.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MySQL
 * Base de datos: hospital_mvp
 */
public class DatabaseConnection {
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_mvp";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Parámetros adicionales para MySQL 8.x
    private static final String FULL_URL = URL 
        + "?useSSL=false"
        + "&serverTimezone=UTC"
        + "&allowPublicKeyRetrieval=true";
    
    private static Connection connection = null;
    
    // Constructor privado para evitar instanciación
    private DatabaseConnection() {
    }
    
    /**
     * Obtiene una conexión a la base de datos (patrón Singleton)
     * @return Connection objeto de conexión activo
     * @throws SQLException si hay error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Si no existe conexión o está cerrada, crear una nueva
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
                System.out.println("✓ Conexión exitosa a la base de datos hospital_mvp");
            }
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado. Añade mysql-connector-java al pom.xml", e);
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos:");
            System.err.println("  - Verifica que MySQL esté ejecutándose");
            System.err.println("  - Comprueba el usuario y contraseña");
            System.err.println("  - Asegúrate de que existe la BD 'hospital_mvp'");
            throw e;
        }
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si la conexión está activa
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Método de prueba para verificar la conexión
     */
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✓ Test de conexión exitoso");
                System.out.println("  Base de datos: hospital_mvp");
                System.out.println("  Driver: " + conn.getMetaData().getDriverName());
                System.out.println("  Versión: " + conn.getMetaData().getDriverVersion());
            }
        } catch (SQLException e) {
            System.err.println("✗ Test de conexión fallido: " + e.getMessage());
        }
    }
}
