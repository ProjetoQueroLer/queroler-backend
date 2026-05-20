package com.usuario.quero_ler.dtos.leitura;

import java.time.LocalDateTime;
import java.util.List;

import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.models.UsuarioLivro;

public record DiarioDeLeituraResponseDto(

    Long id,
    UsuarioLivro usuarioLivro,
    LocalDateTime inicioDaLeitura,
    LocalDateTime terminoDaLeitura,
    Integer paginasLidas,
    List<AcompanhamentoDeLeitura> comentarios,
    Integer nota,
    String tituloDaResenha,
    String resenha
) {
}
