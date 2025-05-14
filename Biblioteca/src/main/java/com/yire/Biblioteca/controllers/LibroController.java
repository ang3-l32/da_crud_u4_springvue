package com.yire.Biblioteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yire.Biblioteca.model.LibroModel;
import com.yire.Biblioteca.repository.LibroRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/libro")
@CrossOrigin(origins = "http://localhost:5173/")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/traer-libro")
    public List<LibroModel> TraerLibro() {
        return libroRepository.findAll();
    }

    @GetMapping("/traer-lib/{id}")
    public ResponseEntity<LibroModel> TraerUnLibro(@PathVariable long id) {
        return libroRepository.findById(id)
            .map(libro -> ResponseEntity.ok(libro))
            .orElse(ResponseEntity.notFound().build());
    }
    

    @PostMapping("/insertar-libro")
    public LibroModel insertarLibro(@RequestBody LibroModel libro) {  
        return libroRepository.save(libro);
    }
    
    @PutMapping("editar-libro/{id}")
    public ResponseEntity<LibroModel> actualizarLibro(@PathVariable Long id, @RequestBody LibroModel libro) {
        return libroRepository.findById(id).map(libroExistente -> {
            libroExistente.setTitulo(libro.getTitulo());
            libroExistente.setAutor(libro.getAutor());
            libroExistente.setCategoria(libro.getCategoria());
            libroExistente.setEditorial(libro.getEditorial());
            libroExistente.setNump(libro.getNump());
            libroExistente.setDescripcion(libro.getDescripcion());
            libroExistente.setImagenURL(libro.getImagenURL());
            LibroModel actualizado = libroRepository.save(libroExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar-libro/{id}")
    public void eliminarLibro(@PathVariable long id){
        libroRepository.deleteById(id);
    }
    

}
