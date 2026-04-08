package nl.miwnn.ch19.paul.skirental.dto;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */
public class NewRentalUserDTO {

    private String username;
    private String confirmPassword;
    private String plainPassword;
    private String role;

    public NewRentalUserDTO() {}

    public String getUsername() { return  username; }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlainPassword() {
        return plainPassword;
    }
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
