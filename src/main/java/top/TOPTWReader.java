package top;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import es.ull.esit.utilities.ExpositoUtilities;

/**
 * @class TOPTWReader
 * @brief Utility class for reading and parsing TOPTW problem instances from files.
 *
 * This class provides static methods to load Team Orienteering Problem with Time Windows (TOPTW)
 * instances from a specified file path. It parses the input file, initializes the problem data,
 * and computes the distance matrix required for further processing.
 */
public class TOPTWReader {
    
    /**
     * @brief Reads a TOPTW problem instance from a file.
     * @param filePath Path to the input file containing the problem definition.
     * @return A TOPTW object initialized with the parsed data.
     *
     * This method opens the specified file, reads the problem parameters and points of interest (POIs),
     * sets their coordinates, service times, scores, and time windows, and calculates the distance matrix.
     * If an error occurs during reading, the method prints the error and terminates the program.
     */
    public static TOPTW readProblem(String filePath) {
        TOPTW problem = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = reader.readLine();
            line = ExpositoUtilities.simplifyString(line);
            String[] parts = line.split(" ");
            problem = new TOPTW(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]));
            line = reader.readLine();
            line = null; parts = null;
            for (int i = 0; i < problem.getPOIs()+1; i++) {
                line = reader.readLine();
                line = ExpositoUtilities.simplifyString(line);
                parts = line.split(" ");
                problem.setX(i, Double.parseDouble(parts[1]));
                problem.setY(i, Double.parseDouble(parts[2]));
                problem.setServiceTime(i, Double.parseDouble(parts[3]));
                problem.setScore(i, Double.parseDouble(parts[4]));
                if(i==0) {
                    problem.setReadyTime(i, Double.parseDouble(parts[7]));
                    problem.setDueTime(i, Double.parseDouble(parts[8]));
                }
                else {
                    problem.setReadyTime(i, Double.parseDouble(parts[8]));
                    problem.setDueTime(i, Double.parseDouble(parts[9]));                    
                }
                line = null; parts = null;
            }
            problem.calculateDistanceMatrix();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(0);
        }
        problem.setMaxTimePerRoute(problem.getDueTime(0));
        return problem;
    }
    
}