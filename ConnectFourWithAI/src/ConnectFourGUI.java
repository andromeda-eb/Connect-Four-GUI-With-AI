import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;

public class ConnectFourGUI extends JFrame
{
    private Container pane;
    private JButton [][] board;
    private boolean gameOver;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newGame;
    private int screenSize;
    private Color user;
    private Color ai;
    private Color empty;
    private int depthLimit;
    private int rows;
    private int columns;
    private int easy;
    private int medium;
    private int hard;
    private Font font;

    public ConnectFourGUI()
    {
        super();
        this.rows = 6;
        this.columns = 7;
        this.screenSize = 700;
        this.board = new JButton[this.rows][this.columns];
        this.gameOver = false;
        this.user = Color.RED;
        this.ai = Color.YELLOW;
        this.empty = Color.WHITE;
        this.depthLimit = 4;
        this.font = new Font("SANS-SERIF", Font.BOLD, 50);
        initPane();
        initializeMenuBar();
        initializeBoard(); 
    }

    private void initPane()
    {
        this.pane = getContentPane();
        this.pane.setLayout(new GridLayout(this.rows, this.columns));
        setTitle("Connect Four with AI");
        setSize(this.screenSize, this.screenSize);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void resetBoard()
    {
        this.gameOver = false;

        for(int y = 0; y < this.rows; ++y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                board[y][x].setBackground(Color.WHITE);
                board[y][x].setText("");
            }
        }
    }

    private boolean isEmptyCell(JButton b)
    {
        return b.getBackground().equals(this.empty);
    }

    private boolean isSameColor(JButton b, Color c)
    {
        return b.getBackground().equals(c);
    }

    //check if two cells are equal and not empty 
    private boolean equalsTwo(JButton b, JButton b1)
    {
        return isEmptyCell(b) == false && b.getBackground().equals(b1.getBackground());
    }

    private boolean equalsFour(JButton b, JButton b1, JButton b2, JButton b3)
    {
        return equalsTwo(b, b1) && equalsTwo(b, b2) && equalsTwo(b, b3);
    }

