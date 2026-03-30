package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.models.Autor;
import org.springframework.stereotype.Component;

@Component
public class AutorMapper {

    public Autor toEntity(AutorRequest dto){
        Autor autor= new Autor();
        autor.setNome(dto.nome());
        return autor;
    }

    public AutorResponse autorResponse(Autor autor){
        return new AutorResponse(autor.getId(), autor.getNome());
    }
}
