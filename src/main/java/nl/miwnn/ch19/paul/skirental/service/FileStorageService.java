package nl.miwnn.ch19.paul.skirental.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // Map waar de plaatjes komen te staan (buiten de target folder!)
    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Kon upload-map niet aanmaken");
        }
    }

    public String save(MultipartFile file) {
        try {
            // Maak een unieke naam: bijv. a1b2-ski.jpg
            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename));
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Kon bestand niet opslaan: " + e.getMessage());
        }
    }
}