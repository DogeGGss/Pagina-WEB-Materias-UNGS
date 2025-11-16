package com.universidad.demo.services;

import com.universidad.demo.models.Materia;
import com.universidad.demo.models.MateriaAprobada;
import com.universidad.demo.models.Usuario;
import com.universidad.demo.repositories.MateriaAprobadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    private final Map<String, Materia> todasLasMaterias;
    private final MateriaAprobadaRepository materiaAprobadaRepository;

    public MateriaService(Map<String, Materia> todasLasMaterias, MateriaAprobadaRepository materiaAprobadaRepository) {
        this.todasLasMaterias = todasLasMaterias;
        this.materiaAprobadaRepository = materiaAprobadaRepository;
    }
    
    // Método para obtener todas las materias
    public Map<String, Materia> obtenerTodasLasMaterias() {
        return todasLasMaterias;
    }

    // Método para obtener las materias disponibles para cursar
    public List<Materia> obtenerMateriasDisponibles(Usuario usuario) {
        // Obtener los códigos de las materias aprobadas del usuario
        List<String> materiasAprobadas = materiaAprobadaRepository.findByUsername(usuario.getUsername())
                .stream()
                .map(MateriaAprobada::getMateriaCodigo)
                .collect(Collectors.toList());
    
        // Depuración: Imprimir las materias aprobadas
        System.out.println("Materias aprobadas del usuario: " + materiasAprobadas);
    
        // Contar materias de Licenciatura aprobadas (todas las materias del plan completo)
        long materiasLicenciaturaAprobadas = materiasAprobadas.stream()
            .filter(codigo -> todasLasMaterias.containsKey(codigo))
            .count();
    
        // Filtrar las materias disponibles
        List<Materia> materiasDisponibles = todasLasMaterias.values().stream()
                .filter(materia -> !materiasAprobadas.contains(materia.getCodigo())) // Excluir materias aprobadas
                .filter(materia -> {
                    // LABI requiere 14 materias aprobadas de Licenciatura
                    if ("LABI".equals(materia.getCodigo())) {
                        boolean cumpleRequisito = materiasLicenciaturaAprobadas >= 14;
                        if (!cumpleRequisito) {
                            System.out.println("LABI no disponible - Requiere 14 materias aprobadas, tiene: " + materiasLicenciaturaAprobadas);
                        }
                        return cumpleRequisito;
                    }
                    
                    // Para otras materias, verificar correlativas normales
                    boolean puedeCursar = materia.puedeCursar(todasLasMaterias, materiasAprobadas);
                    if (!puedeCursar) {
                        System.out.println("No cumple correlativas para: " + materia.getCodigo() + 
                            " - Correlativas requeridas: " + materia.getCorrelativasCodigos() + 
                            " - Materias aprobadas: " + materiasAprobadas);
                    }
                    return puedeCursar;
                })
                .collect(Collectors.toList());
    
        // Depuración: Imprimir las materias disponibles
        System.out.println("Materias disponibles para cursar: " + materiasDisponibles.stream()
                .map(Materia::getCodigo)
                .collect(Collectors.toList()));
    
        return materiasDisponibles;
    }
    // Método para obtener las materias aprobadas de un usuario
    public List<String> obtenerMateriasAprobadas(String username) {
        return materiaAprobadaRepository.findByUsername(username)
                .stream()
                .map(MateriaAprobada::getMateriaCodigo)
                .collect(Collectors.toList());
    }
    @Transactional
public void eliminarMateriaAprobada(String username, String materiaCodigo) {
    // Buscar la materia aprobada por usuario y código
    List<MateriaAprobada> materias = materiaAprobadaRepository.findByUsername(username);
    materias.stream()
            .filter(materia -> materia.getMateriaCodigo().equals(materiaCodigo))
            .findFirst()
            .ifPresent(materiaAprobadaRepository::delete);

    System.out.println("Materia eliminada: " + materiaCodigo + " para el usuario: " + username);
}
    // Método para actualizar las materias aprobadas de un usuario
    @Transactional
public void actualizarMateriasAprobadas(String username, List<String> materiasSeleccionadas) {
    // Obtener las materias aprobadas actuales del usuario
    List<String> materiasAprobadasActuales = materiaAprobadaRepository.findByUsername(username)
            .stream()
            .map(MateriaAprobada::getMateriaCodigo)
            .collect(Collectors.toList());

    // Combinar las materias actuales con las nuevas seleccionadas
    if (materiasSeleccionadas != null) {
        materiasAprobadasActuales.addAll(materiasSeleccionadas);
    }

    // Eliminar duplicados
    List<String> materiasUnicas = materiasAprobadasActuales.stream()
            .distinct()
            .collect(Collectors.toList());

    // Eliminar todas las materias aprobadas actuales del usuario
    materiaAprobadaRepository.deleteByUsername(username);

    // Guardar la lista combinada de materias aprobadas
    List<MateriaAprobada> nuevasMaterias = materiasUnicas.stream()
            .map(codigo -> new MateriaAprobada(username, codigo))
            .collect(Collectors.toList());
    if (!nuevasMaterias.isEmpty()) {
        materiaAprobadaRepository.saveAll(nuevasMaterias);
    }
}
}