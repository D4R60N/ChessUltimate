package palecek.utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private static String EMPTY = "X";
    public static String[][] readCSVTo2DArray(String filePath) throws IOException {
        List<String[]> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the first line to determine the width of the array
            line = br.readLine();
            Dimension dimension = getDimension(line);
            for (int x = 0; x < dimension.height; x++) {
                line = br.readLine();
                if (line == null) {
                    while (dataList.size() <= x) {
                        dataList.add(new String[dimension.width]);
                        for (int y = 0; y < dimension.width; y++) {
                            dataList.get(x)[y] = EMPTY;
                        }
                    }
                    continue;
                }
                String[] values = line.split(",");
                for (int y = 0; y < dimension.width; y++) {
                    if (y < values.length) {
                        if (dataList.size() <= x) {
                            dataList.add(new String[dimension.width]);
                        }
                        dataList.get(x)[y] = values[y].trim();
                    } else {
                        if (dataList.size() <= x) {
                            dataList.add(new String[dimension.width]);
                        }
                        dataList.get(x)[y] = EMPTY;
                    }
                }
            }
        }
        // Convert List<String[]> to String[][]
        return dataList.toArray(new String[0][]);
    }

    private static Dimension getDimension(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Empty file");
        }
        String[] firstLineValues = line.split(",");
        int width = Integer.parseInt(firstLineValues[0].strip());
        int height = Integer.parseInt(firstLineValues[1].strip());
        Dimension dimension = new Dimension(width, height);
        if (dimension.width <= 0 || dimension.height <= 0) {
            throw new IllegalArgumentException("Invalid dimensions in CSV file: " + dimension);
        }
        return dimension;
    }
}

