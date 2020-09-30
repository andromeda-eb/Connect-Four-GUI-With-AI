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

public class ConnectFourGUI extends JFrame
{
    private Container pane;
    private JButton [][] board;
    private boolean gameOver;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem easy;
    private JMenuItem medium;
    private JMenuItem hard;
    private JMenuItem newGame;
    private int size;
    private int screenSize;
    private Color user;
    private Color ai;
    private Color empty;
    private int winAmount;
    private int depthLimit;

    public ConnectFourGUI()
    {
        super();
        this.size = 7;
        this.screenSize = 700;
        this.board = new JButton[this.size][this.size];
        this.gameOver = false;
        this.user = Color.RED;
        this.ai = Color.YELLOW;
        this.empty = Color.WHITE;
        this.winAmount = 4;
        this.depthLimit = 3;
        initPane();
        initializeMenuBar();
        initializeBoard();
    }

    private void initPane()
    {
        this.pane = getContentPane();
        this.pane.setLayout(new GridLayout(this.size, this.size));
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

        for(int y = 0; y < size; ++y)
        {
            for(int x = 0; x < size; ++x)
            {
                board[y][x].setBackground(Color.WHITE);
            }
        }
    }

    private boolean isEmptyCell(JButton button)
    {
        return button.getBackground().equals(this.empty);
    }

    private boolean areEqual(JButton button, JButton button1)
    {
        if(isEmptyCell(button) == false && button.getBackground().equals(button1.getBackground()))
            return true;

       return false;
    }

    private boolean equalsFour(JButton button, JButton button1, JButton button2, JButton button3)
    {
        if(areEqual(button, button1) && areEqual(button, button2) && areEqual(button, button3))
            return true;

        return false;
    }

    private boolean isValidPosition(int y, int x)
    {
        if(y == this.size -1 && isEmptyCell(board[y][x]))
            return true;
        else if (y >= 0 && isEmptyCell(board[y][x]) && (isEmptyCell(board[y+1][x]) == false))
            return true;

        return false;
    }

    private boolean hasHorizontalWin()
    {
        for(int i = 0; i < this.size; ++i)
        {
            for(int j = 0; j <= this.size - this.winAmount; ++j)
            {
                if( equalsFour(board[i][j], board[i][j+1], board[i][j+2], board[i][j+3]) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasVerticalWin()
    {
        for(int i = 0; i <= ( this.size - this.winAmount ); ++i)
        {
            for(int j = 0; j < this.size; ++j)
            {
                if( equalsFour(board[i][j], board[i+1][j], board[i+2][j], board[i+3][j]) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasLeftDiagonalWin()
    {
        for(int i = 0; i <= (this.size - this.winAmount); ++i)
        {
            for(int j = 0; j <= (this.size - this.winAmount); ++j)
            {
                if( equalsFour(board[i][j], board[i+1][j+1], board[i+2][j+2], board[i+3][j+3]) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasRightDiagonalWin()
    {
        for(int i = 0; i <= (this.size - this.winAmount); ++i)
        {
            for(int j = 0; j <= (this.size - this.winAmount); ++j)
            {
                if( equalsFour(board[i+3][j], board[i+2][j+1], board[i+1][j+2], board[i][j+3]) )
                {                    
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasDiagonalWin()
    {
        return hasLeftDiagonalWin() || hasRightDiagonalWin();
    }

    private boolean isTie()
    {
        for(int y = this.size-1; y >= 0; --y)
        {
            for(int x = 0; x < this.size; ++x)
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

    private boolean isGameOver(String currentPlayer)
    {
        if(hasHorizontalWin() || hasVerticalWin() || hasDiagonalWin())
        {
            this.gameOver = true;
            displayGameOverMessage(currentPlayer + " wins");
        }
        else if (isTie())
        {
            this.gameOver = true;
            displayGameOverMessage("Tie");
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

                for(int y = size-1; y >= 0; --y)
                {
                    for(int x = 0; x < size; ++x)
                    {
                        if(e.getSource() == board[y][x] && isValidPosition(y,x) && gameOver == false)
                        {
                            board[y][x].setBackground(user);
                            isGameOver("Red");

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
        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                JButton button = createCell();
                board[i][j] = button;
                pane.add(button);
            }
        }
    }

    JMenuItem createDifficultyMenuItem(JMenuItem item, String itemName, int newDepthLimit)
    {
        item = new JMenuItem(itemName);

        item.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                depthLimit = newDepthLimit;
            }
        });

        return item;
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

        JMenuItem easyItem = createDifficultyMenuItem(this.easy, "Easy", 3);
        JMenuItem mediumItem = createDifficultyMenuItem(this.medium, "Medium", 5);
        JMenuItem hardItem = createDifficultyMenuItem(this.hard, "Hard", 8);

        this.menu.add(newGame);
        this.menu.add(easyItem);
        this.menu.add(mediumItem);
        this.menu.add(hardItem);
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

        if(bestMove[0]  != -1 && bestMove[1] != -1)
        {
            board[bestMove[0]][bestMove[1]].setBackground(ai);
            isGameOver("Yellow");
        }
    }

    private int[] getBestMove()
    {
        int bestMove[] = {-1, -1};
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;

        for(int y = this.size -1; y >= 0; --y)
        {
            for(int x = 0; x < this.size; ++x)
            {
                if(isValidPosition(y,x))
                {
                    board[y][x].setBackground(this.ai);
                    score = minimax(this.ai, this.depthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
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

    private int minimax(Color currentPlayer, int depth, int alpha, int beta, boolean isMax)
    {
        int result = evaluate(currentPlayer, depth);

        if(result != -2)
            return result;
        
        if(isMax)
            return maximizeAlpha(depth, alpha, beta);
        else
            return minimizeBeta(depth, alpha, beta);
    }

    private int maximizeAlpha(int depth, int alpha, int beta)
    {
        int bestScore = Integer.MIN_VALUE;
        int score;

        for(int y = this.size-1; y >= 0; --y)
        {
            for(int x = 0; x < this.size; ++x)
            {
                if(isValidPosition(y, x))
                {
                    board[y][x].setBackground(this.ai);
                    score = minimax(ai, depth-1, alpha, beta, false);
                    board[y][x].setBackground(this.empty);
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

        for(int y = this.size-1; y >=0; --y)
        {
            for(int x = 0; x < this.size; ++x)
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
    // 1 for ai win, -1 for user win, 0 for tie, -2 for more moves that are still left
    private int evaluate(Color currentPlayer, int depth)
    {
        if(hasHorizontalWin() || hasVerticalWin() || hasDiagonalWin())
        {
            if(currentPlayer.equals(ai))
                return 1;
            else if(currentPlayer.equals(user))
                return -1;
        }
        else if(depth == 0 || isTie())
            return 0;
        
        return -2;
    }
}