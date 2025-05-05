package com.universidad.demo.controllers;

import com.universidad.demo.models.Materia;
import com.universidad.demo.models.Usuario;
import com.universidad.demo.services.MateriaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@SessionAttributes("usuarioActual")
public class ProfileController {

    private final MateriaService materiaService; // Declara el servicio
    private final Map<String, Usuario> usuarios = Map.of(
        "admin", new Usuario("admin", "Administrador", "/img/admin.jpg", "ADMIN", 
                List.of("Gestionar usuarios", "Configurar sistema")),
        "Pedro", new Usuario("Pedro", "Pedro", "/img/Pedro.jpg", "ESTUDIANTE",
                List.of("Programación", "Matemáticas")),
        "Valen", new Usuario("Valen", "Valen", "/img/Valen.jpg", "ESTUDIANTE",
                List.of("Biología", "Química")),
        "Kevin", new Usuario("Kevin", "Kevin", "/img/Kevin.jpg", "ESTUDIANTE",
                List.of("Biología", "Química"))
    );

    // Constructor para inyectar el servicio
    public ProfileController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("usuarios", usuarios.values());
        model.addAttribute("mensaje", "Selecciona un perfil para comenzar");
        return "inicio";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam String username, Model model) {
        Usuario usuarioSeleccionado = usuarios.get(username);
        model.addAttribute("usuarioActual", usuarioSeleccionado);

        // Obtener materias aprobadas desde la base de datos
        List<String> materiasAprobadas = materiaService.obtenerMateriasAprobadas(usuarioSeleccionado.getUsername());
        // Obtener materias disponibles para cursar
        List<Materia> materiasDisponibles = materiaService.obtenerMateriasDisponibles(usuarioSeleccionado);

        // Agregar al modelo
        model.addAttribute("materiasAprobadas", materiasAprobadas);
        model.addAttribute("materiasDisponibles", materiasDisponibles);

        return "dashboard";
    }

    @PostMapping("/dashboard/actualizar-materias")
    public String actualizarMateriasAprobadas(@ModelAttribute("usuarioActual") Usuario usuario,
                                              @RequestParam(required = false) List<String> materiasSeleccionadas) {
        // Actualizar las materias aprobadas en la base de datos
        materiaService.actualizarMateriasAprobadas(usuario.getUsername(), materiasSeleccionadas);

        // Redirigir al dashboard con las materias disponibles actualizadas
        return "redirect:/dashboard?username=" + usuario.getUsername();
    }
}