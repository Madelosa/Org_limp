package com.tcc.orgLimp.service;

import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String email, String senha) {
        return usuarioRepository.findByEmailAndAtivoTrue(email)
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()))
                .orElse(null);
    }
}
