# TicTacToe_Supervised
A beginning problem for deep learning.

This network was supposed to be deep, but it was slow in backpropagation so it was better to make it shallow.

The network took in 18 inputs for the board, 750 nodes in the hidden layer, and 1 output node.

Two hidden layers with 120 nodes each worked as well.

The Leaky RELU function was used in the hidden layer and the sigmoid function for the output layer.

UPDATE April 6, 2020: The old neural network class was replaced with a more updated version which made the old saved networks unusable and I thought the amount of nodes I used looked bizarre. The new network architecture of 18 input nodes, two hidden layers with 18 nodes each, and 1 output node. During training the network seemed to forget the obvious moves so I had to reduce the learning rate and the steepness of the loss function. I must have had success with the larger networks for their lack of sensitivity during backpropagation. The new network took about 1 million games to perfect playing as player X and O. After ever 100,000 games I would have it play against random moves to see if it has perfected the game.

The first approach was to feed forward 9 inputs and have 1 indicate a X, have -1 indicate a O, and have 0 indicate an empty spot.

The network never managed to learn this way so each spot was expanded into 2 numbers.

If there is a X, then the spot is labeled 1,0.

If there is a O, then the spot is labeled 0,1.

If it is empty, then the spot is labled 0,0.

This way, the network deals only with 1s and 0s which either allowed the network to learn or sped up its learning.

The network outputs a number between 0 and 1 for each spot, 0 being favorable for the O player and 1 being favorable for the X player.

The network chooses spots as the O player based on which spot gives the lowest output, and as the X player it will choose spots that output the highest output.

The labels for the inputs are given by the outcome of the game, so each board state in the game will have a label of either .01 (O won), .99 (X won), or .5 (Tie).
