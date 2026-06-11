package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

public class UserFixture {
    private static final Long ID = 1L;
    private static final String NOME = "Nome SobreNome";
    private static final String EMAIL = "nome@gmail.com";
    private static final String CONFIRMAR_EMAIL = "nome@gmail.com";
    private static final String SENHA = "Teste123&";
    private static final String CONFIRMAR_SENHA = "Teste123&";
    private static final String CPF = "49618203000";
    private static final LocalDate DATA_DE_NASCIMENTO = LocalDate.of(2000, 12, 5);

    private static final Boolean CHECK_TERMO = true;
    private static final String CIDADE = "Valinhos";
    private static final String ESTADO = "São paulo";
    private static final String PAIS = "Brasil";
    private static final Boolean senhaTrocada = false;
    private static final byte[] FOTO = carregarImagem();

    public static UsuarioRequestDto requestDto() {
        return new UsuarioRequestDto(
                NOME, EMAIL, CONFIRMAR_EMAIL, SENHA, CONFIRMAR_SENHA, CPF,
                DATA_DE_NASCIMENTO, CHECK_TERMO);
    }

    public static UsuarioRequestDto requestDto(String senha) {
        return new UsuarioRequestDto(
                NOME, EMAIL, CONFIRMAR_EMAIL, senha, senha, CPF,
                DATA_DE_NASCIMENTO, CHECK_TERMO);
    }

    public static String requestDtoString() {
        return """
                {
                 "nome":"%s",
                 "email":"%s",
                 "confirmarEmail":"%s",
                 "senha":"%s",
                 "confirmarSenha":"%s",
                 "cpf":"%s",
                 "dataDeNascimento":"%s",
                 "checkTermo": %s
                }
                """.formatted(
                NOME, EMAIL, CONFIRMAR_EMAIL, SENHA, CONFIRMAR_SENHA, CPF,
                DATA_DE_NASCIMENTO.toString(), CHECK_TERMO);
    }

    public static UsuarioDadosComplementarRequest requestDadosComplementares() {
        return new UsuarioDadosComplementarRequest(
                CIDADE, ESTADO, PAIS);
    }

    public static String requestDadosComplementaresEmString() {
        return """
                {
                    "cidade":"%s",
                    "estado":"%s",
                    "pais":"%s"
                }
                """.formatted(CIDADE, ESTADO, PAIS);
    }

    public static User userEntity(UsuarioProfile profile) {
        String senhaHash = BCrypt.hashpw(SENHA, BCrypt.gensalt());
        boolean senhaTrocada = false;
        if (profile.equals(UsuarioProfile.LEITOR)) {
            senhaTrocada = true;
        }

        return new User(2L, EMAIL, senhaHash, senhaTrocada, profile, null);
    }

    public static Usuario entidadePrincipal(User user) {
        Usuario usuario = new Usuario();
        usuario.setId(ID);
        usuario.setNome(NOME);
        usuario.setEmail(EMAIL);
        usuario.setCpf(CPF);
        usuario.setDataDeNascimento(DATA_DE_NASCIMENTO);
        usuario.setAceitarTermos(CHECK_TERMO);
        usuario.setUser(user);
        return usuario;
    }

    public static Usuario entidadeCompleta() {
        User user = userEntity(UsuarioProfile.ADMINISTRADOR);
        return entidadeCompleta(user);
    }

    public static Usuario entidadeCompleta(User user) {
        Usuario usuario = entidadePrincipal(user);
        usuario.setCidade(CIDADE);
        usuario.setEstado(ESTADO);
        usuario.setPais(PAIS);
        usuario.setFoto(FOTO);
        return usuario;
    }

    public static UsuarioResponseDto response(Usuario user) {
        return new UsuarioResponseDto(
                user.getId(), user.getNome(), user.getEmail(), user.getCpf(),
                user.getUser().getProfile(), user.getDataDeNascimento(), user.getAceitarTermos(),
                user.getCidade(), user.getEstado(), user.getPais(), "/usuarios/foto");
    }

    public static Usuario atualizar(Usuario usuario, UsuarioAtualizadoAdministradorRequest atualizacoes) {
        usuario.setDataDeNascimento(atualizacoes.dataDeNascimento() != null ? atualizacoes.dataDeNascimento()
                : usuario.getDataDeNascimento());
        usuario.setCidade(atualizacoes != null ? atualizacoes.cidade() : usuario.getCidade());
        usuario.setEstado(atualizacoes.estado() != null ? atualizacoes.estado() : usuario.getEstado());
        usuario.setPais(atualizacoes.pais() != null ? atualizacoes.pais() : usuario.getPais());
        return usuario;
    }

    public static Usuario atualizar(Usuario usuario, UsuarioAtualizadoLeitorRequest atualizacoes) {
        usuario.setNome(atualizacoes.nome() != null ? atualizacoes.nome() : usuario.getNome());
        usuario.setEmail(atualizacoes.email() != null ? atualizacoes.email() : usuario.getEmail());
        usuario.setDataDeNascimento(atualizacoes.dataDeNascimento() != null ? atualizacoes.dataDeNascimento()
                : usuario.getDataDeNascimento());
        usuario.setCidade(atualizacoes.cidade() != null ? atualizacoes.cidade() : usuario.getCidade());
        usuario.setEstado(atualizacoes.estado() != null ? atualizacoes.estado() : usuario.getEstado());
        usuario.setPais(atualizacoes.pais() != null ? atualizacoes.pais() : usuario.getPais());
        return usuario;
    }

    private static byte[] carregarImagem() {
        try (InputStream is = LivroFixture.class
                .getClassLoader()
                .getResourceAsStream("usuario.jpg")) {

            if (is == null) {
                throw new RuntimeException("Arquivo usuario.jpg não encontrado");
            }

            return is.readAllBytes();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem", e);
        }
    }
}