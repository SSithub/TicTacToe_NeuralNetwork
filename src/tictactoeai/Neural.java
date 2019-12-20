package tictactoeai;
public class Neural {
    Matrix Matrix = new Matrix();
    double[][] weightsIH;
    double[][] weightsHO;
    double[][] biasesH;
    double[][] biasesO;
    double learningRate = .00001;
    double function(double x){
        return 2*(1/(1 + Math.exp(-2*x)))-1;// tanh(x)
    }
    double functionD(double x){
//        return 4*(Math.exp(-2*x)/(Math.pow(1+Math.exp(-2*x), 2)));// tanh'(x)
        return 1 - Math.pow(function(x), 2);// also tanh'(x)
    }
    double[][] activation(double[][] matrix){
        double[][] matrixResult = matrix;
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[0].length; j++)
                matrixResult[i][j] = function(matrix[i][j]);
        return matrixResult;
    }
    double[][] activationD(double[][] matrix){
        double[][] matrixResult = matrix;
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[0].length; j++)
                matrixResult[i][j] = functionD(matrix[i][j]);
        return matrixResult;
    }
    Neural(int inputNodes, int hiddenNodes, int outputNodes){
        //weights
        weightsIH = Matrix.create(hiddenNodes,inputNodes,0);
        weightsHO = Matrix.create(outputNodes,hiddenNodes,0);
        weightsIH = Matrix.randomize(weightsIH,2,-1);
        weightsHO = Matrix.randomize(weightsHO,2,-1);
        //biases
        biasesH = Matrix.create(hiddenNodes,1,0);
        biasesO = Matrix.create(outputNodes,1,0);  
        biasesH = Matrix.randomize(biasesH,2,-1);
        biasesO = Matrix.randomize(biasesO,2,-1);
    }
    double[][] feedforward(double[][] inputsArray){
        double [][] input = Matrix.transpose(inputsArray);
        //outputs of hidden nodes
        double[][] outputsH = Matrix.dot(weightsIH, input);
        outputsH = Matrix.add(outputsH, biasesH);
        double[][] outputsHActivated = activation(outputsH);
        
        //outputs of output nodes
        double[][] outputsO = Matrix.dot(weightsHO, outputsHActivated);
        outputsO = Matrix.add(outputsO, biasesO);
        double[][] outputsOActivated = activation(outputsO);
        
        return Matrix.transpose(outputsOActivated);
    }
    void train(double[][] inputsArray, double[][] targetsArray){
        //feedforward
        double[][] inputs = Matrix.transpose(inputsArray);//transpose array for use
        //outputs of hidden nodes
        double[][] outputsH = Matrix.dot(weightsIH, inputs);
        outputsH = Matrix.add(outputsH, biasesH);
        double[][] outputsHActivated = activation(outputsH);
        //inputs -> inputs * weights -> add biases -> outputs of current layer -> activation function = inputs of next layer
        
        //outputs of output nodes
        double[][] outputsO = Matrix.dot(weightsHO, outputsHActivated);
        outputsO = Matrix.add(outputsO, biasesO);
        double[][] outputsOActivated = activation(outputsO);
        double[][] outputsArray = Matrix.transpose(outputsOActivated);//for data use
        //end of feedfoward
        
        //backpropagation
        double[][] outputsTransposed = Matrix.transpose(outputsArray);
        double[][] targetsTransposed = Matrix.transpose(targetsArray);
        double[][] outputErrors = Matrix.subtract(targetsTransposed, outputsTransposed);//target - result = errors
        //calculate gradients
        //gradients of weights = element-wise(differentiated outputs * output errors * learning rate) * matrixmultiplication (outputs of previous layer)
        double[][] outputsODifferentiated = activationD(outputsO);
        double[][] outputOGradients = Matrix.multiply(outputsODifferentiated, outputErrors);
        outputOGradients = Matrix.scale(outputOGradients, learningRate);
        //calculate deltas of weights
        double[][] outputHODeltas = Matrix.dot(outputOGradients, Matrix.transpose(outputsH));
        //tune weights & biases
        weightsHO = Matrix.add(weightsHO, outputHODeltas);
        biasesO = Matrix.add(biasesO, outputOGradients);
        //turning each weight into a percentage of the sum of all of the weights in its row
        //weight1 / (weight1 + weight2 + ...)
        //weights between inputs and hiddens (unused in single hidden layer networks)
        double[][] weightsIHPercentage = new double[weightsIH.length][weightsIH[0].length];
        for(int i = 0; i < weightsIHPercentage.length; i++){
            for(int j = 0; j < weightsIHPercentage[0].length; j++){
                double weightsSum = 0;//sum of weights in row
                for(int k = 0; k < weightsIHPercentage[0].length; k++){
                    weightsSum += weightsIH[i][k];
                }
                weightsIHPercentage[i][j] = (weightsIH[i][j]) / weightsSum;
            }
        }
        //percentage of weights between hiddens and outputs (
        double[][] weightsHOPercentage = new double[weightsHO.length][weightsHO[0].length];
        for(int i = 0; i < weightsHOPercentage.length; i++){
            for(int j = 0; j < weightsHOPercentage[0].length; j++){
                double weightsSum = 0;//sum of weights in row
                for(int k = 0; k < weightsHOPercentage[0].length; k++){
                    weightsSum += weightsHO[i][k];
                }
                weightsHOPercentage[i][j] = (weightsHO[i][j]) / weightsSum;
            }
        }
        //hidden errors from output errors using weights between hidden and output layers
        double[][] hiddenErrors = Matrix.dot(Matrix.transpose(weightsHOPercentage), outputErrors);
        //gradients of hidden
        double[][] outputsHDifferentiated = activationD(outputsH);
        double[][] outputHGradients = Matrix.multiply(outputsHDifferentiated, hiddenErrors);
        outputHGradients = Matrix.scale(outputHGradients, learningRate);
        //deltas of hidden
        double[][] outputIHDeltas = Matrix.dot(outputHGradients, Matrix.transpose(inputs));
        //tune weights & biases
        weightsIH = Matrix.add(weightsIH, outputIHDeltas);
        biasesH = Matrix.add(biasesH, outputHGradients);
    }
}
