package com.usuario.quero_ler.fixtures;

import java.time.LocalDateTime;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.UsuarioLivro;

public final class DiarioLeituraFixtures {

    private DiarioLeituraFixtures() {
    }

    public static DiarioDeLeituraRequestDto novoDiarioDeLeitura() {
        return new DiarioDeLeituraRequestDto(
                2L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                10,
                4,
                "Titulo",
                "resenha");
    }

    public static DiarioDeLeitura novoDiarioDeLeituraEntity(Long diarioId, Long usuarioId, Long livroId) {
        Livro livro = new Livro();
        livro.setId(livroId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        UsuarioLivro usuarioLivro = new UsuarioLivro();
        usuarioLivro.setLivro(livro);
        usuarioLivro.setUsuario(usuario);

        DiarioDeLeitura diario = new DiarioDeLeitura();
        diario.setId(diarioId);
        diario.setUsuarioLivro(usuarioLivro);
        diario.setInicioDaLeitura(LocalDateTime.now().minusDays(1));
        diario.setTerminoDaLeitura(LocalDateTime.now());
        diario.setPaginasLidas(10);
        diario.setNota(4);
        diario.setTituloDaResenha("Titulo");
        diario.setResenha("resenha");
        return diario;
    }
}
