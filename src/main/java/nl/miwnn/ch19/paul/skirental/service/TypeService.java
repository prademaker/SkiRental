package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> findAll() {
        return typeRepository.findAll();
    }

    public Optional<Type> findById(Long id) {
        return typeRepository.findById(id);
    }

    public void save(Type type) {
        typeRepository.save(type);
    }

    public void deleteById(Long id) {
        // Logica: Je zou hier kunnen checken of er nog ski's gekoppeld zijn
        // aan dit type voordat je het verwijdert.
        typeRepository.deleteById(id);
    }
}