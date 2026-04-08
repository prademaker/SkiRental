package nl.miwnn.ch19.paul.skirental.mapper;

import nl.miwnn.ch19.paul.skirental.dto.NewRentalUserDTO;
import nl.miwnn.ch19.paul.skirental.model.RentalUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */
@Component
public class RentalUserMapper {

    public RentalUser toRentalUser(
            NewRentalUserDTO dto,
            PasswordEncoder passwordEncoder) {

        RentalUser user = new RentalUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPlainPassword()));
        user.setRole(dto.getRole());
        return user;
    }
}
