package tictactoeai;
public class Matrix {
    static private void sizeException(double[][] matrix){
        for(double[] matrixRow : matrix) 
            if(matrixRow.length != matrix[0].length) 
                throw new IllegalArgumentException("Inconsistent Matrix Size");  
    }
    static private void multiplicationDimensionMismatch(double[][] matrixA, double[][] matrixB){
        if(matrixA[0].length != matrixB.length)//A columns must equal B rows
            throw new IllegalArgumentException("Matrices Dimension Mismatch");
    }
    static private void dimensionMismatch(double[][] matrixA, double[][] matrixB){
        if(matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length)
            throw new IllegalArgumentException("Matrices Dimension Mismatch");
    }
    static void print(double[][] matrix, String nameOfMatrix){
        System.out.println(nameOfMatrix + ": ");
        int rows = matrix.length;
        int columns = matrix[0].length;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++)
                System.out.print("[" + matrix[i][j] + "] ");
            System.out.println("");
        }
    }
    static void resetDouble(double[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrix[i][j] = 0;
    }
    static void resetInt(int[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrix[i][j] = 0;
    }
    static double[][] create(int rows, int columns, double valueToAllElements){
        double[][] matrixResult = new double[rows][columns];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++){
                matrixResult[i][j] = valueToAllElements;
            }
        return matrixResult;
    }
    static double[][] randomize(double[][] matrix, double range, double minimum){
        sizeException(matrix);
        double[][] matrixResult = matrix;
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = Math.random() * range + minimum;
        return matrixResult;
    }
    static double[][] transpose(double[][] matrix){
        sizeException(matrix);
        double[][] matrixResult = new double[matrix[0].length][matrix.length];
        int rows = matrix.length;
        int columns = matrix[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[j][i] = matrix[i][j];
        return matrixResult;
    }
    static double[][] scale(double[][] matrix, double factor){
        sizeException(matrix);
        double[][] matrixResult = new double[matrix.length][matrix[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = factor * matrix[i][j];
        return matrixResult;   
    }
    static double[][] scale(double factor, double[][] matrix){
        sizeException(matrix);
        double[][] matrixResult = new double[matrix.length][matrix[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = factor * matrix[i][j];
        return matrixResult;   
    }
    static double[][] add(double[][] matrixA, double[][] matrixB){
        sizeException(matrixA);
        sizeException(matrixB);
        dimensionMismatch(matrixA, matrixB);
        double[][] matrixResult = new double[matrixA.length][matrixA[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = matrixA[i][j] + matrixB[i][j];
        return matrixResult;
    }
    static double[][] subtract(double[][] matrixA, double[][] matrixB){
        sizeException(matrixA);
        sizeException(matrixB);
        dimensionMismatch(matrixA, matrixB);
        double[][] matrixResult = new double[matrixA.length][matrixA[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = matrixA[i][j] - matrixB[i][j];
        return matrixResult;
    }
    static double[][] multiply(double[][] matrixA, double[][] matrixB){
        sizeException(matrixA);
        sizeException(matrixB);
        dimensionMismatch(matrixA, matrixB);
        double[][] matrixResult = new double[matrixA.length][matrixA[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = matrixA[i][j] * matrixB[i][j];
        return matrixResult;
    }
    static double[][] dot(double[][] matrixA, double[][] matrixB){
        //make sure lengths of rows are the same for each matrix
        sizeException(matrixA);
        sizeException(matrixB);
        multiplicationDimensionMismatch(matrixA, matrixB);
        double[][] matrixResult = new double[matrixA.length][matrixB[0].length];//result matrix
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        //multiply A row elements by B column elements = 1 element in result matrix
        for(int i = 0; i < rows; i++)//A rows or B columns or Result rows
            for(int j = 0; j < columns; j++)//A rows or B columns or Result columns
                for(int k = 0; k < matrixA[0].length; k++)//A columns or B rows
                    matrixResult[i][j] += matrixA[i][k] * matrixB[k][j];
        return matrixResult;
    }
    static double[][] power(double[][] matrix, double power){
        double[][] matrixResult = new double[matrix.length][matrix[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = Math.pow(matrix[i][j], power);
        return matrixResult;
    }
    static double sum(double[][] matrix){
        double sum = 0;
        int rows = matrix.length;
        int columns = matrix[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                sum += matrix[i][j];
        return sum;
    }
}
