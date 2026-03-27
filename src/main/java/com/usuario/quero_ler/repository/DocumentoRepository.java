package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.enuns.DocumentoTipo;
import com.usuario.quero_ler.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    Documento findTopByTipoOrderByUltimaAlteracaoDesc(DocumentoTipo tipo);
}