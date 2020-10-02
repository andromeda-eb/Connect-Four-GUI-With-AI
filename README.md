# Connect-Four-With-AI (09/20 - 10/20)
GUI Implementation of connect four in java using <strong>minimax with alpha-beta pruning</strong>.<br>
The algorithm searches 4 moves ahead i.e. it has a depth of 4.

# Evaluation Function
Counts all the possible fours (winning streaks) that both players can make and subtracts
from one another depending on the player. In a 6x7 board, the possible fours are:

horizontal: 24
vertical: 21
diagonals: 24

So 69 possible fours altogether. If the first move of user is bottom left then number of possible fours for AI is 66.
AI cannot get 1 diagonal, 1 vertical and 1 horizontal winning streak from the bottom left cell.

# Compile and Run
javac Main.java ConnectFourGUI.java <br>
java Main.java

# Dependencies
Java
