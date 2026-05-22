package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.repository.AcompanhamentoDeLeituraRepository;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AcompanhamentoLeituraComentarioServiceTest {

    @InjectMocks
    private AcompanhamentoDeLeituraServiceImpl service;

    @Mock
    private DiarioDeLeituraRepository repository;

    @Mock
    private AcompanhamentoDeLeituraRepository acompanhamentoRepository;

    @Test
    @DisplayName("Deve salvar acompanhamento quando diario existir")
    void deveSalvarAcompanhamentoQuandoDiarioExistir() {
        Long diarioId = 1L;

        AcompanhamentoRequestDto dto = new AcompanhamentoRequestDto(10, 20, "Comentário teste");

        DiarioDeLeitura diario = new DiarioDeLeitura();
        diario.setId(diarioId);

        when(repository.findById(diarioId)).thenReturn(Optional.of(diario));

        service.adicionarComentario(diarioId, dto);

        verify(acompanhamentoRepository).save(any(AcompanhamentoDeLeitura.class));
        verify(repository).save(any(DiarioDeLeitura.class));
    }

    @Test
    @DisplayName("Deve lançar DadosDiarioInvalidoException quando diario nao existir")
    void deveLancarQuandoDiarioNaoExistir() {
        Long diarioId = 2L;

        AcompanhamentoRequestDto dto = new AcompanhamentoRequestDto(1, 2, "x");

        when(repository.findById(diarioId)).thenReturn(Optional.empty());

        assertThrows(DadosDiarioInvalidoException.class, () -> service.adicionarComentario(diarioId, dto));

        verify(acompanhamentoRepository, never()).save(any());
        verify(repository, never()).save(any());
    }
}
