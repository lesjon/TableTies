package nl.leonklute.backend.compute;

import java.util.Arrays;
import java.util.Random;

class KMeans {

    private final int numClusters;
    private final int numDimensions;
    private double[][] data;
    private double[][] centroids;
    KMeans(int numClusters, int numDimensions) {
        this.numClusters = numClusters;
        this.numDimensions = numDimensions;
        initCentroids();
    }

    // initialize the centroids at random points
    public void initCentroids() {
        centroids = new double[numClusters][numDimensions];
        Random rand = new Random();
        for (int i = 0; i < numClusters; i++) {
            for (int j = 0; j < numDimensions; j++) {
                centroids[i][j] = 10 * (rand.nextDouble() - .5);
            }
        }
    }

    // assign each data point to the nearest centroid
    public int[] assignToClusters() {
        int[] assignments = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            double minDist = Double.MAX_VALUE;
            for (int j = 0; j < numClusters; j++) {
                double dist = 0;
                for (int k = 0; k < numDimensions; k++) {
                    double diff = data[i][k] - centroids[j][k];
                    dist += diff * diff;
                }
                if (dist < minDist) {
                    minDist = dist;
                    assignments[i] = j;
                }
            }
        }
        return assignments;
    }


    // recompute the centroids based on the current cluster assignments
    public void updateCentroids(int[] assignments) {
        int[] counts = new int[numClusters];
        Random rand = new Random();

        for (int i = 0; i < numClusters; i++) {
            for (int j = 0; j < numDimensions; j++) {
                centroids[i][j] = 0;
            }
            counts[i] = 0;
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < numDimensions; j++) {
                centroids[assignments[i]][j] += data[i][j];
            }
            counts[assignments[i]]++;
        }

        for (int i = 0; i < numClusters; i++) {
            if (counts[i] == 0) {
                // If the cluster is empty, move the centroid to a random data point
                int randomIndex = rand.nextInt(data.length);
                System.arraycopy(data[randomIndex], 0, centroids[i], 0, numDimensions);
            } else {
                for (int j = 0; j < numDimensions; j++) {
                    centroids[i][j] /= counts[i];
                }
            }
        }
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public int[] assignToClustersWithSizes(int[] groupSizes) {

        // Calculate total of groupSizes
        int totalGroupSize = Arrays.stream(groupSizes).sum();

        // Check if totalGroupSize is greater than number of data points
        if (totalGroupSize > data.length) {
            double scale = (double) data.length / totalGroupSize;
            for (int i = 0; i < groupSizes.length; i++) {
                groupSizes[i] = (int) Math.floor(groupSizes[i] * scale);
            }
            totalGroupSize = Arrays.stream(groupSizes).sum();
            // If rounding caused totalGroupSize to be less than data.length, increment some group sizes
            for (int i = 0; totalGroupSize < data.length && i < groupSizes.length; i++) {
                groupSizes[i]++;
                totalGroupSize++;
            }
        }
        int[] assignments = new int[data.length];
        // Initialize assignments with a value indicating unassigned data points
        for (int i = 0; i < data.length; i++) {
            assignments[i] = -1;
        }

        // Create a copy of groupSizes to track remaining capacity of each cluster
        int[] remainingCapacity = Arrays.copyOf(groupSizes, groupSizes.length);

        boolean allGroupsFull;
        do {
            allGroupsFull = true;
            for (int j = 0; j < numClusters; j++) {
                if (remainingCapacity[j] > 0) {
                    allGroupsFull = false;
                    double minDist = Double.MAX_VALUE;
                    int minIndex = -1;
                    for (int i = 0; i < data.length; i++) {
                        // Skip data points that have already been assigned
                        if (assignments[i] != -1) {
                            continue;
                        }

                        double dist = 0;
                        for (int k = 0; k < numDimensions; k++) {
                            double diff = data[i][k] - centroids[j][k];
                            dist += diff * diff;
                        }
                        if (dist < minDist) {
                            minDist = dist;
                            minIndex = i;
                        }
                    }
                    // Assign the nearest unassigned data point to the current cluster
                    if (minIndex != -1) {
                        assignments[minIndex] = j;
                        remainingCapacity[j]--;
                    }
                }
            }
        } while (!allGroupsFull);

        return assignments;
    }

}
