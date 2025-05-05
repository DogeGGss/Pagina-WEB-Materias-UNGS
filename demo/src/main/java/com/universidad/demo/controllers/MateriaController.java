package com.universidad.demo.controllers;

import com.universidad.demo.models.Materia;
import com.universidad.demo.models.Usuario;
import com.universidad.demo.services.MateriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/materias")
@SessionAttributes("usuarioActual")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public String listarMateriasDisponibles(@ModelAttribute("usuarioActual") Usuario usuario, Model model) {
        System.out.println("Usuario actual: " + usuario.getUsername());
        List<Materia> materiasDisponibles = materiaService.obtenerMateriasDisponibles(usuario);
        System.out.println("Materias disponibles para el usuario " + usuario.getUsername() + ": " +
                materiasDisponibles.stream().map(Materia::getCodigo).toList());
        model.addAttribute("materiasDisponibles", materiasDisponibles);
        return "materias-disponibles"; // Nombre de la plantilla Thymeleaf
    }
    @PostMapping("/dashboard/eliminar-materia")
    public String eliminarMateriaAprobada(@ModelAttribute("usuarioActual") Usuario usuario,
                                          @RequestParam String materiaCodigo) {
        System.out.println("Usuario actual: " + usuario.getUsername());
        System.out.println("Materia a eliminar: " + materiaCodigo);
    
        // Eliminar la materia aprobada del usuario
        materiaService.eliminarMateriaAprobada(usuario.getUsername(), materiaCodigo);
    
        // Redirigir al dashboard con la lista actualizada
        return "redirect:/dashboard?username=" + usuario.getUsername();
    }
    @PostMapping("/dashboard/actualizar-materias")
    public String actualizarMateriasAprobadas(@ModelAttribute("usuarioActual") Usuario usuario,
                                              @RequestParam(required = false) List<String> materiasSeleccionadas) {
        System.out.println("Usuario actual: " + usuario.getUsername());
        System.out.println("Materias seleccionadas para aprobar: " + materiasSeleccionadas);

        // Actualizar las materias aprobadas en la base de datos
        materiaService.actualizarMateriasAprobadas(usuario.getUsername(), materiasSeleccionadas);

        // Redirigir al dashboard con las materias disponibles actualizadas
        return "redirect:/dashboard?username=" + usuario.getUsername();
    }
}