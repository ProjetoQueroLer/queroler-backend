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
import com.usuario.quero_ler.models.Leitura;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiarioLeituraMapper {

    public DiarioDeLeitura toEntity(DiarioDeLeituraRequestDto dto) {
        DiarioDeLeitura diario = new DiarioDeLeitura();
        diario.setInicioDaLeitura(dto.inicioDaLeitura());
				diario.setTerminoDaLeitura(dto.terminoDaLeitura());
        diario.setPaginasLidas(dto.paginasLidas());
        diario.setNota(dto.nota() != null ? dto.nota(): 0.0 );
        diario.setTituloDaResenha(dto.tituloDaResenha());
        diario.setResenha(dto.resenha());
				diario.setSpoiler(dto.spoiler() != null ? dto.spoiler(): false);
        return diario;
    }
		public DiarioDeLeituraResponseDto toResponse(DiarioDeLeitura diario){
			if (diario == null) return null;
			
			LivroResumoResponseDto livro = toLivroResumo(diario.getLeitura());
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
					diario.getNota() != null ? diario.getNota(): 0.0,
					diario.getTituloDaResenha(),
					diario.getResenha(),
					diario.getSpoiler() != null ? diario.getSpoiler(): false
					);
			
		}
         private LivroResumoResponseDto toLivroResumo(Leitura leitura) {
             Livro livro = leitura.getLivro();
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
