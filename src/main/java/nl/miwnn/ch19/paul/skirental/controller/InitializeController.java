package nl.miwnn.ch19.paul.skirental.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.service.CopyService;
import nl.miwnn.ch19.paul.skirental.service.SkiService;
import nl.miwnn.ch19.paul.skirental.service.SnowboardService;
import nl.miwnn.ch19.paul.skirental.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(InitializeController.class);

    private final SkiService skiService;
    private final TypeService typeService;
    private final CopyService copyService;
    private final SnowboardService snowboardService;

    public InitializeController(SkiService skiService,
                                TypeService typeService,
                                CopyService copyService,
                                SnowboardService snowboardService) {
        this.skiService = skiService;
        this.typeService = typeService;
        this.copyService = copyService;
        this.snowboardService = snowboardService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (typeService.findAll().isEmpty()) {
            seedTypes();
        }
        if (skiService.getSkis(null).isEmpty()) {
            seedSkis();
        }
        if (snowboardService.findAll().isEmpty()) {
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

            List<Type> types = csvToBean.parse();
            for (Type type : types) {
                typeService.save(type);
            }
            log.info("Types geseederd vanuit CSV");
        } catch (IOException e) {
            log.error("Fout bij seeden van types: ", e);
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
            List<Type> types = typeService.findAll();

            for (int i = 0; i < skis.size(); i++) {
                Ski ski = skis.get(i);

                // Voeg type toe
                if (!types.isEmpty()) {
                    ski.getTypes().add(types.get(i % types.size()));
                }

                if (!skiService.isDuplicate(ski)) {
                    skiService.save(ski);
                    copyService.addSkiCopy(ski.getId());
                    copyService.addSkiCopy(ski.getId());
                }
            }
            log.info("Skis geseederd vanuit CSV");
        } catch (IOException e) {
            log.error("Fout bij seeden van skis: ", e);
        }
    }

    private void seedSnowboards() {
        try {
            ClassPathResource resource = new ClassPathResource("seedData/snowboards.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            CsvToBean<Snowboard> csvToBean = new CsvToBeanBuilder<Snowboard>(reader)
                    .withType(Snowboard.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Snowboard> boards = csvToBean.parse();
            List<Type> types = typeService.findAll();

            for (int i = 0; i < boards.size(); i++) {
                Snowboard board = boards.get(i);

                if (!types.isEmpty()) {
                    board.getTypes().add(types.get(i % types.size()));
                }

                if (!snowboardService.isDuplicate(board)) {
                    snowboardService.save(board);
                    copyService.addSnowboardCopy(board.getId());
                    copyService.addSnowboardCopy(board.getId());
                }
            }
            log.info("Snowboards geseederd vanuit CSV");
        } catch (IOException e) {
            log.error("Fout bij seeden van snowboards: ", e);
        }
    }
}