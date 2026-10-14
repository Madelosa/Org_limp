package com.tcc.orgLimp.service;

import com.tcc.orgLimp.dto.AtualizarPerfilSupervisorRequest;
import com.tcc.orgLimp.repository.TarefaRepository;
import com.tcc.orgLimp.dto.AlterarSenhaRequest;
import com.tcc.orgLimp.dto.UsuarioRequest;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TarefaRepository tarefaRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            TarefaRepository tarefaRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.tarefaRepository = tarefaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarSupervisores() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getPerfil() == Usuario.Perfil.supervisor)
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario salvar(UsuarioRequest request) {
        Usuario usuario;

        if (request.getId() == null) {
            usuario = new Usuario();
        } else {
            usuario = usuarioRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário não encontrado."));
        }

        Usuario usuarioComMesmoEmail = usuarioRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (usuarioComMesmoEmail != null
                && !usuarioComMesmoEmail.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado. Informe outro endereço de e-mail.");
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setPerfil(Usuario.Perfil.valueOf(request.getPerfil()));

        if (request.getAtivo() != null) {
            usuario.setAtivo(request.getAtivo());
        }

        if (request.getSenha() == null
                || request.getSenha().isBlank()) {
            throw new IllegalArgumentException(
                    "A senha é obrigatória para cadastrar ou editar um usuário.");
        }

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public void alterarSenha(
            String emailUsuarioLogado,
            AlterarSenhaRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalStateException(
                        "Não foi possível identificar sua conta."));

        if (!passwordEncoder.matches(
                request.getSenhaAtual(),
                usuario.getSenha())) {
            throw new IllegalArgumentException(
                    "A senha atual está incorreta.");
        }

        if (passwordEncoder.matches(
                request.getNovaSenha(),
                usuario.getSenha())) {
            throw new IllegalArgumentException(
                    "A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenha(
                passwordEncoder.encode(request.getNovaSenha()));

        usuarioRepository.save(usuario);
    }

    public void atualizarPerfilProprio(
            String emailUsuarioLogado,
            AtualizarPerfilSupervisorRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalStateException(
                        "Não foi possível identificar sua conta."));

        if (usuario.getPerfil() != Usuario.Perfil.supervisor
                && usuario.getPerfil() != Usuario.Perfil.gerente) {
            throw new IllegalStateException(
                    "Perfil não autorizado para esta operação.");
        }

        Usuario usuarioComMesmoEmail = usuarioRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (usuarioComMesmoEmail != null
                && !usuarioComMesmoEmail.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado. Informe outro endereço de e-mail.");
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id, String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário não encontrado."));

        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalStateException(
                        "Não foi possível identificar o usuário logado."));

        if (usuario.getId().equals(usuarioLogado.getId())) {
            throw new IllegalStateException(
                    "Você não pode excluir sua própria conta por esta página. Acesse seu perfil.");
        }

        validarExclusao(usuario);
        usuarioRepository.delete(usuario);
    }

    public void excluirPropriaConta(String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalStateException(
                        "Não foi possível identificar sua conta."));

        if (usuario.getPerfil() != Usuario.Perfil.gerente) {
            throw new IllegalStateException(
                    "Somente um gerente pode excluir a própria conta por esta página.");
        }

        validarExclusao(usuario);
        usuarioRepository.delete(usuario);
    }

    private void validarExclusao(Usuario usuario) {
        if (usuario.getPerfil() == Usuario.Perfil.supervisor
                && !tarefaRepository.findBySupervisorId(usuario.getId()).isEmpty()) {

            throw new IllegalStateException(
                    "Não é possível excluir este supervisor, pois existem tarefas vinculadas a ele.");
        }

        if (usuario.getPerfil() == Usuario.Perfil.gerente
                && !tarefaRepository.findByGerenteResponsavelId(usuario.getId()).isEmpty()) {

            throw new IllegalStateException(
                    "Não é possível excluir este gerente, pois existem tarefas sob sua responsabilidade.");
        }
    }
}
