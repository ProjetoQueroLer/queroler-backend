package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhamentoDeLeituraRepository extends JpaRepository<AcompanhamentoDeLeitura, Long> {
}
