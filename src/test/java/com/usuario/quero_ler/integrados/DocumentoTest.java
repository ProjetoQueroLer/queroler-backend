package com.usuario.quero_ler.integrados;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.enuns.DocumentoTipo;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.fixtures.DocumentoFixture;
import com.usuario.quero_ler.fixtures.LoginFixture;
import com.usuario.quero_ler.models.Documento;
import com.usuario.quero_ler.repository.DocumentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/gerar_banco.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/limpar_banco.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentoTest {
    @Autowired
    private TestRestTemplate template;

    @Autowired
    private DocumentoRepository repository;

    private HttpHeaders authHeaders;

    @BeforeEach
    void setUp() {
        LoginRequestDto autenticacaoDto = LoginFixture.requestDto();
        ResponseEntity<Void> loginResponse = template.postForEntity("/logins", autenticacaoDto, Void.class);
        authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("Deve criar um documento com sucesso!")
    public void deveCadastrarUmDocumentoComSucesso() {
        DocumentoRequestDto dto = DocumentoFixture.requestDto();
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        ResponseEntity<DocumentoResponseDto> resposta = template.exchange(
                "/documentos",
                HttpMethod.POST,
            new HttpEntity<>(dto, authHeaders),
                DocumentoResponseDto.class
        );

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(resposta.getBody());
        assertThat(resposta.getBody().id()).isNotNull();
        assertThat(resposta.getBody().titulo()).isEqualTo(dto.titulo());
        assertThat(resposta.getBody().tipo()).isEqualTo(dto.tipo());
        assertThat(resposta.getBody().conteudo()).isEqualTo(dto.conteudo());
        assertThat(resposta.getBody().ultimaAlteracao().truncatedTo(ChronoUnit.MINUTES)).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve retornar um documento Termo Gerais de Uso com sucesso!")
    public void deveRetornarTermoGeraisDeUsoComSucesso(){
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        ResponseEntity<DocumentoResponseDto> resposta = template.exchange(
                "/documentos/termos-gerais-de-uso",
                HttpMethod.GET,
            new HttpEntity<>(authHeaders),
                DocumentoResponseDto.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody().tipo()).isEqualTo(DocumentoTipo.TERMOS_GERAIS_DE_USO);
        assertThat(resposta.getBody().ultimaAlteracao().truncatedTo(ChronoUnit.MINUTES)).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve alterar um documento com sucesso!")
    public void deveAlterarUmDocumentoComSucesso(){
        Long id = 1L;
        DocumentoAlteracoesDto dto = new DocumentoAlteracoesDto("titulo alterado",null,"conteudo alterado");
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        ResponseEntity<Void> resposta = template.exchange(
                "/documentos/{id}",
                HttpMethod.PUT,
            new HttpEntity<>(dto, authHeaders),
                Void.class,
                id
        );

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Documento documentoSalvo = repository.findById(id).get();

        assertEquals(dto.titulo(),documentoSalvo.getTitulo());
        assertEquals(dto.conteudo(),documentoSalvo.getConteudo());
        assertEquals(agora,documentoSalvo.getUltimaAlteracao().truncatedTo(ChronoUnit.MINUTES));
    }
}
