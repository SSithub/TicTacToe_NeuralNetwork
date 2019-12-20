package tictactoeai;

import java.util.ArrayList;

public class ANN {
    final double startinglr = .1;
    double lr;
    double[][] weightsIH;
    double[][] weightsHH;
    double[][] weightsHO;
    double[][] biasesIH;
    double[][] biasesHH;
    double[][] biasesHO;
    int numHiddens;
    ArrayList<double[][]> weights = new ArrayList<>();
    double[][] placeHolder = new double[1][1];
    ANN(int inputNodes, int hiddenNodes, int outputNodes){//Randomize weights and biases
        numHiddens = 1;
        weightsIH = new double[inputNodes][hiddenNodes];//inputs to hiddens
        weightsHO = new double[hiddenNodes][outputNodes];//hiddens to outputs
        biasesIH = new double[1][hiddenNodes];//to hiddens
        biasesHO = new double[1][outputNodes];//to outputs
        weightsIH = Matrix.randomize(weightsIH, 2, -1);
        weightsHO = Matrix.randomize(weightsHO, 2, -1);
        biasesIH = Matrix.randomize(biasesIH, 2, -1);
        biasesHO = Matrix.randomize(biasesHO, 2, -1);
        weights.add(weightsIH);
        weights.add(weightsHO);
        
    }
    ANN(int inputNodes, int hidden1Nodes, int hidden2Nodes, int outputNodes){//Randomize weights and biases
        numHiddens = 2;
        weightsIH = new double[inputNodes][hidden1Nodes];
        weightsHH = new double[hidden1Nodes][hidden2Nodes];
        weightsHO = new double[hidden2Nodes][outputNodes];
        biasesIH = new double[1][hidden1Nodes];
        biasesHH = new double[1][hidden2Nodes];
        biasesHO = new double[1][outputNodes];
        weightsIH = Matrix.randomize(weightsIH, 2, -1);
        weightsHH = Matrix.randomize(weightsHH, 2, -1);
        weightsHO = Matrix.randomize(weightsHO, 2, -1);
        biasesIH = Matrix.randomize(biasesIH, 2, -1);
        biasesHH = Matrix.randomize(biasesHH, 2, -1);
        biasesHO = Matrix.randomize(biasesHO, 2, -1);
    }
    double[][] feedforward(double[][] inputs){//multiply each input by respective weights and find the sum (dot product), then add biases, then feed each output into the activation function
        if(numHiddens == 1){
            double[][] hA = activation(Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH), false);
            return activation(Matrix.add(Matrix.dot(hA, weightsHO), biasesHO), false);//outputs
        }
        else if(numHiddens == 2){
            double[][] h1A = activation(Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH), false);//outputs of hidden layer 1
            double[][] h2A = activation(Matrix.add(Matrix.dot(h1A, weightsHH), biasesHH), false);//outputs of hidden layer 2
            return activation(Matrix.add(Matrix.dot(h2A, weightsHO), biasesHO), false);//outputs
        }
        return placeHolder;
    }
    void backpropagation(double[][] inputs, double[][] targets, double minPercentLR){
        if(numHiddens == 1){
            double[][] hZ = Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH);
            double[][] hA = activation(hZ, false);
            double[][] oZ = Matrix.add(Matrix.dot(hA, weightsHO), biasesHO);
            double[][] oA = activation(oZ, false);
            double[][] dC_doA = Matrix.subtract(oA, targets);
            double[][] doA_doZ = activation(oZ, true);
            double[][] doZ_doW = hA;
            //chain rule to adjust the weights and biases going to the output layer
            double[][] bGradientsO = Matrix.scale(lr*randomPercent(minPercentLR), Matrix.multiply(dC_doA, doA_doZ));
            double[][] wGradientsO = Matrix.scale(lr*randomPercent(minPercentLR), Matrix.dot(Matrix.transpose(doZ_doW), Matrix.multiply(dC_doA, doA_doZ)));
            //add the negative gradients
            biasesHO = Matrix.subtract(biasesHO, bGradientsO);
            weightsHO = Matrix.subtract(weightsHO, wGradientsO);
            //chain rule to adjust the weights and biases going to the hidden layer
            double[][] doZ_dhA = weightsHO;
            double[][] dhA_dhZ = activation(hZ, true);
            double[][] dhZ_dhW = inputs;
            double[][] bGradientsH = Matrix.scale(lr*randomPercent(minPercentLR), Matrix.multiply(dhA_dhZ ,Matrix.transpose(Matrix.dot(doZ_dhA, Matrix.multiply(dC_doA, doA_doZ)))));
            double[][] wGradientsH = Matrix.scale(lr*randomPercent(minPercentLR), Matrix.dot(Matrix.transpose(dhZ_dhW), Matrix.multiply(dhA_dhZ, Matrix.transpose(Matrix.dot(doZ_dhA, Matrix.multiply(dC_doA, doA_doZ))))));
            //add the negative gradients
            biasesIH = Matrix.subtract(biasesIH, bGradientsH);
            weightsIH = Matrix.subtract(weightsIH, wGradientsH);
        }
        else if(numHiddens == 2){
            double[][] h1Z = Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH);
            double[][] h1A = activation(h1Z, false);
            double[][] h2Z = Matrix.add(Matrix.dot(h1A, weightsHH), biasesHH);
            double[][] h2A = activation(h2Z, false);
            double[][] oZ = Matrix.add(Matrix.dot(h2A, weightsHO), biasesHO);
            double[][] oA = activation(oZ, false);
            double[][] dC_doA = Matrix.subtract(oA, targets);
            double[][] doA_doZ = activation(oZ, true);
            double[][] doZ_doW = h2A;
            //chain rule
            double[][] dC_doZ = Matrix.multiply(doA_doZ, dC_doA);
            double[][] dC_doW = Matrix.dot(Matrix.transpose(doZ_doW), dC_doZ);
            //adjust
            double[][] bGradientsHO = Matrix.scale(lr, dC_doZ);//derivatives of the biases in respect to Z is 1, so it is unnecessary
            double[][] wGradientsHO = Matrix.scale(lr, dC_doW);
            biasesHO = Matrix.subtract(biasesHO, bGradientsHO);
            weightsHO = Matrix.subtract(weightsHO, wGradientsHO);
            //chain rule
            double[][] doZ_dh2A = weightsHO;
            double[][] dh2A_dh2Z = activation(h2Z, true);
            double[][] dh2Z_dh2W = h1A;
            double[][] dC_dh2A = Matrix.dot(Matrix.transpose(doZ_dh2A), dC_doZ);
            double[][] dC_dh2Z = Matrix.multiply(dh2A_dh2Z, dC_dh2A);
            double[][] dC_dh2W = Matrix.dot(Matrix.transpose(dh2Z_dh2W), dC_dh2Z);
            //adjust
            double[][] bGradientsHH = Matrix.scale(lr, dC_dh2Z);
            double[][] wGradientsHH = Matrix.scale(lr, dC_dh2W);
            biasesHH = Matrix.subtract(biasesHH, bGradientsHH);
            weightsHH = Matrix.subtract(weightsHH, wGradientsHH);
            //chain rule
            
        }
    }
    double function(double x, boolean derivative){
        if(derivative == true)
            return 1 - Math.pow(function(x, false), 2);//tanh'(x)
        return 2*(1/(1 + Math.exp(-2*x)))-1;//tanh(x)
    }
    double[][] activation(double[][] matrix, boolean derivative){
        if(derivative == true){
            double[][] matrixResult = matrix;
            for(int i = 0; i < matrix.length; ++i)
                for(int j = 0; j < matrix[0].length; ++j)
                    matrixResult[i][j] = function(matrix[i][j], true);
            return matrixResult;
        }
        double[][] matrixResult = matrix;
        for(int i = 0; i < matrix.length; ++i)
            for(int j = 0; j < matrix[0].length; ++j)
                matrixResult[i][j] = function(matrix[i][j], false);
        return matrixResult;
    }
    double[][] cost(double[][] inputs, double[][] targets){
        double[][] outputsO = this.feedforward(inputs);
        double[][] cost = Matrix.scale(Matrix.power(Matrix.subtract(outputsO, targets), 2), .5);
        return cost;
    }
    double costTotal(double[][] inputs, double[][] targets){
        double[][] cost = cost(inputs, targets);
        double totalCost = 0;
        for(int i = 0; i < cost.length; ++i)
            for(int j = 0; j < cost[i].length; ++j){
                totalCost += cost[i][j];
            }
        return totalCost/cost[0].length;
    }
    double randomPercent(double minPercentLR){
        return minPercentLR + (Math.random() * (1 - minPercentLR));
    }
    double randomScale(double max){
        return 1 + Math.random()*max;
    }
    void mutate(){
        for(int i = 0; i < numHiddens+1; ++i){
            double[][] temp = new double[weights.get(i).length][weights.get(i)[0].length];
            temp = Matrix.randomize(temp, .0002, -.0001);
            temp = Matrix.add(weights.get(i), temp);
            weights.set(i, temp);
        }
        if(numHiddens == 1){
            weightsIH = weights.get(0);
            weightsHO = weights.get(1);
        }
    }
}