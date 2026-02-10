package com.example.gestionmedica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.example.gestionmedica.models.Usuario;
import com.example.gestionmedica.utils.DatabaseConnection;

/**
 * DAO para gestionar la autenticación de usuarios
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales de un usuario
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Usuario si las credenciales son correctas, null en caso contrario
     */
    public Usuario validarCredenciales(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = mapearUsuario(rs);
                    System.out.println("✓ Login exitoso: " + usuario.getNombreCompleto());
                    return usuario;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al validar credenciales: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtiene un usuario por su username
     * @param username Nombre de usuario
     * @return Usuario encontrado o null
     */
    public Usuario obtenerPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener usuario: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Inserta un nuevo usuario en la base de datos
     * @param usuario El usuario a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombre_completo, rol) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombreCompleto());
            stmt.setString(4, usuario.getRol());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setIdUsuario(rs.getInt(1));
                    }
                }
                System.out.println("✓ Usuario insertado: " + usuario.getUsername());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al insertar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            usuario.setFechaCreacion(timestamp.toLocalDateTime());
        }
        
        return usuario;
    }
}
