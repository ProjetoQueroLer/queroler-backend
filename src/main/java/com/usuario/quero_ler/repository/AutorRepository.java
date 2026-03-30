package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor,Long> {
}
