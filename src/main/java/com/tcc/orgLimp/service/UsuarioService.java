package com.tcc.orgLimp.service;

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

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
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

    public Usuario salvar(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setId(request.getId());
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setPerfil(Usuario.Perfil.valueOf(request.getPerfil()));
        usuario.setAtivo(request.getAtivo());

        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        } else if (request.getId() != null) {
            Usuario existente = usuarioRepository.findById(request.getId()).orElse(null);
            if (existente != null) {
                usuario.setSenha(existente.getSenha());
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
