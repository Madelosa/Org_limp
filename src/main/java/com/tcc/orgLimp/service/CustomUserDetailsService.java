package com.tcc.orgLimp.service;

import com.tcc.orgLimp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailAndAtivoTrue(username)
            .map(usuario -> User.withUsername(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles(usuario.getPerfil().name().toUpperCase())
                    .build())
            .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuário não encontrado ou inativo: " + username));
}
}
