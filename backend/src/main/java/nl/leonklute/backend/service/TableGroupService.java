package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.TableGroup;
import nl.leonklute.backend.repository.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    @Value("classpath:tables.csv")
    Resource defaultTables;

    @Autowired
    public TableGroupService(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    public void loadDefaults(KeycloakUser keycloakUser) throws IOException {
        List<TableGroup> tableGroups = readTablesFromFile(defaultTables.getFile().getAbsolutePath());
        for (TableGroup tableGroup : tableGroups) {
            tableGroup.setKeycloakUser(keycloakUser);
            tableGroupRepository.save(tableGroup);
        }
    }
    private List<TableGroup> readTablesFromFile(String tablesFileName) throws IOException {
        List<TableGroup> tableGroups = new ArrayList<>();
        try (var tablesFile = new BufferedReader(new FileReader(tablesFileName))) {
            while (tablesFile.ready()) {
                String relationLine = tablesFile.readLine();
                var table = new TableGroup();
                String[] relationParts = relationLine.split(", *");
                table.setCapacity(Integer.parseInt(relationParts[0]));
                table.setTarget(Integer.parseInt(relationParts[1]));
                tableGroups.add(table);
            }
        }
        return tableGroups;
    }

    public List<TableGroup> getAllTablesByUser(KeycloakUser keycloakUser) {
        return tableGroupRepository.findAllByKeycloakUser(keycloakUser);
    }
}
