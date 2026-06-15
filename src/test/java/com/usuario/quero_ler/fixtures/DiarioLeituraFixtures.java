package com.usuario.quero_ler.fixtures;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoLeituraResponseDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.Leitura;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;

public final class DiarioLeituraFixtures {

    private DiarioLeituraFixtures() {
    }

    public static DiarioDeLeituraRequestDto novoDiarioDeLeitura() {
        return new DiarioDeLeituraRequestDto(
                2L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                10,
                4.0,
                "Titulo",
                "resenha",
								true);
    }

    public static DiarioDeLeitura novoDiarioDeLeituraEntity(Long diarioId, Long usuarioId, Long livroId) {
        Livro livro = new Livro();
        livro.setId(livroId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Leitura leitura = new Leitura();
        leitura.setLivro(livro);
        leitura.setUsuario(usuario);

        DiarioDeLeitura diario = new DiarioDeLeitura();
        diario.setId(diarioId);
        diario.setLeitura(leitura);
        diario.setInicioDaLeitura(LocalDateTime.now().minusDays(1));
        diario.setTerminoDaLeitura(LocalDateTime.now());
        diario.setPaginasLidas(10);
        diario.setNota(4.0);
        diario.setTituloDaResenha("Titulo");
        diario.setResenha("resenha");
        return diario;
    }

		public static DiarioDeLeituraResponseDto diarioDeLeituraResponse(){
			LivroResumoResponseDto livroResumo = new LivroResumoResponseDto(1L, "titulo", 10);
		  List<AcompanhamentoLeituraResponseDto> acompanhamentoLeitura = new ArrayList<>();

			return new DiarioDeLeituraResponseDto(
					2L,
					livroResumo,
          LocalDateTime.now().minusDays(1),
          LocalDateTime.now(),
					acompanhamentoLeitura,
					0.0,
					"Titulo",
					"resenha",
					true
					);
		}
}
