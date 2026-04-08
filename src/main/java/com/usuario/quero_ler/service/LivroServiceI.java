package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.livro.BuscaDeLivrosRequest;
import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.models.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LivroServiceI {
    LivroResponse criar(LivroRequest dto, MultipartFile capaDoLivro);
    Page<LivroCardResponse> listar(Pageable pageable);
    public Page<LivroResponse> listarPopulares();
    LivroResponse buscarIsbn(String isbn);
    Page<LivroCardResponse> buscar(String titulo, String editora, String autor, Pageable pageable);
    void inserirCapaDoLivro(Long id, MultipartFile capaDoLivro);
    byte[] buscarCapa(Long id);
    Livro buscar(Long id);
}