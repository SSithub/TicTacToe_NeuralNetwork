# TicTacToe_Supervised
A beginning problem for deep learning.

This network was supposed to be deep, but it was slow in backpropagation so it was better to make it shallow.

The network took in 18 inputs for the board, 750 nodes in the hidden layer, and 1 output node.

Two hidden layers with 120 nodes each worked as well.

The Leaky RELU function was used in the hidden layer and the sigmoid function for the output layer.

The first approach was to feed forward 9 inputs and have 1 indicate a X, have -1 indicate a O, and have 0 indicate an empty spot.

The network never managed to learn this way so each spot was expanded into 2 numbers.

If there is a X, then the spot is labeled 1,0.

If there is a O, then the spot is labeled 0,1.

If it is empty, then the spot is labled 0,0.

This way, the network deals only with 1s and 0s which either allowed the network to learn or sped up its learning.

The network outputs a number between 0 and 1 for each spot, 0 being favorable for the O player and 1 being favorable for the X player.

The network chooses spots as the O player based on which spot gives the lowest output, and as the X player it will choose spots that output the highest output.

The labels for the inputs are given by the outcome of the game, so each board state in the game will have a label of either .01 (O won), .99 (X won), or .5 (Tie).
