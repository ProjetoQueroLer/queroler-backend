package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.DiarioDeLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiarioDeLeituraRepository extends JpaRepository<DiarioDeLeitura, Long> {

}
