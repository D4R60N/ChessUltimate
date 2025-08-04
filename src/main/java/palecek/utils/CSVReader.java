package palecek.utils;

import palecek.entity.Space;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private static Space EMPTY = new Space(null, "X");
    public static Space[][] readCSVTo2DArray(String filePath) throws IOException {
        List<Space[]> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the first line to determine the width of the array
            line = br.readLine();
            Dimension dimension = getDimension(line);
            for (int x = 0; x < dimension.height; x++) {
                line = br.readLine();
                if (line == null) {
                    while (dataList.size() <= x) {
                        dataList.add(new Space[dimension.width]);
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
                            dataList.add(new Space[dimension.width]);
                        }
                        String[] value = values[y].trim().split(Separators.TYPE_SEPARATOR);
                        if (value.length > 2) {
                            throw new IllegalArgumentException("Invalid move: from position is not valid");
                        }
                        if (value.length < 2) {
                            dataList.get(x)[y] = new Space(null, value[0]);
                        } else {
                            dataList.get(x)[y] = new Space(value[0], value[1]);
                        }
                    } else {
                        if (dataList.size() <= x) {
                            dataList.add(new Space[dimension.width]);
                        }
                        dataList.get(x)[y] = EMPTY;
                    }
                }
            }
        }
        // Convert List<Space[]> to Space[][]
        return dataList.toArray(new Space[0][]);
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

