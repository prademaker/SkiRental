package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */

@Service
public class RentalUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public RentalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));


    }
}
