package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar um Usuário com sucesso")
    void deveCriarUmUsuarioComSucesso() throws Exception {

        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        UsuarioRequestDto request = UserFixture.requestDto();
        Usuario usuario = UserFixture.entidadeCompleta(user);
        UsuarioResponseDto response = UserFixture.response(usuario);

        MockMultipartFile dados = new MockMultipartFile(
                "dados",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile imagem = new MockMultipartFile(
                "imagem",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "imagem fake".getBytes()
        );

        when(service.criar(any(UsuarioRequestDto.class), any(MultipartFile.class)))
                .thenReturn(response);

        mockMvc.perform(multipart("/usuarios")
                        .file(dados)
                        .file(imagem)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.nome").value(response.nome()))
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.cpf").value(response.cpf()))
                .andExpect(jsonPath("$.dataDeNascimento")
                        .value(response.dataDeNascimento().toString()))
                .andExpect(jsonPath("$.cidade").value(response.cidade()))
                .andExpect(jsonPath("$.estado").value(response.estado()))
                .andExpect(jsonPath("$.pais").value(response.pais()));

        verify(service).criar(any(UsuarioRequestDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("Deve retornar dados do usuário")
    void deveRetornarDadosDoUsuario() throws Exception {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        UsuarioDadosResponse response = UserFixture.responseDados(usuario);

        when(service.getDadosDoUsuario()).thenReturn(response);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(response.nome()))
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.cidade").value(response.cidade()))
                .andExpect(jsonPath("$.estado").value(response.estado()))
                .andExpect(jsonPath("$.pais").value(response.pais()))
                .andExpect(jsonPath("$.foto").value(response.foto()));

        verify(service).getDadosDoUsuario();
    }

    @Test
    @DisplayName("Deve inserir dados adicionais ao usuário com sucesso")
    void deveInserirDadosAdcionais() throws Exception {
        UsuarioDadosComplementarRequest complementarRequest = UserFixture.requestDadosComplementares();

        mockMvc.perform(put("/usuarios/dados-adicionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complementarRequest)))
                .andExpect(status().isNoContent());

        verify(service).adicionarDados(complementarRequest);
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso")
    void deveAlterarASenhaDoUsuarioComSucesso() throws Exception {
        UsuarioAlterarSenhaRequest request = new UsuarioAlterarSenhaRequest("Teste123&", "Senha1232@");

        mockMvc.perform(put("/usuarios/alterar-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).alterarSenha(request);
    }

    @Test
    @DisplayName("Deve atualizar usuário leitor com sucesso")
    void deveAtualizarUsuarioLeitorComSucesso() throws Exception {
        UsuarioAtualizadoLeitorRequest request = new UsuarioAtualizadoLeitorRequest("Nome atualizado",
                null, null, null, null, null, null);

        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(request);
    }

    @Test
    @DisplayName("Deve atualizar usuário administrador com sucesso")
    void deveAtualizarUsuarioAdministradorComSucesso() throws Exception {
        UsuarioAtualizadoAdministradorRequest request = new UsuarioAtualizadoAdministradorRequest(LocalDate.of(2015, 06, 03),
                null, null, null, null);

        mockMvc.perform(put("/usuarios/administrador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(request);
    }

    @Test
    @DisplayName("Deve apagar um perfil com sucesso")
    void deveApagarUmPerfilComSucesso() throws Exception {

        mockMvc.perform(delete("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).excluirPerfil();
    }

    @Test
    @DisplayName("Deve adicionar um livro na estante do usuario.")
    void deveAdicionarUmLivroNaEstanteDoUsuario() throws Exception {
        Long idLivro = 10L;
        LivroStatus status = LivroStatus.LIVROS_QUE_QUERO_LER;

        mockMvc.perform(post("/usuarios/livro")
                        .param("idLivro", idLivro.toString())
                        .param("status", String.valueOf(status))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service).adicionarLivro(idLivro, status);
    }

}
