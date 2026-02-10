package com.example.gestionmedica;

import com.example.gestionmedica.dao.UsuarioDAO;
import com.example.gestionmedica.models.Usuario;
import com.example.gestionmedica.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase para probar la conexión y validar que la tabla usuarios existe
 */
public class TestLogin {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TEST DE LOGIN Y BASE DE DATOS");
        System.out.println("========================================\n");
        
        // 1. Probar conexión a la base de datos
        System.out.println("1. Probando conexión a la base de datos...");
        try {
            DatabaseConnection.testConnection();
        } catch (Exception e) {
            System.err.println("✗ ERROR: No se pudo conectar a la base de datos");
            System.err.println("   Detalles: " + e.getMessage());
            System.err.println("\n⚠ SOLUCIÓN:");
            System.err.println("   - Asegúrate de que MySQL esté ejecutándose");
            System.err.println("   - Verifica usuario y contraseña en DatabaseConnection.java");
            System.err.println("   - Ejecuta el script SQL: datosMedicos");
            return;
        }
        
        // 2. Verificar que existe la tabla usuarios
        System.out.println("\n2. Verificando tabla 'usuarios'...");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM usuarios")) {
            
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✓ Tabla 'usuarios' encontrada con " + total + " registros");
                
                if (total == 0) {
                    System.err.println("\n⚠ ADVERTENCIA: La tabla usuarios está vacía!");
                    System.err.println("   Ejecuta el script SQL completo: datosMedicos");
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ ERROR: No existe la tabla 'usuarios'");
            System.err.println("   Detalles: " + e.getMessage());
            System.err.println("\n⚠ SOLUCIÓN:");
            System.err.println("   Ejecuta el script SQL completo en MySQL:");
            System.err.println("   mysql -u root -p < datosMedicos");
            return;
        }
        
        // 3. Listar todos los usuarios disponibles
        System.out.println("\n3. Usuarios disponibles en el sistema:");
        System.out.println("-------------------------------------");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, password, nombre_completo, rol FROM usuarios WHERE activo = TRUE")) {
            
            while (rs.next()) {
                System.out.println("  📝 Usuario: " + rs.getString("username"));
                System.out.println("     Contraseña: " + rs.getString("password"));
                System.out.println("     Nombre: " + rs.getString("nombre_completo"));
                System.out.println("     Rol: " + rs.getString("rol"));
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("✗ Error al listar usuarios: " + e.getMessage());
        }
        
        // 4. Probar el login con el usuario admin
        System.out.println("\n4. Probando login con usuario 'admin'...");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarCredenciales("admin", "admin123");
        
        if (usuario != null) {
            System.out.println("✓ ¡LOGIN EXITOSO!");
            System.out.println("  Usuario: " + usuario.getUsername());
            System.out.println("  Nombre: " + usuario.getNombreCompleto());
            System.out.println("  Rol: " + usuario.getRol());
        } else {
            System.err.println("✗ LOGIN FALLIDO");
            System.err.println("  Usuario 'admin' con contraseña 'admin123' no fue validado");
        }
        
        System.out.println("\n========================================");
        System.out.println("  FIN DEL TEST");
        System.out.println("========================================");
    }
}
