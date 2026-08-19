package com.usuario.quero_ler.service.implementacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enums.LeituraStatus;
import com.usuario.quero_ler.exceptions.especies.LeituraEstadoInvalidoException;
import com.usuario.quero_ler.exceptions.especies.LeituraNaoEncontradaException;
import com.usuario.quero_ler.exceptions.especies.UsuarioJaPossueOLivroException;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Leitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.LeituraRepository;
import com.usuario.quero_ler.service.LeituraService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LeituraServiceImpl implements LeituraService {
	private final LivroService livroService;
	private final LeituraRepository repository;
	private final LivroMapper livroMapper;
	private final LoginService loginService;


	@Override
	public void adicionar(Long idLivro, LeituraStatus status) {
		Usuario usuario = loginService.getUsuarioLogado().getUsuario();
		Long idUsuario = usuario.getId();

		Optional<Leitura> usuarioLeitura = repository.findByUsuarioIdAndLivroId(idUsuario, idLivro);
		if (usuarioLeitura.isPresent()) {
				throw new UsuarioJaPossueOLivroException("O usuario já possue o livro na estante.");
		}

		
		Livro livro = livroService.buscar(idLivro);

		Leitura leitura = new Leitura();
		leitura.setUsuario(usuario);
		leitura.setLivro(livro);
		ControleStatusLeitura(leitura, status);

		leitura.setStatus(status);
		repository.save(leitura);
    }

	@Override
	public void remover(Long idLivro) {
		Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
		Leitura leitura = repository.findByUsuarioIdAndLivroId(idUsuario, idLivro)
				.orElseThrow(() -> new LeituraNaoEncontradaException("Leitura não encontrada para este usuário e livro."));
		repository.delete(leitura);
	}

	@Override
	public Page<LivroTelaLeituraResponse> lista(Long id, Pageable pageable) {

		Page<Leitura> pageUsuarioLivros = repository.findAllByUsuarioId(id, pageable);

		List<LivroTelaLeituraResponse> resposta = new ArrayList<>();

		for (Leitura leitura : pageUsuarioLivros.getContent()) {
			resposta.add(
					livroMapper.toLivroTelaLeituraResponse(
							leitura.getLivro(),
							leitura.getStatus()));
		}

		return new PageImpl<>(resposta, pageable, pageUsuarioLivros.getTotalElements());
	}

	@Override
	public void ControleStatusLeitura(Leitura leitura, LeituraStatus status) {

		switch (leitura.getStatus()) {
			case null:
				if (status.equals(LeituraStatus.LIVROS_QUE_QUERO_LER) ||
						status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO) ||
						status.equals(LeituraStatus.LIVROS_LIDOS)) {
					leitura.setStatus(status);
					if (status.equals(LeituraStatus.LIVROS_LIDOS)) leitura.setLido(true);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente as transições Quero ler, lendo e lidos podem ser realizadas",
							new RuntimeException());
				}

				break;
			case LIVROS_QUE_QUERO_LER:
				if (status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO) ||
						status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
						status.equals(LeituraStatus.LIVROS_LIDOS)) {
					leitura.setStatus(status);
					if (status.equals(LeituraStatus.LIVROS_LIDOS)) leitura.setLido(true);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente as transições lendo, abandonados e lidos podem ser realizadas",
							new RuntimeException());
				}

				break;

			case LIVROS_QUE_ESTOU_LENDO:
				if (status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
						status.equals(LeituraStatus.LIVROS_LIDOS)) {
					leitura.setStatus(status);
					if (status.equals(LeituraStatus.LIVROS_LIDOS)) leitura.setLido(true);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente as transições abandonados e lidos podem ser realizadas",
							new RuntimeException());
				}

				break;
			case RELENDO:
				if (status.equals(LeituraStatus.LIVROS_ABANDONADOS) ||
						status.equals(LeituraStatus.LIVROS_LIDOS)) {
					leitura.setStatus(status);
					if (status.equals(LeituraStatus.LIVROS_LIDOS)) leitura.setLido(true);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente as transições abandonados e lidos podem ser realizadas",
							new RuntimeException());
				}
				break;
			case LIVROS_ABANDONADOS:
				if (status.equals(LeituraStatus.LIVROS_QUE_ESTOU_LENDO) ||
						status.equals(LeituraStatus.LIVROS_QUE_QUERO_LER)) {
					leitura.setStatus(status);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente as transições estou lendo e quero ler podem ser realizadas",
							new RuntimeException());
				}

				break;
			case LIVROS_LIDOS:
				if (status.equals(LeituraStatus.RELENDO)) {
					leitura.setStatus(status);
				} else {
					throw new LeituraEstadoInvalidoException(
							"Transição inválida, para o estado atual somente a transições relendo pode ser realizada",
							new RuntimeException());
				}
				break;

			default:
				throw new LeituraEstadoInvalidoException(
						"Estado inexistente",
						new RuntimeException());

		}
	}
}
