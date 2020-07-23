# TicTacToe_Supervised
A beginning problem for deep learning.

### Network Architecture
18 input nodes, 18 nodes in layer 1, 9 nodes in layer 2, 9 nodes in layer 3, and 1 output node. All layers use the tanh activation function with Xavier weight initialization. The optimizer used is AMSGrad with a learning rate of .0001.

### Method
The first approach was to feed forward 9 inputs and have 1 indicate an X, have -1 indicate an O, and have 0 indicate an empty spot.

The network never managed to learn this way so each spot was expanded into 2 numbers.

If there is a X, then the spot is labeled 1, 0.

If there is a O, then the spot is labeled 0, 1.

If it is empty, then the spot is labled 0, 0.

This way, the network deals only with 1s and 0s which either allowed the network to learn or sped up its learning. Consequently, the input nodes of the network become 18 instead of 9.

The method that does the AI turn takes the current board and simulates an action by the AI in each open spot. The simulated boards are fed into the network and outputs a value that is bounded within the range of the output activation function, and the simulated board with the respective action that outputed the highest value is chosen as the action if the AI is playing for X or the lowest value is chosen if playing for O. 

### Training
The labels for the inputs are given by the outcome of the game, so each board state in the game will have a label of either 1 (X won), -1 (O won), or 0 (Tie). The network is trained once with each board state with the label after a game directly after the game ends and the labeled board states are not kept. These labels are not true labels, but after many games, outputs of the network will closely match the likelihood that a move will generally result in a win, loss, or tie. I did not know reinforcement learning at the time of this project so this method was the best way to solve tic-tac-toe that I thought of in a supervised fashion.

### Problems

This method tends to lead to the network forgetting previous optimal actions, but depending on the network hyperparameters and the exploration rate, the network will converge to a point where it will consistently make optimal moves with the occasional forgetting.

### Results

With the network architecture above, the network can tie any game as both players after around 300,000 games of self-play. It does not retain this ability for many games but can consistently avoid losing as the X player but it has a much harder time doing the same for the O player. 
