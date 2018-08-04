package com.liemily.recommender;

import com.liemily.math.Matrix;

import java.util.Arrays;

public class DataSet {
    private final String[] header;
    private final Matrix data;

    public DataSet(final String[] header, final Matrix data) {
        this.header = header;
        this.data = data;
    }

    public double get(final String col, final String row) throws NoSuchFieldException {
        final int colIdx = getIndex(col);
        final int rowIdx = getIndex(row);
        return data.get(colIdx, rowIdx);
    }

    public double[] get(String item) throws NoSuchFieldException {
        final int idx = getIndex(item);
        return data.getCol(idx);
    }

    public void set(final int row, final int col, final double val) {
        data.set(row, col, val);
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

    public Matrix getData() {
        return data;
    }
}
