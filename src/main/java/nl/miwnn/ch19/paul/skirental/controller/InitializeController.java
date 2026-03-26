package nl.miwnn.ch19.paul.skirental.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * @author Paul Rademaker
 * Initialize the database when the application is started empty
 */
@Controller
public class InitializeController {

    private final SkiRepository skiRepository;
    private final TypeRepository typeRepository;
    private final CopyRepository copyRepository;

    public InitializeController(SkiRepository bookRepository, TypeRepository typeRepository, CopyRepository copyRepository) {
        this.skiRepository = bookRepository;
        this.typeRepository = typeRepository;
        this.copyRepository = copyRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
        public void seed() {
            if (typeRepository.count() == 0) {
                seedTypes();
            }
            if (skiRepository.count() == 0) {
                seedSkis();
            }
        }

    private void seedTypes() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("seedData/types.csv");
            Reader reader = new InputStreamReader(
                    resource.getInputStream());
            CsvToBean<Type> csvToBean =
                    new CsvToBeanBuilder<Type>(reader)
                            .withType(Type.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            typeRepository.saveAll(csvToBean.parse());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon types.csv niet inlezen", e);
            }
        }

    private void seedSkis() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("seedData/skis.csv");
            Reader reader = new InputStreamReader(
                    resource.getInputStream());
            CsvToBean<Ski> csvToBean =
                    new CsvToBeanBuilder<Ski>(reader)
                            .withType(Ski.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            List<Ski> skis = csvToBean.parse();
            List<Type> types = typeRepository.findAll();
            for (int i = 0; i < skis.size(); i++) {
                Ski ski = skis.get(i);
                ski.getTypes().add(types.get(i % types.size()));
                skiRepository.save(ski);
                copyRepository.save(new Copy(ski));
                copyRepository.save(new Copy(ski));
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon skis.csv niet inlezen", e);
        }
    }

    }



