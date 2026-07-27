package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;
import com.usuario.quero_ler.exceptions.especies.DataInvalidaException;
import com.usuario.quero_ler.exceptions.especies.LivroJaCadastradoException;
import com.usuario.quero_ler.exceptions.especies.MetaDeLeituraJaCadastradaException;
import com.usuario.quero_ler.exceptions.especies.MetaDeLeituraNaoEncontradaException;
import com.usuario.quero_ler.mappers.MetaLeituraMapper;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.MetaLeitura;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.MetaLeituraRepository;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.MetaLeituraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MetaLeituraServiceImpl implements MetaLeituraService {

    private final LoginService loginService;
    private final MetaLeituraRepository repository;
    private final MetaLeituraMapper mapper;
    private final LivroService livroService;

    @Override
    public void novaMeta(MetaRequestDto dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        validarNovaMeta(dto, usuario);
        MetaLeitura novaMeta = mapper.toMetaLeitura(dto);
        novaMeta.setUsuario(usuario);
        novaMeta = repository.save(novaMeta);
    }

    @Transactional
    @Override
    public void atualizar(MetaRequestDto dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();

        Integer anoAtual = LocalDate.now().getYear();
        Integer ano = dto.ano() != null ? dto.ano() : anoAtual;

        MetaLeitura meta = repository.findByUsuarioAndAno(usuario, ano)
                .orElseThrow(() -> new MetaDeLeituraNaoEncontradaException(
                        "Não há meta cadastrada para o ano de: " + ano + "."));

        mapper.atualizarMetaLeitura(meta, dto);

        repository.save(meta);
    }

    @Transactional
    @Override
    public void deletar() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        repository.deleteAllByUsuario(usuario);
    }

    @Override
    public MetaResponseDto getMetas() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        int anoAtual = LocalDate.now().getYear();
        MetaLeitura metaAtual = getMetaLeitura(usuario,anoAtual);

        MetaResponseDto responseDto = mapper.metaResponseDto(metaAtual);
        return responseDto;
    }

    @Override
    public void adicionarLivro(Long id){
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        Livro livro = livroService.buscar(id);
        Integer anoAtual = LocalDate.now().getYear();
        MetaLeitura metaLeitura = getMetaLeitura(usuario,anoAtual);

        if(repository.existsByIdAndLivrosMetaLivroId(metaLeitura.getId(),livro.getId())){
            throw new LivroJaCadastradoException("Livro já adicionado na meta deste ano!");
        }

        metaLeitura.adicionarLivro(livro);
        repository.save(metaLeitura);
    }

    protected void validarNovaMeta(MetaRequestDto dto, Usuario usuario) {
        Integer anoAtual = LocalDate.now().getYear();
        Integer anoDto = dto.ano() != null ? dto.ano() : anoAtual;

        if (anoDto < anoAtual) {
            throw new DataInvalidaException("O ano informado não pode ser anterior ao corrente.");
        }

        if (repository.existsByUsuarioAndAno(usuario, anoDto)) {
            throw new MetaDeLeituraJaCadastradaException(
                    "Já há meta cadastrada para o ano de: " + anoDto + ".");
        }
    }

    public MetaLeitura getMetaLeitura(Usuario usuario, Integer ano){
        return repository.findByUsuarioAndAno(usuario, ano)
                .orElseThrow(() -> new MetaDeLeituraNaoEncontradaException(
                        "Não há metas para o ano de: " + ano + "."
                ));
    }

}