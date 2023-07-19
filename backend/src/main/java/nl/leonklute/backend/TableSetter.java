package nl.leonklute.backend;
import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.domain.Relation;
import nl.leonklute.backend.domain.TableGroup;
import nl.leonklute.backend.compute.Grouping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@Slf4j
@Component
public class TableSetter {
    private final Grouping grouping;

    @Autowired
    public TableSetter(Grouping grouping) {
        this.grouping = grouping;
    }

    static double[][] matchWithSelf(double[][] compatibility, int size) {
        for (int i = 0; i < size; i++) {
            compatibility[i][i] = 5.0;
        }
        return compatibility;
    }
    public int[] createTables(List<Person> people, List<Relation> relations, List<TableGroup> tables){

        Map<Person, Integer> personToIndex = new HashMap<>();
        IntStream.range(0, people.size())
                .forEach(i -> personToIndex.put(people.get(i), i));

        double[][] compatibility = new double[people.size()][people.size()];
        compatibility = matchWithSelf(compatibility, people.size());
        Function<double[][], Consumer<Relation>> addRelation = (double[][] c) -> (Relation r) -> {
            int index1 = personToIndex.get(r.getPerson1());
            int index2 = personToIndex.get(r.getPerson2());
            c[index1][index2] = r.getRelationStrength();
            c[index2][index1] = r.getRelationStrength();
        };
        relations.forEach(addRelation.apply(compatibility));
        int[][] tablesArray = tables.stream()
                .map(tableGroup -> new int[]{tableGroup.getTarget(), tableGroup.getCapacity()})
                .toArray(int[][]::new);
        int[] groups = grouping.group(tablesArray, people.size(), compatibility);
        log.debug("groups: " + Arrays.toString(groups));

        for (int i = 0; i < groups.length; i++) {
            log.debug("TableGroup " + groups[i] + " has " + people.get(i));

        }
        return groups;
    }
}
