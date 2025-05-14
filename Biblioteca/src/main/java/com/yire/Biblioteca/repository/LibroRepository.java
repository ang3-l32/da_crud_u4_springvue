package com.yire.Biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yire.Biblioteca.model.LibroModel;

public interface LibroRepository extends JpaRepository<LibroModel, Long>{

}
