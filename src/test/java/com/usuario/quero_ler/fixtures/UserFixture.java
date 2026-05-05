package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class UserFixture {
    private static final Long ID = 1L;
    private static final String NOME = "Nome SobreNome";
    private static final String EMAIL = "nome@gmail.com";
    private static final String CONFIRMAR_EMAIL = "nome@gmail.com";
    private static final String SENHA = "Teste123&";
    private static final String CONFIRMAR_SENHA = "Teste123&";
    private static final String CPF = "49618203000";
    private static final LocalDate DATA_DE_NASCIMENTO = LocalDate.of(2000, 12, 05);
    private static final Boolean CHECK_TERMO = true;
    private static final String CIDADE = "Valinhos";
    private static final String ESTADO = "São paulo";
    private static final String PAIS = "Brasil";
    private static final byte[] FOTO = null;

    public static UsuarioRequestDto requestDto() {
        return new UsuarioRequestDto(
                NOME, EMAIL, CONFIRMAR_EMAIL, SENHA, CONFIRMAR_SENHA, CPF,
                DATA_DE_NASCIMENTO, CHECK_TERMO);
    }

    public static UsuarioDadosComplementarRequest requestDadosComplementares() {
        return new UsuarioDadosComplementarRequest(
                CIDADE, ESTADO, PAIS, FOTO);
    }

    public static User userEntity(UsuarioProfile profile) {
        String senhaHash = BCrypt.hashpw(SENHA, BCrypt.gensalt());
        User user = new User();
        user.setId(2L);
        user.setUser(EMAIL);
        user.setSenha(senhaHash);
        user.setProfile(profile);
        user.setExcluido(false);
        user.setDataExclusao(null);
        return user;
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
                user.getCidade(), user.getEstado(), user.getPais(), user.getFoto());
    }

    public static UsuarioDadosResponse responseDados(Usuario user) {
        return new UsuarioDadosResponse(
                user.getNome(), user.getEmail(), user.getDataDeNascimento(),
                user.getCidade(), user.getEstado(), user.getPais(), user.getFoto());
    }

    public static Usuario atualizar(Usuario usuario, UsuarioAtualizadoAdministradorRequest atualizacoes) {
        usuario.setDataDeNascimento(atualizacoes.dataDeNascimento() != null ? atualizacoes.dataDeNascimento()
                : usuario.getDataDeNascimento());
        usuario.setCidade(atualizacoes != null ? atualizacoes.cidade() : usuario.getCidade());
        usuario.setEstado(atualizacoes.estado() != null ? atualizacoes.estado() : usuario.getEstado());
        usuario.setPais(atualizacoes.pais() != null ? atualizacoes.pais() : usuario.getPais());
        usuario.setFoto(atualizacoes.foto() != null ? atualizacoes.foto() : usuario.getFoto());
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
        usuario.setFoto(atualizacoes.foto() != null ? atualizacoes.foto() : usuario.getFoto());
        return usuario;
    }

}