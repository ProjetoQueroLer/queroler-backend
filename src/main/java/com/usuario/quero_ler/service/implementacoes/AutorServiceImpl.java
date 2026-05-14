package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.mappers.AutorMapper;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.repository.AutorRepository;
import com.usuario.quero_ler.service.AutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AutorServiceImpl implements AutorService {
    private final AutorRepository repository;
    private final AutorMapper mapper;

    @Override
    public Autor criar(AutorRequest dto) {
        log.info("AutorServiceImpl.criar - nome={}", dto.nome());
        Autor autor = mapper.toEntity(dto);
        autor = repository.save(autor);
        log.debug("Autor criado id={}", autor.getId());
        return autor;
    }
}
