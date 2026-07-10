package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.exceptions.especies.DataInvalidaException;
import com.usuario.quero_ler.exceptions.especies.MetaDeLeituraJaCadastradaException;
import com.usuario.quero_ler.mappers.MetaLeituraMapper;
import com.usuario.quero_ler.models.MetaLeitura;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.MetaLeituraRepository;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.MetaLeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class MetaLeituraLeituraServiceImpl implements MetaLeituraService {

    private final LoginService loginService;
    private final MetaLeituraRepository repository;
    private final MetaLeituraMapper mapper;

    @Override
    public void novaMeta(MetaRequestDto dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        validarNovaMeta(dto, usuario);
        MetaLeitura novaMeta = mapper.toMetaLeitura(dto);
        novaMeta.setUsuario(usuario);
        novaMeta = repository.save(novaMeta);
    }

    protected void validarNovaMeta(MetaRequestDto dto, Usuario usuario) {
        Integer anoAtual = LocalDate.now().getYear();
        Integer anoDto = dto.ano() != null ? dto.ano() : anoAtual;

        if (anoDto < anoAtual) {
            throw new DataInvalidaException("O ano informado não pode ser anterior ao corrente.");
        }

        if (repository.existsByUsuarioAndAno(usuario, anoDto)) {
            throw new MetaDeLeituraJaCadastradaException(
                    "Já há meta cadastrada para o ano de: " + anoDto + "."
            );
        }
    }

}