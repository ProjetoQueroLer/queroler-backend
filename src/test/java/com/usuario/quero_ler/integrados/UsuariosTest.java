package com.usuario.quero_ler.integrados;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.UsuarioRepository;
import com.usuario.quero_ler.support.AbstractIntegrationTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/gerar_banco.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/limpar_banco.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuariosTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UsuarioRepository repository;

    private HttpHeaders authHeaders;

    private void logar(Long id) {
        Usuario usuario = repository.findById(id).get();
        LoginRequestDto autenticacaoDto = new LoginRequestDto(usuario.getUser().getUser(), "Teste123&");
        ResponseEntity<Void> loginResponse = template.postForEntity("/logins", autenticacaoDto, Void.class);
        authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("Deve criar um usuario com sucesso!")
    public void deveCriarUmUsuarioComSucesso() {
        UsuarioRequestDto dto = UserFixture.requestDto();
        logar(1L);

        ResponseEntity<UsuarioResponseDto> resposta = template.exchange(
                "/usuarios",
                HttpMethod.POST,
                new HttpEntity<>(dto, authHeaders),
                UsuarioResponseDto.class);

        Long id = resposta.getBody().id();
        Usuario usuarioSalvo = repository.findById(id).get();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(usuarioSalvo.getId());
        assertEquals(dto.email(), usuarioSalvo.getEmail());
        assertEquals(dto.nome(), usuarioSalvo.getNome());
        assertEquals(dto.dataDeNascimento(), usuarioSalvo.getDataDeNascimento());
        assertEquals(dto.cpf(), usuarioSalvo.getCpf());
    }

    @Test
    @DisplayName("Deve retornar dados de um usuario com sucesso!")
    public void deveRetornarDadosUmUsuarioComSucesso() {
        Long id = 2L;
        logar(id);
        ResponseEntity<UsuarioDadosResponse> resposta = template.exchange(
                "/usuarios/{id}",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                UsuarioDadosResponse.class,
                id);

        Usuario usuarioDoBanco = repository.findById(id).get();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(usuarioDoBanco.getNome(), resposta.getBody().nome());
        assertEquals(usuarioDoBanco.getEmail(), resposta.getBody().email());
        assertEquals(usuarioDoBanco.getDataDeNascimento(), resposta.getBody().dataDeNascimento());
        assertEquals(usuarioDoBanco.getCidade(), resposta.getBody().cidade());
        assertEquals(usuarioDoBanco.getEstado(), resposta.getBody().estado());
        assertEquals(usuarioDoBanco.getPais(), resposta.getBody().pais());
        assertEquals(usuarioDoBanco.getFoto(), resposta.getBody().foto());
    }

    @Test
    @DisplayName("Deve adicionar informações a um usuario com sucesso!")
    public void deveAdcionarInformacoesAUmUsuarioComSucesso() {
        UsuarioDadosComplementarRequest dto = UserFixture.requestDadosComplementares();
        Long id = 2L;
        logar(id);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}/dados-adicionais",
                HttpMethod.PUT,
                new HttpEntity<>(dto, authHeaders),
                Void.class,
                id);

        Usuario usuarioSalvo = repository.findById(id).get();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertEquals(dto.cidade(), usuarioSalvo.getCidade());
        assertEquals(dto.estado(), usuarioSalvo.getEstado());
        assertEquals(dto.pais(), usuarioSalvo.getPais());
        assertEquals(dto.foto(), usuarioSalvo.getFoto());

    }

    @Test
    @DisplayName("Deve alterar a sennha de um usuario com sucesso!")
    public void deveAlterarASenhaDeUmUsuarioComSucesso() {
        UsuarioAlterarSenhaRequest dto = new UsuarioAlterarSenhaRequest("Teste123&", "NovaSenha456$");
        Long id = 2L;
        logar(id);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}/alterar-senha",
                HttpMethod.PUT,
                new HttpEntity<>(dto, authHeaders),
                Void.class,
                id);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Deve alterar de um usuario Leitor com sucesso!")
    public void deveAlterarUmUsuarioLeitorComSucesso() {
        UsuarioAtualizadoLeitorRequest dto = new UsuarioAtualizadoLeitorRequest("Nome Alterado",
                "emailAlterado@gmail.com", null,
                "Cidade alterada", "Estado Alterado", "Pais alterado", null);

        Long id = 2L;
        logar(id);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(dto, authHeaders),
                Void.class,
                id);

        Usuario usuarioSalvo = repository.findById(id).get();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertEquals(dto.nome(), usuarioSalvo.getNome());
        assertEquals(dto.email(), usuarioSalvo.getEmail());
        assertEquals(dto.cidade(), usuarioSalvo.getCidade());
        assertEquals(dto.estado(), usuarioSalvo.getEstado());
        assertEquals(dto.pais(), usuarioSalvo.getPais());
        assertEquals(UsuarioProfile.LEITOR, usuarioSalvo.getUser().getProfile());
    }

    @Test
    @DisplayName("Deve alterar de um usuario administrador com sucesso!")
    public void deveAlterarUmUsuarioAdministradorComSucesso() {
        UsuarioAtualizadoAdministradorRequest dto = new UsuarioAtualizadoAdministradorRequest(null,
                "Cidade alterada", "Estado Alterado", "Pais alterado", null);

        Long id = 1L;
        logar(id);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}/administrador",
                HttpMethod.PUT,
                new HttpEntity<>(dto, authHeaders),
                Void.class,
                id);

        Usuario usuarioSalvo = repository.findById(id).get();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertEquals(dto.cidade(), usuarioSalvo.getCidade());
        assertEquals(dto.estado(), usuarioSalvo.getEstado());
        assertEquals(dto.pais(), usuarioSalvo.getPais());
        assertEquals(UsuarioProfile.ADMINISTRADOR, usuarioSalvo.getUser().getProfile());

    }

    @Test
    @DisplayName("Deve excluir um usuario leitor com sucesso!")
    public void deveExcluirUmUsuarioLeitorComSucesso() {
        Long id = 2L;
        logar(id);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders),
                Void.class,
                id);
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Usuario usuarioDoBanco = repository.findById(id).orElseThrow();
        assertThat(usuarioDoBanco.getExcluido()).isTrue();
        assertThat(usuarioDoBanco.getDataExclusao()).isNotNull();
        assertThat(usuarioDoBanco.getUser().getExcluido()).isTrue();
        assertThat(usuarioDoBanco.getUser().getDataExclusao()).isNotNull();

        logar(1L);
        ResponseEntity<String> getDepoisDelete = template.exchange(
                "/usuarios/{id}",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                String.class,
                id);
        assertThat(getDepoisDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        LoginRequestDto loginDto = new LoginRequestDto(usuarioDoBanco.getUser().getUser(), "Teste123&");
        ResponseEntity<Void> loginResponse = template.postForEntity("/logins", loginDto, Void.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Administrador deve conseguir excluir outro usuário (soft-delete)")
    public void adminDeveExcluirOutroUsuarioComSucesso() {
        Long idAdmin = 1L;
        Long idLeitor = 2L;

        logar(idAdmin);
        ResponseEntity<Void> resposta = template.exchange(
                "/usuarios/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders),
                Void.class,
                idLeitor);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Usuario usuarioDoBanco = repository.findById(idLeitor).orElseThrow();
        assertThat(usuarioDoBanco.getExcluido()).isTrue();
        assertThat(usuarioDoBanco.getDataExclusao()).isNotNull();
        assertThat(usuarioDoBanco.getUser().getExcluido()).isTrue();
        assertThat(usuarioDoBanco.getUser().getDataExclusao()).isNotNull();
    }
}