package com.example.gestionmedica.dao;

import com.example.gestionmedica.models.Medico;
import com.example.gestionmedica.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de operaciones CRUD sobre la tabla 'medicos'
 */
public class MedicoDAO {

    /**
     * Inserta un nuevo médico en la base de datos
     * @param medico El médico a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Medico medico) {
        String sql = "INSERT INTO medicos (nombre, apellidos, especialidad) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellidos());
            stmt.setString(3, medico.getEspecialidad());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        medico.setIdMedico(rs.getInt(1));
                    }
                }
                System.out.println("✓ Médico insertado: " + medico.getNombreCompleto());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al insertar médico: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene todos los médicos de la base de datos
     * @return Lista de todos los médicos
     */
    public List<Medico> obtenerTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos ORDER BY apellidos, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                medicos.add(mapearMedico(rs));
            }
            
            System.out.println("✓ Se obtuvieron " + medicos.size() + " médicos");
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener médicos: " + e.getMessage());
        }
        return medicos;
    }

    /**
     * Obtiene un médico por su ID
     * @param idMedico ID del médico
     * @return El médico encontrado o null
     */
    public Medico obtenerPorId(int idMedico) {
        String sql = "SELECT * FROM medicos WHERE id_medico = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMedico(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener médico por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca médicos por especialidad
     * @param especialidad Especialidad a buscar
     * @return Lista de médicos con esa especialidad
     */
    public List<Medico> buscarPorEspecialidad(String especialidad) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE especialidad LIKE ? ORDER BY apellidos, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + especialidad + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar médicos por especialidad: " + e.getMessage());
        }
        return medicos;
    }

    /**
     * Busca médicos por nombre o apellidos
     * @param texto Texto a buscar
     * @return Lista de médicos que coinciden
     */
    public List<Medico> buscarPorNombre(String texto) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos " +
                     "WHERE nombre LIKE ? OR apellidos LIKE ? " +
                     "ORDER BY apellidos, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + texto + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar médicos: " + e.getMessage());
        }
        return medicos;
    }

    /**
     * Actualiza los datos de un médico
     * @param medico Médico con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Medico medico) {
        String sql = "UPDATE medicos SET nombre = ?, apellidos = ?, especialidad = ? " +
                     "WHERE id_medico = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellidos());
            stmt.setString(3, medico.getEspecialidad());
            stmt.setInt(4, medico.getIdMedico());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Médico actualizado: " + medico.getNombreCompleto());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar médico: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un médico de la base de datos
     * @param idMedico ID del médico a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int idMedico) {
        String sql = "DELETE FROM medicos WHERE id_medico = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Médico eliminado con ID: " + idMedico);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar médico: " + e.getMessage());
            System.err.println("  Puede que tenga citas asociadas");
        }
        return false;
    }

    /**
     * Obtiene todas las especialidades únicas
     * @return Lista de especialidades
     */
    public List<String> obtenerEspecialidades() {
        List<String> especialidades = new ArrayList<>();
        String sql = "SELECT DISTINCT especialidad FROM medicos ORDER BY especialidad";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                especialidades.add(rs.getString("especialidad"));
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener especialidades: " + e.getMessage());
        }
        return especialidades;
    }

    /**
     * Cuenta el número total de médicos
     * @return Número de médicos
     */
    public int contarMedicos() {
        String sql = "SELECT COUNT(*) as total FROM medicos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al contar médicos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Mapea un ResultSet a un objeto Medico
     * @param rs ResultSet con los datos del médico
     * @return Objeto Medico
     */
    private Medico mapearMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setIdMedico(rs.getInt("id_medico"));
        medico.setNombre(rs.getString("nombre"));
        medico.setApellidos(rs.getString("apellidos"));
        medico.setEspecialidad(rs.getString("especialidad"));
        return medico;
    }
}
