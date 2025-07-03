package com.example.biblio.service;

import com.example.biblio.model.Adherent;
import com.example.biblio.model.User;
import com.example.biblio.repository.AdherentRepository;
import com.example.biblio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CombinedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cherche dans User
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }

        // Sinon cherche dans Adherent par email (si tu considères email comme identifiant)
        Adherent adherent = adherentRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        return adherent;
    }
}
