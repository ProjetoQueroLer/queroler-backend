package com.usuario.quero_ler.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoLeituraResponseDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;
import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.UsuarioLivro;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiarioLeituraMapper {

    public DiarioDeLeitura toEntity(DiarioDeLeituraRequestDto dto) {
        DiarioDeLeitura diario = new DiarioDeLeitura();
        diario.setInicioDaLeitura(dto.inicioDaLeitura());
				diario.setTerminoDaLeitura(dto.terminoDaLeitura());
        diario.setPaginasLidas(dto.paginasLidas());
        diario.setNota(dto.nota());
        diario.setTituloDaResenha(dto.tituloDaResenha());
        diario.setResenha(dto.resenha());
				diario.setSpoiler(dto.spoiler());
        return diario;
    }
		public DiarioDeLeituraResponseDto toResponse(DiarioDeLeitura diario){
			if (diario == null) return null;
			
			LivroResumoResponseDto livro = toLivroResumo(diario.getUsuarioLivro());
			List<AcompanhamentoLeituraResponseDto> acompanhamentos = diario.getComentarios()
				.stream()
				.map(this::toAcompanhamentoResponse)
				.toList();

			return new DiarioDeLeituraResponseDto(
					diario.getId(),
					livro,
					diario.getInicioDaLeitura(),
					diario.getTerminoDaLeitura(),
					acompanhamentos,
					diario.getNota(),
					diario.getTituloDaResenha(),
					diario.getResenha(),
					diario.isSpoiler()
					);
			
		}
         private LivroResumoResponseDto toLivroResumo(UsuarioLivro usuarioLivro) {
             Livro livro = usuarioLivro.getLivro();
             return new LivroResumoResponseDto( 
                 livro.getId(),
                 livro.getTitulo(),
                 livro.getNumeroDePaginas()
             );
         }

         private AcompanhamentoLeituraResponseDto toAcompanhamentoResponse(AcompanhamentoDeLeitura acomp) {
             return new AcompanhamentoLeituraResponseDto(
                 acomp.getId(),
                 acomp.getPaginaInicial(),
                 acomp.getPaginaFinal(),
                 acomp.getComentario()
             );
         }
}
