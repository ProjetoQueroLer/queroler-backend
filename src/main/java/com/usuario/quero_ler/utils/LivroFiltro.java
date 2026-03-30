package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.models.Livro;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class LivroFiltro {
    public static Specification<Livro> filtro(String titulo, String editora, String autor){
        return (Root<Livro>root, CriteriaQuery<?> criterio, CriteriaBuilder builder)-> {
            Predicate predicate = builder.conjunction();

            if(titulo!=null && !titulo.isEmpty()){
                predicate = builder.and(predicate,builder.like(builder
                        .lower(root.get("titulo")),"%"+titulo.toLowerCase()+"%"));
            }

            if(editora!=null && !editora.isEmpty()) {
                predicate = builder.and(predicate, builder.like(builder
                        .lower(root.get("editora")), "%" + editora + "%"));
            }

            if (autor != null && !autor.isEmpty()) {

                Join<Object, Object> autoresJoin = root.join("autores");

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
}