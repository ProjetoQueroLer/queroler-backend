package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enums.LeituraStatus;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Leitura;
import com.usuario.quero_ler.repository.LeituraRepository;
import com.usuario.quero_ler.service.LeituraService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeituraServiceImpl implements LeituraService {
    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final LeituraRepository repository;
    private final LivroMapper livroMapper;

    @Override
    public void adicionar(Long idUsuario, Long idLivro) {

    }

    @Override
    public Page<LivroTelaLeituraResponse> lista(Long id, Pageable pageable) {

        Page<Leitura> pageUsuarioLivros = repository.findAllByUsuarioId(id, pageable);

        List<LivroTelaLeituraResponse> resposta = new ArrayList<>();

        for (Leitura leitura : pageUsuarioLivros.getContent()) {
            resposta.add(
                    livroMapper.toLivroTelaLeituraResponse(
                            leitura.getLivro(),
                            leitura.getStatus()
                    )
            );
        }

        return new PageImpl<>(resposta, pageable, pageUsuarioLivros.getTotalElements());
    }

    @Override
    public Boolean ControleStatusLeitura(Leitura leitura , LeituraStatus status) {
			

			switch (leitura.getStatus()) {
				case null:
					if (
							status.equals(LeituraStatus.LIVROS_QUE_QUERO_LER) ||
							status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO) ||
							status.equals(LeituraStatus.LIVROS_LIDOS)
								){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
						throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para os estados LIVROS_QUE_QUERO_LER, LIVROS_QUE_ESTOU_LENDO e LIVROS_LIDOS.");
					}
					
					break;
				case LIVROS_QUE_QUERO_LER:
					if (
							status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO) ||
							status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
							status.equals(LeituraStatus.LIVROS_LIDOS)
						 ){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
						throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para os estados LIVROS_QUE_ESTOU_LENDO, LIVROS_ABNDONADOS e LIVROS_LIDOS.");
					}
					
					break;
			
				case LIVROS_QUE_ESTOU_LENDO:
					if ( 
							status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
							status.equals(LeituraStatus.LIVROS_LIDOS)
							){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
						throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para os estado LIVROS_ABNDONADOS e LIVROS_LIDOS.");
					}
					
					break;
				case RELENDO:
					if (
							status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
							status.equals(LeituraStatus.LIVROS_LIDOS)
							){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
					 throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para os estado LIVROS_ABNDONADOS e LIVROS_LIDOS.");
					}
					break;
				case LIVROS_ABANDONADOS:
					if (status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO)
							){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
					 throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para os estados LIVROS_QUE_ESTOU_LENDO e RELENDO.");
					}
					
					break;
				case LIVROS_LIDOS:
					if (
							status.equals(LeituraStatus.RELENDO)
							){
						leitura.setStatus(status);
					}else{
						// Mensagem de transição inválida.
					 throw new RuntimeException("Transição inválida. Para o estado atual, somente é permitido transicionar para o estado RELENDO.");
					}
					break;

				default:
					 throw new RuntimeException("Estado invalido");

			}
			return false;
    }
}
