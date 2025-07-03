package com.example.biblio.service;

import com.example.biblio.model.Adherent;
import com.example.biblio.repository.AdherentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdherentService implements UserDetailsService {

    @Autowired
    private AdherentRepository adherentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adherentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Adherent not found with email: " + email));
    }

    public Adherent findByEmail(String email) {
        return adherentRepository.findAdByEmail(email);
    }
}
