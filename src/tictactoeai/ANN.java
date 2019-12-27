package tictactoeai;
import java.util.ArrayList;
public class ANN {
    final double startinglr = .001;
    double lr;
    double[][] weightsIH;
    double[][] biasesIH;
    double[][] weightsH1H2;
    double[][] biasesH1H2;
    double[][] weightsH2H3;
    double[][] biasesH2H3;
    double[][] weightsHO;
    double[][] biasesHO;
    int numHiddens;
    ArrayList<double[][]> weights = new ArrayList<>();
    ANN(int inputNodes, int hidden1Nodes, int outputNodes){//Randomize weights and biases
        lr = startinglr;
        numHiddens = 1;
        weightsIH = new double[inputNodes][hidden1Nodes];//inputs to hiddens
        weightsHO = new double[hidden1Nodes][outputNodes];//hiddens to outputs
        biasesIH = new double[1][hidden1Nodes];//to hiddens
        biasesHO = new double[1][outputNodes];//to outputs
        weightsIH = Matrix.scale(Matrix.randomize(weightsIH, 2, -1), Math.sqrt(2.0/inputNodes));
        weightsHO = Matrix.scale(Matrix.randomize(weightsHO, 2, -1), Math.sqrt(2.0/hidden1Nodes));
        biasesIH = Matrix.randomize(biasesIH, 2, -1);
        biasesHO = Matrix.randomize(biasesHO, 2, -1);
        weights.add(weightsIH);
        weights.add(weightsHO);
    }
    ANN(int inputNodes, int hidden1Nodes, int hidden2Nodes, int outputNodes){//Randomize weights and biases
        lr = startinglr;
        numHiddens = 2;
        weightsIH = new double[inputNodes][hidden1Nodes];
        weightsH1H2 = new double[hidden1Nodes][hidden2Nodes];
        weightsHO = new double[hidden2Nodes][outputNodes];
        biasesIH = new double[1][hidden1Nodes];
        biasesH1H2 = new double[1][hidden2Nodes];
        biasesHO = new double[1][outputNodes];
        weightsIH = Matrix.scale(Matrix.randomize(weightsIH, 2, -1), Math.sqrt(2.0/inputNodes));
        weightsH1H2 = Matrix.scale(Matrix.randomize(weightsH1H2, 2, -1), Math.sqrt(2.0/hidden1Nodes));
        weightsHO = Matrix.scale(Matrix.randomize(weightsHO, 2, -1), Math.sqrt(2.0/hidden2Nodes));
        biasesIH = Matrix.randomize(biasesIH, 2, -1);
        biasesH1H2 = Matrix.randomize(biasesH1H2, 2, -1);
        biasesHO = Matrix.randomize(biasesHO, 2, -1);
        weights.add(weightsIH);
        weights.add(weightsH1H2);
        weights.add(weightsHO);
    }
    ANN(int inputNodes, int hidden1Nodes, int hidden2Nodes, int hidden3Nodes, int outputNodes){//Randomize weights and biases
        lr = startinglr;
        numHiddens = 3;
        weightsIH = new double[inputNodes][hidden1Nodes];
        weightsH1H2 = new double[hidden1Nodes][hidden2Nodes];
        weightsH2H3 = new double[hidden2Nodes][hidden3Nodes];
        weightsHO = new double[hidden3Nodes][outputNodes];
        biasesIH = new double[1][hidden1Nodes];
        biasesH1H2 = new double[1][hidden2Nodes];
        biasesH2H3 = new double[1][hidden3Nodes];
        biasesHO = new double[1][outputNodes];
        weightsIH = Matrix.scale(Matrix.randomize(weightsIH, 2, -1), Math.sqrt(2.0/inputNodes));
        weightsH1H2 = Matrix.scale(Matrix.randomize(weightsH1H2, 2, -1), Math.sqrt(2.0/hidden1Nodes));
        weightsH2H3 = Matrix.scale(Matrix.randomize(weightsH2H3, 2, -1), Math.sqrt(2.0/hidden2Nodes));
        weightsHO = Matrix.scale(Matrix.randomize(weightsHO, 2, -1), Math.sqrt(2.0/hidden3Nodes));
        biasesIH = Matrix.randomize(biasesIH, 2, -1);
        biasesH1H2 = Matrix.randomize(biasesH1H2, 2, -1);
        biasesH2H3 = Matrix.randomize(biasesH2H3, 2, -1);
        biasesHO = Matrix.randomize(biasesHO, 2, -1);
        weights.add(weightsIH);
        weights.add(weightsH1H2);
        weights.add(weightsH2H3);
        weights.add(weightsHO);
    }
    double[][] feedforward(double[][] inputs){//multiply each input by respective weights and find the sum (dot product), then add biases, then feed each output into the activation function
        if(numHiddens == 1){
            double[][] hA = leakyReluActivation(Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH), false);
            return tanhActivation(Matrix.add(Matrix.dot(hA, weightsHO), biasesHO), false);//outputs
        }
        else if(numHiddens == 2){
            double[][] h1A = leakyReluActivation(Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH), false);//outputs of hidden layer 1
            double[][] h2A = leakyReluActivation(Matrix.add(Matrix.dot(h1A, weightsH1H2), biasesH1H2), false);//outputs of hidden layer 2
            return tanhActivation(Matrix.add(Matrix.dot(h2A, weightsHO), biasesHO), false);//outputs
        }
        else if(numHiddens == 3){
            double[][] h1A = leakyReluActivation(Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH), false);//outputs of hidden layer 1
            double[][] h2A = leakyReluActivation(Matrix.add(Matrix.dot(h1A, weightsH1H2), biasesH1H2), false);//outputs of hidden layer 2
            double[][] h3A = leakyReluActivation(Matrix.add(Matrix.dot(h2A, weightsH2H3), biasesH2H3), false);//outputs of hidden layer 3
            return tanhActivation(Matrix.add(Matrix.dot(h3A, weightsHO), biasesHO), false);//outputs
        }
        return null;
    }
    void backpropagation(double[][] inputs, double[][] targets){
        if(numHiddens == 1){
            double[][] h1Z = Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH);
            double[][] h1A = leakyReluActivation(h1Z, false);
            double[][] oZ = Matrix.add(Matrix.dot(h1A, weightsHO), biasesHO);
            double[][] oA = tanhActivation(oZ, false);
            double[][] dC_doA = Matrix.subtract(oA, targets);
            double[][] doA_doZ = tanhActivation(oZ, true);
            double[][] doZ_doW = h1A;
            //chain rule to adjust the weights and biases going to the output layerwGradientsHO
            double[][] bGradientsHO = Matrix.scale(lr, Matrix.multiply(dC_doA, doA_doZ));
            double[][] wGradientsHO = Matrix.scale(lr, Matrix.dot(Matrix.transpose(doZ_doW), Matrix.multiply(dC_doA, doA_doZ)));
            //add the negative gradients
            biasesHO = Matrix.subtract(biasesHO, bGradientsHO);
            weightsHO = Matrix.subtract(weightsHO, wGradientsHO);
            //chain rule to adjust the weights and biases going to the hidden layer
            double[][] doZ_dhA = weightsHO;
            double[][] dhA_dhZ = leakyReluActivation(h1Z, true);
            double[][] dhZ_dhW = inputs;
            double[][] bGradientsIH = Matrix.scale(lr, Matrix.multiply(dhA_dhZ ,Matrix.transpose(Matrix.dot(doZ_dhA, Matrix.multiply(dC_doA, doA_doZ)))));
            double[][] wGradientsIH = Matrix.scale(lr, Matrix.dot(Matrix.transpose(dhZ_dhW), Matrix.multiply(dhA_dhZ, Matrix.transpose(Matrix.dot(doZ_dhA, Matrix.multiply(dC_doA, doA_doZ))))));
            //add the negative gradients
//            Matrix.print(bGradientsIH, "bGradientsIH");
//            Matrix.print(wGradientsIH, "wGradientsIH");
            biasesIH = Matrix.subtract(biasesIH, bGradientsIH);
            weightsIH = Matrix.subtract(weightsIH, wGradientsIH);
        }
        else if(numHiddens == 2){
            double[][] h1Z = Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH);
            double[][] h1A = leakyReluActivation(h1Z, false);
            double[][] h2Z = Matrix.add(Matrix.dot(h1A, weightsH1H2), biasesH1H2);
            double[][] h2A = leakyReluActivation(h2Z, false);
            double[][] oZ = Matrix.add(Matrix.dot(h2A, weightsHO), biasesHO);
            double[][] oA = tanhActivation(oZ, false);
            double[][] dC_doA = Matrix.subtract(oA, targets);
            double[][] doA_doZ = tanhActivation(oZ, true);
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
            double[][] dh2A_dh2Z = leakyReluActivation(h2Z, true);
            double[][] dh2Z_dh2W = h1A;
            double[][] dC_dh2A = Matrix.dot(dC_doZ, Matrix.transpose(doZ_dh2A));
            double[][] dC_dh2Z = Matrix.multiply(dh2A_dh2Z, dC_dh2A);
            double[][] dC_dh2W = Matrix.dot(Matrix.transpose(dh2Z_dh2W), dC_dh2Z);
            //adjust
            double[][] bGradientsHH = Matrix.scale(lr, dC_dh2Z);
            double[][] wGradientsHH = Matrix.scale(lr, dC_dh2W);
            biasesH1H2 = Matrix.subtract(biasesH1H2, bGradientsHH);
            weightsH1H2 = Matrix.subtract(weightsH1H2, wGradientsHH);
            //chain rule
            double[][] dh2Z_dh1A = weightsH1H2;
            double[][] dh1A_dh1Z = leakyReluActivation(h1Z, true);
            double[][] dh1Z_dh1W = inputs;
            double[][] dC_dh1A = Matrix.dot(dC_dh2Z, Matrix.transpose(dh2Z_dh1A));
            double[][] dC_dh1Z = Matrix.multiply(dh1A_dh1Z, dC_dh1A);
            double[][] dC_dh1W = Matrix.dot(Matrix.transpose(dh1Z_dh1W), dC_dh1Z);
            //adjust
            double[][] bGradientsIH = Matrix.scale(lr, dC_dh1Z);
            double[][] wGradientsIH = Matrix.scale(lr, dC_dh1W);
            biasesIH = Matrix.subtract(biasesIH, bGradientsIH);
            weightsIH = Matrix.subtract(weightsIH, wGradientsIH);
        }
        else if(numHiddens == 3){
            double[][] h1Z = Matrix.add(Matrix.dot(inputs, weightsIH), biasesIH);
            double[][] h1A = leakyReluActivation(h1Z, false);
            double[][] h2Z = Matrix.add(Matrix.dot(h1A, weightsH1H2), biasesH1H2);
            double[][] h2A = leakyReluActivation(h2Z, false);
            double[][] h3Z = Matrix.add(Matrix.dot(h2A, weightsH2H3), biasesH2H3);
            double[][] h3A = leakyReluActivation(h3Z, false);
            double[][] oZ = Matrix.add(Matrix.dot(h3A, weightsHO), biasesHO);
            double[][] oA = tanhActivation(oZ, false);
            double[][] dC_doA = Matrix.subtract(oA, targets);
            //chain rule
            double[][] doA_doZ = tanhActivation(oZ, true);
            double[][] doZ_doW = h3A;
            double[][] dC_doZ = Matrix.multiply(doA_doZ, dC_doA);
            double[][] dC_doW = Matrix.dot(Matrix.transpose(doZ_doW), dC_doZ);
            //adjust
            double[][] bGradientsHO = Matrix.scale(lr, dC_doZ);//derivatives of the biases in respect to Z is 1, so it is unnecessary
            double[][] wGradientsHO = Matrix.scale(lr, dC_doW);
            biasesHO = Matrix.subtract(biasesHO, bGradientsHO);
            weightsHO = Matrix.subtract(weightsHO, wGradientsHO);
            //chain rule
            double[][] doZ_dh3A = weightsHO;
            double[][] dh3A_dh3Z = leakyReluActivation(h3Z, true);
            double[][] dh3Z_dh3W = h2A;
            double[][] dC_dh3A = Matrix.dot(dC_doZ, Matrix.transpose(doZ_dh3A));
            double[][] dC_dh3Z = Matrix.multiply(dC_dh3A, dh3A_dh3Z);
            double[][] dC_dh3W = Matrix.dot(Matrix.transpose(dh3Z_dh3W), dC_dh3Z);
            //adjust
            double[][] bGradientsH2H3 = Matrix.scale(lr, dC_dh3Z);
            double[][] wGradientsH2H3 = Matrix.scale(lr, dC_dh3W);
            biasesH2H3 = Matrix.subtract(biasesH2H3, bGradientsH2H3);
            weightsH2H3 = Matrix.subtract(weightsH2H3, wGradientsH2H3);
            //chain rule
            double[][] dh3Z_dh2A = weightsH2H3;
            double[][] dh2A_dh2Z = leakyReluActivation(h2Z, true);
            double[][] dh2Z_dh2W = h1A;
            double[][] dC_dh2A = Matrix.dot(dC_dh3Z, Matrix.transpose(dh3Z_dh2A));
            double[][] dC_dh2Z = Matrix.multiply(dh2A_dh2Z, dC_dh2A);
            double[][] dC_dh2W = Matrix.dot(Matrix.transpose(dh2Z_dh2W), dC_dh2Z);
            //adjust
            double[][] bGradientsH1H2 = Matrix.scale(lr, dC_dh2Z);
            double[][] wGradientsH1H2 = Matrix.scale(lr, dC_dh2W);
            biasesH1H2 = Matrix.subtract(biasesH1H2, bGradientsH1H2);
            weightsH1H2 = Matrix.subtract(weightsH1H2, wGradientsH1H2);
            //chain rule
            double[][] dh2Z_dh1A = weightsH1H2;
            double[][] dh1A_dh1Z = leakyReluActivation(h1Z, true);
            double[][] dh1Z_dh1W = inputs;
            double[][] dC_dh1A = Matrix.dot(dC_dh2Z, Matrix.transpose(dh2Z_dh1A));
            double[][] dC_dh1Z = Matrix.multiply(dh1A_dh1Z, dC_dh1A);
            double[][] dC_dh1W = Matrix.dot(Matrix.transpose(dh1Z_dh1W), dC_dh1Z);
            //adjust
            double[][] bGradientsIH = Matrix.scale(lr, dC_dh1Z);
            double[][] wGradientsIH = Matrix.scale(lr, dC_dh1W);
            biasesIH = Matrix.subtract(biasesIH, bGradientsIH);
            weightsIH = Matrix.subtract(weightsIH, wGradientsIH);
        }
    }
    double leakyRelu(double x, boolean derivative){
        if(derivative == true){
            if(x < 0)
                return .001;
            else
                return 1;
        }
        else{
            if(x < 0)
                return .001*x;
            else
                return x;
        }
    }
    double[][] leakyReluActivation(double[][] matrix, boolean derivative){
        double[][] matrixResult = new double[matrix.length][matrix[0].length];
        int rows = matrixResult.length;
        int columns = matrixResult[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                matrixResult[i][j] = leakyRelu(matrix[i][j], derivative);
        return matrixResult;
    }
    double sigmoid(double x, boolean derivative){
        if(derivative == true)
            return sigmoid(x, false)*(1 - sigmoid(x, false));//sigmoid'(x)
        return 1/(1+Math.exp(-x));//sigmoid(x)
    }
    double[][] sigmoidActivation(double[][] matrix, boolean derivative){
            double[][] matrixResult = new double[matrix.length][matrix[0].length];
            int rows = matrix.length;
            int columns = matrix[0].length;
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    matrixResult[i][j] = sigmoid(matrix[i][j], derivative);
            return matrixResult;
    }
    double tanh(double x, boolean derivative){
        if(derivative == true)
            return 1 - (Math.pow(tanh(x, false), 2));//tanh'(x)
        return (2/(1 + Math.exp(-2*x)))-1;//tanh(x)
    }
    double[][] tanhActivation(double[][] matrix, boolean derivative){
            double[][] matrixResult = new double[matrix.length][matrix[0].length];
            int rows = matrix.length;
            int columns = matrix[0].length;
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    matrixResult[i][j] = tanh(matrix[i][j], derivative);
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
        int rows = cost.length;
        int columns = cost[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++){
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
        for(int i = 0; i < numHiddens+1; i++){
            double[][] temp = new double[weights.get(i).length][weights.get(i)[0].length];
            temp = Matrix.randomize(temp, .000002, -.000001);
            temp = Matrix.add(weights.get(i), temp);
            weights.set(i, temp);
        }
        if(numHiddens == 1){
            weightsIH = weights.get(0);
            weightsHO = weights.get(1);
        }
    }
}