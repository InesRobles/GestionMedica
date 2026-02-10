package com.example.gestionmedica.dao;

import com.example.gestionmedica.models.Paciente;
import com.example.gestionmedica.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de operaciones CRUD sobre la tabla 'pacientes'
 */
public class PacienteDAO {

    /**
     * Inserta un nuevo paciente en la base de datos
     * @param paciente El paciente a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (dni, nombre, apellidos, fecha_nacimiento, telefono, email, direccion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, paciente.getDni());
            stmt.setString(2, paciente.getNombre());
            stmt.setString(3, paciente.getApellidos());
            stmt.setDate(4, paciente.getFechaNacimiento() != null ? Date.valueOf(paciente.getFechaNacimiento()) : null);
            stmt.setString(5, paciente.getTelefono());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getDireccion());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        paciente.setIdPaciente(rs.getInt(1));
                    }
                }
                System.out.println("✓ Paciente insertado: " + paciente.getNombreCompleto());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al insertar paciente: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene todos los pacientes de la base de datos
     * @return Lista de todos los pacientes
     */
    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY apellidos, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }
            
            System.out.println("✓ Se obtuvieron " + pacientes.size() + " pacientes");
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    /**
     * Obtiene un paciente por su ID
     * @param idPaciente ID del paciente
     * @return El paciente encontrado o null
     */
    public Paciente obtenerPorId(int idPaciente) {
        String sql = "SELECT * FROM pacientes WHERE id_paciente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener paciente por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca pacientes por DNI
     * @param dni DNI a buscar
     * @return El paciente encontrado o null
     */
    public Paciente buscarPorDni(String dni) {
        String sql = "SELECT * FROM pacientes WHERE dni = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar paciente por DNI: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca pacientes por nombre o apellidos
     * @param texto Texto a buscar
     * @return Lista de pacientes que coinciden
     */
    public List<Paciente> buscarPorNombre(String texto) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes " +
                     "WHERE nombre LIKE ? OR apellidos LIKE ? " +
                     "ORDER BY apellidos, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + texto + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapearPaciente(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    /**
     * Actualiza los datos de un paciente
     * @param paciente Paciente con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE pacientes SET dni = ?, nombre = ?, apellidos = ?, " +
                     "fecha_nacimiento = ?, telefono = ?, email = ?, direccion = ? " +
                     "WHERE id_paciente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getDni());
            stmt.setString(2, paciente.getNombre());
            stmt.setString(3, paciente.getApellidos());
            stmt.setDate(4, paciente.getFechaNacimiento() != null ? Date.valueOf(paciente.getFechaNacimiento()) : null);
            stmt.setString(5, paciente.getTelefono());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getDireccion());
            stmt.setInt(8, paciente.getIdPaciente());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Paciente actualizado: " + paciente.getNombreCompleto());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar paciente: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un paciente de la base de datos
     * @param idPaciente ID del paciente a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int idPaciente) {
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Paciente eliminado con ID: " + idPaciente);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar paciente: " + e.getMessage());
            System.err.println("  Puede que tenga citas asociadas");
        }
        return false;
    }

    /**
     * Cuenta el número total de pacientes
     * @return Número de pacientes
     */
    public int contarPacientes() {
        String sql = "SELECT COUNT(*) as total FROM pacientes";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al contar pacientes: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Mapea un ResultSet a un objeto Paciente
     * @param rs ResultSet con los datos del paciente
     * @return Objeto Paciente
     */
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setDni(rs.getString("dni"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellidos(rs.getString("apellidos"));
        
        Date fecha = rs.getDate("fecha_nacimiento");
        if (fecha != null) {
            paciente.setFechaNacimiento(fecha.toLocalDate());
        }
        
        paciente.setTelefono(rs.getString("telefono"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDireccion(rs.getString("direccion"));
        
        return paciente;
    }
}
