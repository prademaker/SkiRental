package nl.miwnn.ch19.paul.skirental.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.model.Snowboard; // Vergeet deze import niet
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.SnowboardRepository; // Vergeet deze import niet
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class InitializeController {

    private final SkiRepository skiRepository;
    private final TypeRepository typeRepository;
    private final CopyRepository copyRepository;
    private final SnowboardRepository snowboardRepository; // Toegevoegd

    public InitializeController(SkiRepository skiRepository,
                                TypeRepository typeRepository,
                                CopyRepository copyRepository,
                                SnowboardRepository snowboardRepository) {
        this.skiRepository = skiRepository;
        this.typeRepository = typeRepository;
        this.copyRepository = copyRepository;
        this.snowboardRepository = snowboardRepository; // Toegevoegd
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (typeRepository.count() == 0) {
            seedTypes();
        }
        if (skiRepository.count() == 0) {
            seedSkis();
        }
        // Nieuwe check voor snowboards
        if (snowboardRepository.count() == 0) {
            seedSnowboards();
        }
    }

    private void seedTypes() {
        try {
            ClassPathResource resource = new ClassPathResource("seedData/types.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            CsvToBean<Type> csvToBean = new CsvToBeanBuilder<Type>(reader)
                    .withType(Type.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            typeRepository.saveAll(csvToBean.parse());
        } catch (IOException e) {
            throw new RuntimeException("Kon types.csv niet inlezen", e);
        }
    }

    private void seedSkis() {
        try {
            ClassPathResource resource = new ClassPathResource("seedData/skis.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            CsvToBean<Ski> csvToBean = new CsvToBeanBuilder<Ski>(reader)
                    .withType(Ski.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Ski> skis = csvToBean.parse();
            List<Type> types = typeRepository.findAll();
            for (int i = 0; i < skis.size(); i++) {
                Ski ski = skis.get(i);
                if (!types.isEmpty()) {
                    ski.getTypes().add(types.get(i % types.size()));
                }
                skiRepository.save(ski);
                copyRepository.save(new Copy(ski));
                copyRepository.save(new Copy(ski));
            }
        } catch (IOException e) {
            throw new RuntimeException("Kon skis.csv niet inlezen", e);
        }
    }

    // NIEUWE METHODE VOOR SNOWBOARDS
    private void seedSnowboards() {
        try {
            ClassPathResource resource = new ClassPathResource("seedData/snowboards.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            CsvToBean<Snowboard> csvToBean = new CsvToBeanBuilder<Snowboard>(reader)
                    .withType(Snowboard.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Snowboard> boards = csvToBean.parse();
            List<Type> types = typeRepository.findAll();

            for (int i = 0; i < boards.size(); i++) {
                Snowboard board = boards.get(i);
                // Verdeel de beschikbare types over de boards
                if (!types.isEmpty()) {
                    board.getTypes().add(types.get(i % types.size()));
                }
                snowboardRepository.save(board);
                // Voeg 2 exemplaren toe per board
                copyRepository.save(new Copy(board));
                copyRepository.save(new Copy(board));
            }
        } catch (IOException e) {
            throw new RuntimeException("Kon snowboards.csv niet inlezen", e);
        }
    }
}