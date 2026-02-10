# 🏥 Sistema de Gestión Médica - Instrucciones de Uso

## 📋 Pasos para configurar y usar el sistema

### 1. **Crear la Base de Datos**

Ejecuta el archivo SQL en MySQL:

```bash
# Opción 1: Desde MySQL Workbench
- Abre MySQL Workbench
- Conecta a tu servidor local
- Abre el archivo: gestionMedica/datosMedicos
- Ejecuta el script completo (Ctrl + Shift + Enter)

# Opción 2: Desde línea de comandos
mysql -u root -p < C:\Users\LuisC\Desktop\JustDoIt\GestionMedica\gestionMedica\datosMedicos
```

Esto creará:
- ✅ Base de datos `hospital_mvp`
- ✅ Tabla `usuarios` (para login)
- ✅ Tablas `medicos`, `pacientes`, `citas`
- ✅ Datos de prueba incluidos

---

## 🔐 Usuarios disponibles para el Login

### **Usuario 1: Administrador**
```
Usuario:    admin
Contraseña: admin123
Rol:        Administrador
```

### **Usuario 2: Recepcionista**
```
Usuario:    recepcion
Contraseña: recep123
Rol:        Recepcionista
```

---

## ▶️ Cómo ejecutar la aplicación

### **Opción 1: Desde IntelliJ IDEA / Eclipse**
1. Abre el proyecto en tu IDE
2. Ejecuta `Launcher.java` (clase principal)
3. Aparecerá la pantalla de login

### **Opción 2: Desde Maven**
```bash
cd gestionMedica
mvnw javafx:run
```

---

## 📝 Flujo de uso de la aplicación

1. **Login**: Ingresa usuario y contraseña
2. **Dashboard**: Verás 3 botones principales:
   - 🟢 **Gestión de Pacientes** (si está implementada)
   - 🔵 **Gestión de Médicos** (VistaDoctor.fxml)
   - 🟠 **Gestión de Citas** (si está implementada)
3. **Cerrar Sesión**: Botón rojo arriba a la derecha

---

## ➕ Cómo agregar más usuarios

### **Método 1: Desde MySQL Workbench/línea de comandos**
```sql
USE hospital_mvp;

INSERT INTO usuarios (username, password, nombre_completo, rol) 
VALUES ('nuevousuario', 'contraseña123', 'Juan Pérez', 'recepcionista');
```

### **Método 2: Programáticamente (Java)**
```java
UsuarioDAO usuarioDAO = new UsuarioDAO();
Usuario nuevoUsuario = new Usuario("usuario", "password", "Nombre Completo", "admin");
usuarioDAO.insertar(nuevoUsuario);
```

---

## 🔧 Estructura de la tabla usuarios

```sql
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(200),
    rol ENUM('admin', 'medico', 'recepcionista') DEFAULT 'recepcionista',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Roles disponibles:**
- `admin`: Acceso completo al sistema
- `medico`: Acceso a sus citas y pacientes
- `recepcionista`: Gestión de citas y registro de pacientes

---

## ⚠️ Solución de problemas

### **Error: "No se pudo conectar a la base de datos"**
✅ Verifica que MySQL esté ejecutándose  
✅ Comprueba usuario y contraseña en `DatabaseConnection.java`  
✅ Asegúrate de que existe la base de datos `hospital_mvp`

### **Error: "Usuario o contraseña incorrectos"**
✅ Verifica que ejecutaste el script SQL completo  
✅ Comprueba que la tabla `usuarios` tiene datos:
```sql
SELECT * FROM usuarios;
```

### **Error: "No se pudo cargar Dashboard"**
✅ Verifica que existe el archivo `Dashboard.fxml` en `resources/com/example/gestionmedica/`

---

## 📊 Estado actual del proyecto

✅ **Completado:**
- Sistema de login funcional
- Validación de usuarios contra base de datos
- Dashboard con navegación
- Modelos: Usuario, Medico, Paciente, Cita
- DAOs: UsuarioDAO, MedicoDAO, PacienteDAO, CitaDAO
- Base de datos con datos de prueba

⏳ **Pendiente:**
- Vista de Pacientes (Pacientes.fxml)
- Vista de Citas (Citas.fxml)
- Mejoras en la interfaz
- Encriptación de contraseñas

---

## 🚀 Próximos pasos recomendados

1. **Seguridad**: Implementar hash de contraseñas (BCrypt)
2. **Vistas faltantes**: Crear Pacientes.fxml y Citas.fxml
3. **Validaciones**: Agregar más validaciones en formularios
4. **Permisos**: Implementar control de acceso por roles
5. **Reportes**: Generar reportes de citas, pacientes, etc.

---

¡Sistema listo para usar! 🎉
