package com.example.gestionmedica.dao;

import com.example.gestionmedica.models.Usuario;
import com.example.gestionmedica.utils.DatabaseConnection;
import java.sql.*;

public class LoginDAO {

    public Usuario login(String username, String password) {
        String sql = "SELECT u.*, m.especialidad " +
                "FROM usuarios u " +
                "LEFT JOIN medicos m ON u.id_usuario = m.id_usuario " +
                "WHERE u.username = ? AND u.password = ? AND u.activo = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("username"),
                        rs.getString("rol"),
                        rs.getString("nombre")
                );
                usuario.setActivo(rs.getBoolean("activo"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Error login: " + e.getMessage());
        }
        return null;
    }
}
