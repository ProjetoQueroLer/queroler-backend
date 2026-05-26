package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.repository.AcompanhamentoDeLeituraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcompanhamentoDeLeituraServiceImplListarTest {

    @InjectMocks
    private AcompanhamentoDeLeituraServiceImpl service;

    @Mock
    private AcompanhamentoDeLeituraRepository acompanhamentoRepository;

    @Test
    @DisplayName("listarPorLivro deve retornar lista de DTOs quando existirem acompanhamentos")
    void listarPorLivroRetornaDto() {
        Long livroId = 1L;

        DiarioDeLeitura diario = com.usuario.quero_ler.fixtures.DiarioLeituraFixtures
                .novoDiarioDeLeituraEntity(3L, 2L, livroId);

        AcompanhamentoDeLeitura acomp = AcompanhamentoDeLeitura.builder()
                .id(10L)
                .paginaInicial(1)
                .paginaFinal(2)
                .comentario("Comentário teste")
                .diarioDeLeitura(diario)
                .build();

        when(acompanhamentoRepository.findByDiarioDeLeitura_UsuarioLivro_Livro_Id(livroId))
                .thenReturn(List.of(acomp));

        List<AcompanhamentoResponseDto> resp = service.listarPorLivro(livroId);

        assertEquals(1, resp.size());
        assertEquals("Comentário teste", resp.get(0).comentario());
        assertEquals(3L, resp.get(0).diarioId());
        assertEquals(2L, resp.get(0).usuarioId());
    }

    @Test
    @DisplayName("listarPorUsuario deve retornar lista de DTOs quando existirem acompanhamentos")
    void listarPorUsuarioRetornaDto() {
        Long usuarioId = 2L;

        DiarioDeLeitura diario = com.usuario.quero_ler.fixtures.DiarioLeituraFixtures
                .novoDiarioDeLeituraEntity(5L, usuarioId, 1L);

        AcompanhamentoDeLeitura acomp = AcompanhamentoDeLeitura.builder()
                .id(11L)
                .paginaInicial(5)
                .paginaFinal(10)
                .comentario("Outro comentário")
                .diarioDeLeitura(diario)
                .build();

        when(acompanhamentoRepository.findByDiarioDeLeitura_UsuarioLivro_Usuario_Id(usuarioId))
                .thenReturn(List.of(acomp));

        List<AcompanhamentoResponseDto> resp = service.listarPorUsuario(usuarioId);

        assertEquals(1, resp.size());
        assertEquals("Outro comentário", resp.get(0).comentario());
        assertEquals(5L, resp.get(0).diarioId());
        assertEquals(usuarioId, resp.get(0).usuarioId());
    }
}
