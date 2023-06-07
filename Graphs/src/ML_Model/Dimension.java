package ML_Model;


public class Dimension {

    private final int row, col;

    public Dimension(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int[] getDimensions(){
        return new int[]{row, col};
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
