package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.mappers.AutorMapper;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.repository.AutorRepository;
import com.usuario.quero_ler.service.AutorServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class AutorServiceImpl implements AutorServiceI {
    private final AutorRepository repository;
    private final AutorMapper mapper;

    @Override
    public Autor criar(AutorRequest dto) {
        Autor autor = mapper.toEntity(dto);
        autor = repository.save(autor);
        return autor;
    }
}
