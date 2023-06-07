package ML_Model;

public class LinearRegression {


    private Matrix matrix;
    private int dataPoints;
    private double m, b, mse;
    private double learning_rate;
    private boolean calculateMSE;


    public LinearRegression(Matrix matrix){
        setMatrix(matrix);
        this.dataPoints = matrix.getDimensions().getRow();
        this.m = 0D;
        this.b = 0D;
        this.learning_rate = 0.01;
        this.setCalculateMSE(false);
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void fit(){

        // the x=> is the Derivative of function
        // the b => is the y-inter derivative

        Matrix x_vals = matrix.getRow(0);
        Matrix f_x  = matrix.getRow(1);
        Matrix f_hat_x; // m * x + b
        double d_dx, d_db;

        for(int i = 0; i < 1000; i++){

            // the mx+b slope
            f_hat_x = x_vals.multiply(m).addition(b);

            // the error rate of our calculations
            if(calculateMSE) {

                // to reduce the error of our dataset we take partial derivative of the cost function with respect to m and b
                // and we call this method Gradient Descent ∆f(m, b) = <∂/∂m, ∂/∂b> and the cost of the function must be updated
                mse = f_x.subtraction(f_hat_x).pow(2).sum() / dataPoints;  //  ƒ(x) = (∑(y - (m * x + b))^2)/n
            }



            // the partial derivative of x-axis and then sum difference of f(x) and predicted f'(x) values
            d_dx = ((x_vals.multiply(-1D)).multiply(f_x.subtraction(f_hat_x)).sum()) * 2 / dataPoints;

            // the partial derivative of y-intercept and then sum difference of f(x) and predicted f'(x) values
            d_db = -1 * ((f_x.subtraction(f_hat_x)).sum()) * 2 / dataPoints;


            // adjusting the variables with respect to f'(x) and partial derivatives
            m -= learning_rate * d_dx;
            b -= learning_rate * d_db;
        }


    }

    public double getM() {
        return Math.round(m);
    }

    public double getB() {
        return Math.round(b);
    }

    public double getMse() {
        return mse;
    }




    public void setCalculateMSE(boolean calculateMSE) {
        this.calculateMSE = calculateMSE;
    }

    public double predict(double x){
        return getM() * x + getB();
    }
}
