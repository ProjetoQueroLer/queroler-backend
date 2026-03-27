package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CredenciaisInvalidasException;
import com.usuario.quero_ler.exceptions.especies.UsuarioComPerfilInvalidoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoAutenticadoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.service.LoginServiceI;
import com.usuario.quero_ler.utils.Senhas;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginServiceI {
    private User logado;
    private final UserRepository repository;

    @Transactional
    @Override
    public User criar(UsuarioRequestDto dto, UsuarioProfile profile) {
        User user = new User();
        String senha = Senhas.gerar(dto.senha());
        user.setUser(dto.email());
        user.setSenha(senha);
        user.setProfile(profile);
        user = repository.save(user);
        return user;
    }

    @Override
    public void login(LoginRequestDto dto) {
        User user = repository.findByUserIgnoreCase(dto.user()).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Usuario não cadastrado.")
        );

//        if (!user.getProfile().equals(dto.profile())) {
//            throw new UsuarioComPerfilInvalidoException("Perfil inválido");
//        }

        Boolean senhaValida = Senhas.validar(dto.senha(), user.getSenha());

        if (!senhaValida) {
            throw new CredenciaisInvalidasException("Senha inválida.");
        }
        logado = user;
    }

    @Override
    public User validarLogin() {
        if (logado == null) {
            throw new UsuarioNaoAutenticadoException("Usuario não logado!");
        }
        return logado;
    }

    @Override
    public Boolean validarLogin(User user) {
        if (!(logado != null && user.getId().equals(logado.getId()))) {
            throw new UsuarioNaoAutenticadoException("Usuário não logado. ");
        }
        return true;
    }
}