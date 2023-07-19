package nl.leonklute.backend.compute;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Grouping {
    public int[] group(int[][] tables, int numDimensions, double[][] data) {
        KMeans kmeans = new KMeans(tables.length, numDimensions);
        kmeans.setData(data);
        int[] assignments = kmeans.assignToClusters();
        for (int i = 0; i < 100; i++) {
            kmeans.updateCentroids(assignments);
            assignments = kmeans.assignToClusters();
        }
        return kmeans.assignToClustersWithSizes(Arrays.stream(tables).mapToInt(t -> t[1]).toArray());
    }
}

