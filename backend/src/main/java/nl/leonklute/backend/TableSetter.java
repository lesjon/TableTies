package nl.leonklute.backend;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.domain.Relation;
import nl.leonklute.backend.domain.Table;
import nl.leonklute.backend.service.GroupingService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class TableSetter {
    static double[][] matchWithSelf(double[][] compatibility, int size) {
        for (int i = 0; i < size; i++) {
            compatibility[i][i] = 5.0;
        }
        return compatibility;
    }
    public static int[] createTables(List<Person> people, List<Relation> relations, List<Table> tables){

        Map<Person, Integer> personToIndex = new HashMap<>();
        IntStream.range(0, people.size())
                .forEach(i -> personToIndex.put(people.get(i), i));

        double[][] compatibility = new double[people.size()][people.size()];
        compatibility = matchWithSelf(compatibility, people.size());
        Function<double[][], Consumer<Relation>> addRelation = (double[][] c) -> (Relation r) -> {
            int index1 = personToIndex.get(r.person1());
            int index2 = personToIndex.get(r.person2());
            c[index1][index2] = Float.parseFloat(r.relation());
            c[index2][index1] = Float.parseFloat(r.relation());
        };
        relations.forEach(addRelation.apply(compatibility));
        int[][] tablesArray = tables.stream()
                .map(table -> new int[]{table.target(), table.capacity()})
                .toArray(int[][]::new);
        int[] groups = GroupingService.group(tablesArray, people.size(), compatibility);
        System.out.println("groups: " + Arrays.toString(groups));

        for (int i = 0; i < groups.length; i++) {
            System.out.println("Table " + groups[i] + " has " + people.get(i));

        }
        return groups;
    }
}
