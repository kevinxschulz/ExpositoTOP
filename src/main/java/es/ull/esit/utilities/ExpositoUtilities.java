package es.ull.esit.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ExpositoUtilities
 * @brief Utility class providing helper methods for formatting, file operations, and matrix manipulation.
 *
 * This class offers a collection of static utility methods for string formatting, reading and writing files,
 * matrix operations, type checking, and graph-related functions. It supports tasks such as formatting numbers and strings,
 * multiplying matrices, checking for cycles in graphs, and simplifying string content. The utilities are designed to
 * facilitate common operations required in data processing and algorithm implementation.
 */
public class ExpositoUtilities {

    public static final int DEFAULT_COLUMN_WIDTH = 10;
    public static final int ALIGNMENT_LEFT = 1;
    public static final int ALIGNMENT_RIGHT = 2;

    public static int getFirstAppearance(int[] vector, int element) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == element) {
                return i;
            }
        }
        return -1;
    }

    public static void printFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpositoUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String simplifyString(String string) {
        string = string.replace("\t", " ");
        for (int i = 0; i < 50; i++) {
            string = string.replace("  ", " ");
        }
        string = string.trim();
        return string;
    }

    public static double[][] multiplyMatrices(double a[][], double b[][]) {
        if (a.length == 0) {
            return new double[0][0];
        }
        if (a[0].length != b.length) {
            return null;
        }
        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        double ans[][] = new double[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }

    public static void writeTextToFile(String file, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }

    public static String getFormat(String string) {
        if (!ExpositoUtilities.isInteger(string)) {
            if (ExpositoUtilities.isDouble(string)) {
                double value = Double.parseDouble(string);
                string = ExpositoUtilities.getFormat(value);
            }
        }
        return string;
    }

    public static String getFormat(double value) {
        DecimalFormat decimalFormatter = new DecimalFormat("0.000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    public static String getFormat(double value, int zeros) {
        String format = "0.";
        for (int i = 0; i < zeros; i++) {
            format += "0";
        }
        DecimalFormat decimalFormatter = new DecimalFormat(format);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    public static String getFormat(String string, int width) {
        return ExpositoUtilities.getFormat(string, width, ExpositoUtilities.ALIGNMENT_RIGHT);
    }

    public static String getFormat(String string, int width, int alignment) {
        String format = "";
        if (alignment == ExpositoUtilities.ALIGNMENT_LEFT) {
            format = "%-" + width + "s";
        } else {
            format = "%" + 1 + "$" + width + "s";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String[] data = new String[]{string};
        return String.format(format, (Object[]) data);
    }

    public static String getFormat(ArrayList<String> strings, int width) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + width + "s";
        }
        String[] data = new String[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings.get(t));
        }
        return String.format(format, (Object[]) data);
    }

    public static String getFormat(ArrayList<Integer> strings) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + DEFAULT_COLUMN_WIDTH + "s";
        }
        Integer[] data = new Integer[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = strings.get(t);
        }
        return String.format(format, (Object[]) data);
    }

    public static String getFormat(String[] strings, int width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, width);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }
    
        public static String getFormat(String[][] matrixStrings, int width) {
        String result = "";
        for (int i = 0; i < matrixStrings.length; i++) {
            String[] strings = matrixStrings[i];
            int[] alignment = new int[strings.length];
            Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
            int[] widths = new int[strings.length];
            Arrays.fill(widths, width);
            result += ExpositoUtilities.getFormat(strings, widths, alignment);
            if (i < (matrixStrings.length - 1)) {
                result += "\n";
            }
        }
        return result;
    }

    public static String getFormat(String[] strings) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, ExpositoUtilities.DEFAULT_COLUMN_WIDTH);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }

    public static String getFormat(String[] strings, int[] width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        return ExpositoUtilities.getFormat(strings, width, alignment);
    }

    public static String getFormat(String[] strings, int[] width, int[] alignment) {
        String format = "";
        for (int i = 0; i < strings.length; i++) {
            if (alignment[i] == ExpositoUtilities.ALIGNMENT_LEFT) {
                format += "%" + (i + 1) + "$-" + width[i] + "s";
            } else {
                format += "%" + (i + 1) + "$" + width[i] + "s";
            }
        }
        String[] data = new String[strings.length];
        for (int t = 0; t < strings.length; t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings[t]);
        }
        return String.format(format, (Object[]) data);
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isAcyclic(int[][] distanceMatrix) {
        int numRealTasks = distanceMatrix.length - 2;
        int node = 1;
        boolean acyclic = true;
        while (acyclic && node <= numRealTasks) {
            if (ExpositoUtilities.thereIsPath(distanceMatrix, node)) {
                return false;
            }
            node++;
        }
        return true;
    }

    public static boolean thereIsPath(int[][] distanceMatrix, int node) {
        int n = distanceMatrix.length;
        boolean[] visited = new boolean[n];
        java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
        visited[node] = true;
        queue.add(node);

        while (!queue.isEmpty()) {
            int toCheck = queue.removeFirst();
            for (int i = 0; i < n; i++) {
                if (toCheck != i && distanceMatrix[toCheck][i] != Integer.MAX_VALUE) {
                    if (i == node) {
                        return true;
                    }
                    if (!visited[i]) {
                        visited[i] = true;
                        queue.add(i);
                    }
                }
            }
        }
        return false;
    }
}
