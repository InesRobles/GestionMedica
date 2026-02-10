package com.example.gestionmedica.dao;

import com.example.gestionmedica.models.Cita;
import com.example.gestionmedica.models.Cita.EstadoCita;
import com.example.gestionmedica.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de operaciones CRUD sobre la tabla 'citas'
 */
public class CitaDAO {

    /**
     * Inserta una nueva cita en la base de datos
     * @param cita La cita a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Cita cita) {
        String sql = "INSERT INTO citas (id_paciente, id_medico, fecha_cita, hora_cita, motivo, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, cita.getIdPaciente());
            stmt.setInt(2, cita.getIdMedico());
            stmt.setDate(3, Date.valueOf(cita.getFechaCita()));
            stmt.setTime(4, Time.valueOf(cita.getHoraCita()));
            stmt.setString(5, cita.getMotivo());
            stmt.setString(6, cita.getEstado().getValor());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cita.setIdCita(rs.getInt(1));
                    }
                }
                System.out.println("✓ Cita insertada con ID: " + cita.getIdCita());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al insertar cita: " + e.getMessage());
            if (e.getMessage().contains("uq_medico_fecha_hora")) {
                System.err.println("  El médico ya tiene una cita a esa hora");
            }
        }
        return false;
    }

    /**
     * Obtiene todas las citas con información del paciente y médico
     * @return Lista de todas las citas
     */
    public List<Cita> obtenerTodas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                citas.add(mapearCitaCompleta(rs));
            }
            
            System.out.println("✓ Se obtuvieron " + citas.size() + " citas");
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Obtiene una cita por su ID
     * @param idCita ID de la cita
     * @return La cita encontrada o null
     */
    public Cita obtenerPorId(int idCita) {
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.id_cita = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCitaCompleta(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener cita por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene las citas de un paciente específico
     * @param idPaciente ID del paciente
     * @return Lista de citas del paciente
     */
    public List<Cita> obtenerPorPaciente(int idPaciente) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.id_paciente = ? " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCitaCompleta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas del paciente: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Obtiene las citas de un médico específico
     * @param idMedico ID del médico
     * @return Lista de citas del médico
     */
    public List<Cita> obtenerPorMedico(int idMedico) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.id_medico = ? " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCitaCompleta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas del médico: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Obtiene las citas de una fecha específica
     * @param fecha Fecha a buscar
     * @return Lista de citas de esa fecha
     */
    public List<Cita> obtenerPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.fecha_cita = ? " +
                     "ORDER BY c.hora_cita";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCitaCompleta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas por fecha: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Obtiene las citas de hoy
     * @return Lista de citas de hoy
     */
    public List<Cita> obtenerCitasDeHoy() {
        return obtenerPorFecha(LocalDate.now());
    }

    /**
     * Obtiene citas futuras (a partir de hoy)
     * @return Lista de citas futuras
     */
    public List<Cita> obtenerCitasFuturas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.fecha_cita >= CURDATE() " +
                     "ORDER BY c.fecha_cita, c.hora_cita";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                citas.add(mapearCitaCompleta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas futuras: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Obtiene citas por estado
     * @param estado Estado de las citas
     * @return Lista de citas con ese estado
     */
    public List<Cita> obtenerPorEstado(EstadoCita estado) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "CONCAT(p.nombre, ' ', p.apellidos) as nombre_paciente, " +
                     "CONCAT(m.nombre, ' ', m.apellidos) as nombre_medico " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN medicos m ON c.id_medico = m.id_medico " +
                     "WHERE c.estado = ? " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getValor());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCitaCompleta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al obtener citas por estado: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Actualiza los datos de una cita
     * @param cita Cita con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Cita cita) {
        String sql = "UPDATE citas SET id_paciente = ?, id_medico = ?, fecha_cita = ?, " +
                     "hora_cita = ?, motivo = ?, estado = ? " +
                     "WHERE id_cita = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cita.getIdPaciente());
            stmt.setInt(2, cita.getIdMedico());
            stmt.setDate(3, Date.valueOf(cita.getFechaCita()));
            stmt.setTime(4, Time.valueOf(cita.getHoraCita()));
            stmt.setString(5, cita.getMotivo());
            stmt.setString(6, cita.getEstado().getValor());
            stmt.setInt(7, cita.getIdCita());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Cita actualizada con ID: " + cita.getIdCita());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza solo el estado de una cita
     * @param idCita ID de la cita
     * @param estado Nuevo estado
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEstado(int idCita, EstadoCita estado) {
        String sql = "UPDATE citas SET estado = ? WHERE id_cita = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getValor());
            stmt.setInt(2, idCita);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Estado de cita actualizado a: " + estado);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar estado de cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina una cita de la base de datos
     * @param idCita ID de la cita a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int idCita) {
        String sql = "DELETE FROM citas WHERE id_cita = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCita);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Cita eliminada con ID: " + idCita);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cuenta el número total de citas
     * @return Número de citas
     */
    public int contarCitas() {
        String sql = "SELECT COUNT(*) as total FROM citas";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al contar citas: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Cuenta las citas de hoy
     * @return Número de citas de hoy
     */
    public int contarCitasDeHoy() {
        String sql = "SELECT COUNT(*) as total FROM citas WHERE fecha_cita = CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error al contar citas de hoy: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Mapea un ResultSet a un objeto Cita (sin nombres)
     * @param rs ResultSet con los datos de la cita
     * @return Objeto Cita
     */
    private Cita mapearCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setIdPaciente(rs.getInt("id_paciente"));
        cita.setIdMedico(rs.getInt("id_medico"));
        cita.setFechaCita(rs.getDate("fecha_cita").toLocalDate());
        cita.setHoraCita(rs.getTime("hora_cita").toLocalTime());
        cita.setMotivo(rs.getString("motivo"));
        cita.setEstado(EstadoCita.fromString(rs.getString("estado")));
        return cita;
    }

    /**
     * Mapea un ResultSet a un objeto Cita con nombres de paciente y médico
     * @param rs ResultSet con los datos de la cita y nombres
     * @return Objeto Cita completo
     */
    private Cita mapearCitaCompleta(ResultSet rs) throws SQLException {
        Cita cita = mapearCita(rs);
        cita.setNombrePaciente(rs.getString("nombre_paciente"));
        cita.setNombreMedico(rs.getString("nombre_medico"));
        return cita;
    }
}
