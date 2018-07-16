package com.liemily.data;

import java.util.Arrays;

public class DataSet {
    private final String[] header;
    private final double[][] data;

    public DataSet(String[] header, double[][] data) {
        this.header = header;
        this.data = data;
    }

    public double get(final String col, final String row) throws NoSuchFieldException {
        final int colIdx = getIndex(col);
        final int rowIdx = getIndex(row);
        return data[colIdx][rowIdx];
    }

    public double[] get(String item) throws NoSuchFieldException {
        final int idx = getIndex(item);
        return Arrays.stream(data).mapToDouble(line -> line[idx]).toArray();
    }

    public void set(final String col, String row, double val) throws NoSuchFieldException {
        final int colIdx = getIndex(col);
        final int rowIdx = getIndex(row);
        data[colIdx][rowIdx] = val;
    }

    public String[] getHeader() {
        return header;
    }

    public int getIndex(final String item) throws NoSuchFieldException {
        final int idx = Arrays.asList(header).indexOf(item);
        if (idx < 0) {
            throw new NoSuchFieldException(item);
        } else {
            return idx;
        }
    }

    public double[][] getData() {
        return data;
    }
}
