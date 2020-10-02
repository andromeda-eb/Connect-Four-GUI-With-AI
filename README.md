# Connect-Four-With-AI (09/20 - 10/20)
GUI Implementation of connect four in java using <strong>minimax with alpha-beta pruning</strong>.
The algorithm searches 4 moves ahead i.e. it has a depth of 4.

# Evaluation Function
Counts all the possible fours (winning streaks) that both players can make and subtracts
from one another. In a 6x7 board, the possible fours are: <br>

horizontal: 24 <br>
vertical: 21 <br>
diagonals: 24 <br>

There are 69 possible fours altogether. If the first move of user is bottom left then number of possible fours for AI is 66.
AI cannot get 1 diagonal, 1 vertical and 1 horizontal winning streak from the bottom left cell. So by default it will
try to acquire the middle cell on its first turn as that is the column that holds the most control over the board.

# Compile and Run
javac Main.java ConnectFourGUI.java <br>
java Main.java

# Dependencies
Java
