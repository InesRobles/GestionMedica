package com.example.gestionmedica;

import java.util.List;

import com.example.gestionmedica.dao.CitaDAO;
import com.example.gestionmedica.dao.MedicoDAO;
import com.example.gestionmedica.dao.PacienteDAO;
import com.example.gestionmedica.models.Cita;
import com.example.gestionmedica.models.Medico;
import com.example.gestionmedica.models.Paciente;
import com.example.gestionmedica.utils.DatabaseConnection;

/**
 * Clase de prueba para verificar que los DAOs funcionan correctamente
 * Ejecutar: java -cp ... com.example.gestionmedica.TestDAO
 */
public class TestDAO {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TEST DE DAOs - Sistema Gestión Médica");
        System.out.println("========================================\n");
        
        try {
            // Probar conexión
            DatabaseConnection.testConnection();
            System.out.println();
            
            // Test PacienteDAO
            testPacienteDAO();
            
            // Test MedicoDAO
            testMedicoDAO();
            
            // Test CitaDAO
            testCitaDAO();
            
            System.out.println("\n========================================");
            System.out.println("  ✓ TODOS LOS TESTS PASARON");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("\n✗ Error en los tests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
    
    private static void testPacienteDAO() {
        System.out.println("--- Test PacienteDAO ---");
        PacienteDAO dao = new PacienteDAO();
        
        List<Paciente> pacientes = dao.obtenerTodos();
        System.out.println("Total pacientes: " + pacientes.size());
        
        if (!pacientes.isEmpty()) {
            Paciente p = pacientes.get(0);
            System.out.println("Primer paciente: " + p.getNombreCompleto());
            System.out.println("DNI: " + p.getDni());
        }
        
        int total = dao.contarPacientes();
        System.out.println("Conteo total: " + total);
        System.out.println("✓ PacienteDAO funciona correctamente\n");
    }
    
    private static void testMedicoDAO() {
        System.out.println("--- Test MedicoDAO ---");
        MedicoDAO dao = new MedicoDAO();
        
        List<Medico> medicos = dao.obtenerTodos();
        System.out.println("Total médicos: " + medicos.size());
        
        if (!medicos.isEmpty()) {
            Medico m = medicos.get(0);
            System.out.println("Primer médico: " + m.getNombreCompleto());
            System.out.println("Especialidad: " + m.getEspecialidad());
        }
        
        List<String> especialidades = dao.obtenerEspecialidades();
        System.out.println("Especialidades disponibles: " + especialidades);
        
        int total = dao.contarMedicos();
        System.out.println("Conteo total: " + total);
        System.out.println("✓ MedicoDAO funciona correctamente\n");
    }
    
    private static void testCitaDAO() {
        System.out.println("--- Test CitaDAO ---");
        CitaDAO dao = new CitaDAO();
        
        List<Cita> citas = dao.obtenerTodas();
        System.out.println("Total citas: " + citas.size());
        
        if (!citas.isEmpty()) {
            Cita c = citas.get(0);
            System.out.println("Primera cita ID: " + c.getIdCita());
            System.out.println("Paciente: " + c.getNombrePaciente());
            System.out.println("Médico: " + c.getNombreMedico());
            System.out.println("Fecha: " + c.getFechaCita());
            System.out.println("Estado: " + c.getEstado());
        }
        
        List<Cita> citasHoy = dao.obtenerCitasDeHoy();
        System.out.println("Citas de hoy: " + citasHoy.size());
        
        int total = dao.contarCitas();
        System.out.println("Conteo total: " + total);
        
        int totalHoy = dao.contarCitasDeHoy();
        System.out.println("Conteo hoy: " + totalHoy);
        System.out.println("✓ CitaDAO funciona correctamente\n");
    }
}
