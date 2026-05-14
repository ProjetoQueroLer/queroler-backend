package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.exceptions.especies.UsuarioLivroNaoEncontradoException;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.models.UsuarioLivroId;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiarioDeLeituraServiceImplTest {

    @InjectMocks
    private DiarioDeLeituraServiceImpl service;

    @Mock
    private DiarioDeLeituraRepository repository;

    @Mock
    private UsuarioLivroRepository usuarioLivroRepository;

    @Test
    @DisplayName("Deve salvar diario de leitura quando usuarioLivro existir")
    void deveSalvarDiarioQuandoUsuarioLivroExistir() {
        DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
                1L,
                2L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                10,
                5,
                "Título",
                "resenha");

        UsuarioLivroId id = new UsuarioLivroId();
        id.setUsuarioId(1L);
        id.setLivroId(2L);

        UsuarioLivro usuarioLivro = new UsuarioLivro();
        usuarioLivro.setId(id);
        usuarioLivro.setUsuario(new Usuario());

        when(usuarioLivroRepository.findByUsuarioIdAndLivroId(1L, 2L))
                .thenReturn(Optional.of(usuarioLivro));

        service.criar(dto);

        verify(repository).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsuarioLivroNaoEncontradoException quando não existir")
    void deveLancarQuandoNaoExistir() {
        DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
                1L,
                2L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                10,
                5,
                "Título",
                "resenha");

        when(usuarioLivroRepository.findByUsuarioIdAndLivroId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioLivroNaoEncontradoException.class, () -> service.criar(dto));

        verify(repository, never()).save(any());
    }
}
