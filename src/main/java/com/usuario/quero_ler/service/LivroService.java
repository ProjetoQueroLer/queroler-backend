package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface LivroService {
    LivroResponse criar(LivroRequest dto, MultipartFile capaDoLivro);
    Page<LivroCardResponse> listar(Pageable pageable);
    Page<LivroResponse> listarPopulares();
    LivroResponse buscarIsbn(String isbn);
    Page<LivroCardResponse> buscar(String titulo, String editora, String autor, Pageable pageable);
    void inserirCapaDoLivro(Long id, MultipartFile capaDoLivro);
    byte[] buscarCapa(Long id);
    Livro buscar(Long id);
    Page<LivroDetalhadoResponse> getLivrosDoUsuario(Long id, Pageable pageable);
    void alterarStatusDoLivroNoUsuario(Long id, Long idUsuario, LivroStatus status);
    Page<LivroTelaLeituraResponse> getLivrosTelaDeLeituraDoUsuario(Long id,Pageable pageable);
}