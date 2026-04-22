package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.usuario.UsuárioAtualizadoLeitorRequest;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioResponseDto;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @InjectMocks
    private UsuarioMapper mapper;


    @Test
    @DisplayName("Deve transformar um requestDto em entity")
    void toEntity() {
        UsuarioRequestDto dto = UserFixture.requestDto();

        Usuario resposta= mapper.toEntity(dto);

        assertNull(resposta.getId());
        assertEquals(dto.nome(),resposta.getNome());
        assertEquals(dto.cpf(),resposta.getCpf());
        assertEquals(dto.dataDeNascimento(),resposta.getDataDeNascimento());
        assertEquals(dto.email(),resposta.getEmail());
    }

    @Test
    @DisplayName("Deve converter um usuário em response.")
    void toResponse() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);

        UsuarioResponseDto resposta = mapper.toResponse(usuario);

        assertEquals(usuario.getId(),resposta.id());
        assertEquals(usuario.getNome(),resposta.nome());
        assertEquals(usuario.getEmail(),resposta.email());
        assertEquals(usuario.getDataDeNascimento(),resposta.dataDeNascimento());
        assertEquals(usuario.getCidade(),resposta.cidade());
        assertEquals(usuario.getEstado(),resposta.estado());
        assertEquals(usuario.getPais(),resposta.pais());
        assertEquals(usuario.getFoto(),resposta.foto());
    }

    @Test
    @DisplayName("Deve atualizar um usuário leitor")
    void toUpdate() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        UsuárioAtualizadoLeitorRequest atualizacoes = new UsuárioAtualizadoLeitorRequest(
                "nome atualizado","email atualizado", null,"cabralha",
                "Bahia",null,null);

        Usuario resposta = mapper.update(usuario,atualizacoes);

        assertEquals(usuario.getId(),resposta.getId());
        assertEquals(atualizacoes.nome(),resposta.getNome());
        assertEquals(atualizacoes.email(),resposta.getEmail());
        assertEquals(usuario.getDataDeNascimento(),resposta.getDataDeNascimento());
        assertEquals(atualizacoes.cidade(),resposta.getCidade());
        assertEquals(atualizacoes.estado(),resposta.getEstado());
    }
}