package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class LivroFiltro {
    public static Specification<Livro> filtro(String titulo, String editora, String autor) {
        return (Root<Livro> root, CriteriaQuery<?> criterio, CriteriaBuilder builder) -> {
            Predicate predicate = builder.conjunction();

            if (titulo != null && !titulo.isEmpty()) {
                predicate = builder.and(predicate, builder.like(builder
                        .lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
            }

            if (editora != null && !editora.isEmpty()) {
                predicate = builder.and(predicate, builder.like(builder
                        .lower(root.get("editora")), "%" + editora + "%"));
            }

            if (autor != null && !autor.isEmpty()) {

                Join<Livro, Autor> autoresJoin = root.join("autores", JoinType.LEFT);

                criterio.distinct(true);

                predicate = builder.and(predicate,
                        builder.like(
                                builder.lower(autoresJoin.get("nome")),
                                "%" + autor.toLowerCase() + "%"
                        )
                );
            }
            return predicate;
        };
    }

    public static Pageable top5MaisVotados() {
        return PageRequest.of(
                0,
                5,
                Sort.by("quantidadeDeUso").descending()
                        .and(Sort.by("dataDeCadastro").descending())
        );
    }
}