    // check if if there are four cells in a row that are either empty or have same color
    private boolean isPossibleFour(JButton b, JButton b1, JButton b2, JButton b3, Color c)
    {
        return (
            isEmptyCell(b) || isSameColor(b, c)) 
            && (isEmptyCell(b1) || isSameColor(b1, c)) 
            && (isEmptyCell(b2) || isSameColor(b2, c)) 
            && (isEmptyCell(b3) || isSameColor(b3, c)
        );
    }
    /* count all possible fours (winning streaks) that a player can still make
        horizontal, vertical and diagonals */
    private int findPossibleFours(Color color)
    {  
        int possibleFourCount = 0;

        for(int y = 0; y < this.rows; ++y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if( x <= this.columns-4 && isPossibleFour(board[y][x], board[y][x+1], board[y][x+2], board[y][x+3], color))
                {
                    possibleFourCount += 1;
                }
                if(y <= this.rows-4 && isPossibleFour(board[y][x], board[y+1][x], board[y+2][x], board[y+3][x], color) )
                {
                    possibleFourCount += 1;
                }
                if(y <= this.rows-4 && x <= this.columns-4 && isPossibleFour(board[y][x], board[y+1][x+1], board[y+2][x+2], board[y+3][x+3], color))
                {
                    possibleFourCount +=1;
                }
                if(y <= this.rows-4 && x <= this.columns-4 && isPossibleFour(board[y+3][x], board[y+2][x+1], board[y+1][x+2], board[y][x+3], color))
                {
                    possibleFourCount +=1;
                }
            }
        }
        return possibleFourCount;
    }

    // scan the grid to check if there is a winning streak.
    private boolean findEqualsFour(Color currentPlayer, boolean setWinningButtons)
    {  
        for(int y = 0; y < this.rows; ++y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if( x <= this.columns-4 && equalsFour(board[y][x], board[y][x+1], board[y][x+2], board[y][x+3]) && board[y][x].getBackground().equals(currentPlayer))
                {
                    if(setWinningButtons == true)
                        setWinningButtons(board[y][x], board[y][x+1], board[y][x+2], board[y][x+3]);
                    return true;
                }
                if(y <= this.rows-4 && equalsFour(board[y][x], board[y+1][x], board[y+2][x], board[y+3][x]) && board[y][x].getBackground().equals(currentPlayer) )
                {
                    if(setWinningButtons == true)
                        setWinningButtons(board[y][x], board[y+1][x], board[y+2][x], board[y+3][x]);
                    return true;
                }
                if(y <= this.rows-4 && x <= this.columns-4 && equalsFour(board[y][x], board[y+1][x+1], board[y+2][x+2], board[y+3][x+3]) && board[y][x].getBackground().equals(currentPlayer))
                {
                    if(setWinningButtons == true)
                        setWinningButtons(board[y][x], board[y+1][x+1], board[y+2][x+2], board[y+3][x+3]);
                    return true;
                }
                if(y <= this.rows-4 && x <= this.columns-4 && equalsFour(board[y+3][x], board[y+2][x+1], board[y+1][x+2], board[y][x+3]) && board[y+3][x].getBackground().equals(currentPlayer))
                {
                    if(setWinningButtons == true)
                        setWinningButtons(board[y+3][x], board[y+2][x+1], board[y+1][x+2], board[y][x+3]);
                    return true;
                }
            }
        }
        return false;
    }

    private void setWinningButtons(JButton b, JButton b1, JButton b2, JButton b3)
    {
        b.setText("x");
        b.setFont(this.font);
        b1.setText("x");
        b1.setFont(this.font);
        b2.setText("x");
        b2.setFont(this.font);
        b3.setText("x");
        b3.setFont(this.font);
    }

    // check if a selected cell is valid to make the next move
    private boolean isValidPosition(int y, int x)
    {
        if(y == this.rows -1 && isEmptyCell(board[y][x]))
            return true;
        else if (y >= 0 && isEmptyCell(board[y][x]) && (isEmptyCell(board[y+1][x]) == false))
            return true;

        return false;
    }

    private boolean isTie()
    {
        for(int y = this.rows-1; y >= 0; --y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if(isEmptyCell(board[y][x]))
                    return false;
            }
        }
        return true;
    }

    private void displayGameOverMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message);
    }

    private boolean isGameOver(Color currentPlayer)
    {
        if(findEqualsFour(currentPlayer, true) || isTie()){
            displayGameOverMessage("Game is over");
            this.gameOver = true;
        }
        
        return this.gameOver == true;
    }

    JButton createCell()
    {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                for(int y = rows-1; y >= 0; --y)
                {
                    for(int x = 0; x < columns; ++x)
                    {
                        if(e.getSource() == board[y][x] && isValidPosition(y,x) && gameOver == false)
                        {   
                            board[y][x].setBackground(user);
                            isGameOver(user);

                            if(gameOver == false)
                               getOpponentsMove();
                        }
                    }
                }
            } 
        });

        return button;
    }

    private void initializeBoard()
    {
        for(int i = 0; i < this.rows; ++i)
        {
            for(int j = 0; j < this.columns; ++j)
            {
                JButton button = createCell();
                board[i][j] = button;
                pane.add(button);
            }
        }
    }

    private void createMenuBarItems()
    {
        this.menu = new JMenu("File");
        this.newGame = new JMenuItem("New Game");

        this.newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
            
        });

        this.menu.add(newGame);
        this.menuBar.add(menu);
    }

    private void initializeMenuBar()
    {
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("New Game");
        createMenuBarItems();
        setJMenuBar(menuBar);
    }

    private void getOpponentsMove()
    {
        int bestMove [] = getBestMove();

        if(bestMove[0] != -1 && bestMove[1] != -1)
        {
            board[bestMove[0]][bestMove[1]].setBackground(ai);
            isGameOver(this.ai);
        }
    }

    private int[] getBestMove()
    {
        int bestMove[] = {-1,-1};
        int bestScore = Integer.MIN_VALUE;
        int score;

        for(int y  = this.rows-1; y >= 0; --y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if(isValidPosition(y,x))
                {   
                    board[y][x].setBackground(ai);
                    score = minimax(this.ai, depthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board[y][x].setBackground(this.empty);
                    if(score > bestScore)
                    {
                        bestScore = score;
                        bestMove[0] = y;
                        bestMove[1] = x;
                    }
                }
            }
        }
        return bestMove;
    }

    private int evaluate(Color currentPlayer, int depth)
    {
        if(findEqualsFour(currentPlayer, false))
        {
            if(currentPlayer.equals(this.ai))
                return Integer.MAX_VALUE -1;
            else
                return Integer.MIN_VALUE + 1;
        }
        else if(depth == 0)
        {
            int a = findPossibleFours(this.user);
            int b = findPossibleFours(this.ai);
            
            if(currentPlayer.equals(this.ai))
                return (b - a );

            return a - b;

        }

        return -1000;
    }

    private int minimax(Color currentPlayer, int depth, int alpha, int beta, boolean isMax)
    {
        int result = evaluate(currentPlayer, depth);

        if(result != -1000){
            return result;
        }

        if(isMax){
            return maximizeAlpha(depth, alpha, beta);
        }
        else{
        }
            return minimizeBeta(depth, alpha, beta);
    }

    private int maximizeAlpha(int depth, int alpha, int beta)
    {
        int bestScore = Integer.MIN_VALUE;
        int score;

        for(int y = this.rows-1; y >=0; --y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if(isValidPosition(y,x))
                {
                    board[y][x].setBackground(this.ai);
                    score = minimax(ai, depth-1, alpha, beta, false);
                    board[y][x].setBackground(empty);
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, score);

                    if(beta <= alpha)
                        break;
                }
            }
        }
        return bestScore;
    }

    private int minimizeBeta(int depth, int alpha, int beta)
    {
        int bestScore = Integer.MAX_VALUE;
        int score;

        for(int y = this.rows-1; y >=0; --y)
        {
            for(int x = 0; x < this.columns; ++x)
            {
                if(isValidPosition(y,x))
                {
                    board[y][x].setBackground(this.user);
                    score = minimax(user, depth-1, alpha, beta, true);
                    board[y][x].setBackground(this.empty);
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, score);

                    if(beta <= alpha)
                        break;
                }
            }
        }
        return bestScore;
    }
}