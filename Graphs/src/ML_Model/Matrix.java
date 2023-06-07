package ML_Model;


import java.util.ArrayList;
import java.util.Arrays;

public class Matrix {

    private final Double[][] matrix;


    private final Dimension dimensions; // return the row, col of matrix
    private final int row;
    private final int col;
    private int row_length; // the row length

    public Matrix(int row, int col){
        this.dimensions = new Dimension(row, col);
        this.row = dimensions.getRow();
        this.col = dimensions.getCol();
        matrix = new Double[this.row][this.col];
        row_length = 0;
    }


    public boolean addItem(int row, int col, Double item){
        if(row >= 0 && row < dimensions.getRow() || col >= 0 && col < dimensions.getCol()) {
            this.matrix[row][col] = item;
            return true;
        }
        return false;
    }

    public boolean addRow(int row, Double[] colSet){
        if(row >= 0 && row < dimensions.getRow()) {
            matrix[row] = colSet;
            return true;
        }
        return false;
    }

    public boolean addRow(int row, ArrayList<Double> colSet){
        if(row >= 0 && row < dimensions.getRow()) {
            for(int i = 0; i < colSet.size(); i++){
                matrix[row][i] = colSet.get(i);
            }
            return true;
        }
        return false;
    }


    public boolean addRow(Double[] colSet){
        if(row_length < this.row) {
            matrix[row_length] = colSet;
            row_length += 1;
            return true;
        }
        return false;
    }


    private void swap(int row, int col, int row2, int col2){
        Double temp = matrix[row][col];
        matrix[row][col] = matrix[row2][col2];
        matrix[row2][col2] = temp;
    }


    public Matrix transpose() {

        Matrix transposed_matrix = new Matrix(col, row);

        for(int c = 0; c < col; c++)
            for (int r = 0; r < row; r++)
                transposed_matrix.matrix[c][r] = this.matrix[r][c];

        return transposed_matrix;

    }



    // A scalar addition with the matrix
    public  <T> Matrix addition(T n){

        Matrix added = new Matrix(row, col);
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                added.matrix[r][c] =  this.matrix[r][c] + (Double) n;
            }
        }

        return added;
    }




    // A scalar subtraction with the matrix

    public <T> void subtraction(T n){
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                this.matrix[r][c] -=  (Double)n;
            }
        }
    }

    public <T> Matrix subtraction(Matrix n){
        if(n.getDimensions().getRow() == this.getDimensions().getRow() && this.getDimensions().getCol() == n.getDimensions().getCol()) {
            Matrix sub = new Matrix(row, col);
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    sub.matrix[r][c] = this.matrix[r][c] - n.matrix[r][c];
                }
            }
            return sub;
        }else throw new ArithmeticException("Bound does not match");
    }




    public double sum(){
        double total = 0D;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                total += this.matrix[r][c];
            }
        }
        return total;
    }



    // A scalar division with the matrix
    public void divide(int n){
        if (n > 0) {

            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    this.matrix[r][c] /=  n;
                }
            }

        }else throw new ArithmeticException("Can not divide by 0");
    }


    // A scalar multiplication with the matrix
    public <T> Matrix multiply(Double n){
        Matrix multiplied = new Matrix(row, col);
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                multiplied.matrix[r][c] =  this.matrix[r][c] * n;
            }
        }
        return multiplied;
    }

    public Matrix pow(int n){
        Matrix matrix = new Matrix(row, col);
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                matrix.matrix[r][c] =  Math.pow(this.matrix[r][c], n);
            }
        }
        return matrix;
    }


    // A vector multiplication with the matrix that has same number of cols as a linear vector
    public void multiply(Double[] vector){
        if(vector.length == col){
            for(int r = 0; r < row; r++){
                for(int c = 0; c < col; c++){
                    this.matrix[r][c] *= vector[c];
                }
            }
        }else throw new ArrayIndexOutOfBoundsException("Columns does not match with Vector");
    }

    public Matrix multiply(Matrix matrix){
        Matrix multiplied = new Matrix(row, col);
        if(matrix.row ==  this.row && matrix.col <= this.col){
            for(int r = 0; r < row; r++){
                for(int c = 0, j = 0; c < col; c++) {
                    multiplied.matrix[r][c] = this.matrix[r][c] * matrix.matrix[r][j];
                    if(j < matrix.col && matrix.col > 1) j++;
                }
            }
            return multiplied;
        }else throw new ArrayIndexOutOfBoundsException("Shape does not match with Matrix");
    }



    public Matrix dot(Matrix matrix){
        // match the shape to pass the rule of dot matrix
        /*
               1 2 3            2   3   4           2 [ 1 2 3 ]        2    4   6
               4 5 6    *                       =>  3 [ 4 5 6 ]   =>   12   15  18
               7 8 9                                4 [ 7 8 9 ]        28   32  36
                                                                       [42 51 60]

            The time complexity: O(n^3)
            The Auxilary Space: O(r * m.c)
         */

        if(this.col == matrix.row){
            Matrix dotted = new Matrix(this.row, matrix.col);
            for(int r = 0; r < this.row; r++){
                for(int c =  0; c < this.col; c++){
                    for(int m_c = 0; m_c < matrix.col; m_c++){
                        dotted.matrix[r][m_c] += matrix.matrix[c][m_c] * this.matrix[r][c];
                    }
                }
            }
            return dotted;
        }else {
            throw new ArrayIndexOutOfBoundsException("Shape does not match with Matrix");
        }

    }

    public Number getItem(int row, int col){
        return this.matrix[row][col];
    }

    public Matrix getRow(int row){

        if(row >= 0 && row < getDimensions().getRow()) {
            Matrix sliced = new Matrix(1, this.getDimensions().getCol());
            if (this.getDimensions().getCol() >= 0)
                System.arraycopy(matrix[row], 0, sliced.matrix[0], 0, this.getDimensions().getCol());
            return sliced;
        }
        return null;
    }

    public Double[] getCol(int column){
        if(column >= 0 && column < col){
            Double [] cols = new Double[row];
            for(int c = 0; c < row; c++){
                cols[c] = matrix[c][column];
            }
            return cols;
        }
        return null;
    }

    public Dimension getDimensions(){
        return this.dimensions;
    }


    @Override
    public String toString() {
        StringBuilder printer = new StringBuilder();
        for(int num_rows = 0; num_rows < row; num_rows++) {
            printer.append(Arrays.toString(this.matrix[num_rows])).append('\n');
        }
        return printer.toString();
    }


    public int getRow_length() {
        return row_length;
    }
}
