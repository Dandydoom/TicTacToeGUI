import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI version of the 2-player Tic Tac Toe game.
 * The game logic (board, isWin, isTie) comes from the console version.
 * The GUI is a 3 x 3 grid of TicTacToeTile buttons.
 *
 * @author Kirby Fortney
 */
public class TicTacToeFrame extends JFrame
{
    private static final int ROW = 3;
    private static final int COL = 3;
    private static final int MOVES_FOR_WIN = 5;
    private static final int MOVES_FOR_TIE = 7;

    // game data
    private String[][] board = new String[ROW][COL];
    private String player = "X";
    private int moveCnt = 0;

    // GUI parts
    private TicTacToeTile[][] tiles = new TicTacToeTile[ROW][COL];
    private JLabel statusLabel;
    private JButton quitButton;

    public TicTacToeFrame()
    {
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());

        // status label at the top
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(statusLabel, BorderLayout.NORTH);

        // board in the center
        JPanel boardPanel = new JPanel(new GridLayout(ROW, COL));
        TileListener tileListener = new TileListener(); // one listener for all tiles

        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                tiles[row][col] = new TicTacToeTile(row, col);
                tiles[row][col].setText(" ");
                tiles[row][col].setFont(new Font("SansSerif", Font.BOLD, 48));
                tiles[row][col].addActionListener(tileListener);
                boardPanel.add(tiles[row][col]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // quit button at the bottom
        JPanel buttonPanel = new JPanel();
        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent ae) -> quitGame());
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        clearBoard();

        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Single listener used by every tile on the board.
     */
    private class TileListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            TicTacToeTile clickedTile = (TicTacToeTile) ae.getSource();
            int row = clickedTile.getRow();
            int col = clickedTile.getCol();

            if (!isValidMove(row, col))
            {
                JOptionPane.showMessageDialog(TicTacToeFrame.this,
                        "That square is taken. Please pick an empty square.",
                        "Illegal Move", JOptionPane.WARNING_MESSAGE);
                return; // wait for a legal move
            }

            // record the move
            board[row][col] = player;
            clickedTile.setText(player);
            moveCnt++;

            // check for a win starting with move 5
            if (moveCnt >= MOVES_FOR_WIN && isWin(player))
            {
                JOptionPane.showMessageDialog(TicTacToeFrame.this, "Player " + player + " wins!");
                askPlayAgain();
                return;
            }

            // check for a tie starting with move 7
            if (moveCnt >= MOVES_FOR_TIE && isTie())
            {
                JOptionPane.showMessageDialog(TicTacToeFrame.this, "It's a Tie!");
                askPlayAgain();
                return;
            }

            // switch players
            if (player.equals("X"))
            {
                player = "O";
            }
            else
            {
                player = "X";
            }
            statusLabel.setText("Player " + player + "'s turn");
        }
    }

    private void askPlayAgain()
    {
        int answer = JOptionPane.showConfirmDialog(this, "Do you want to play again?",
                "Play Again?", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION)
        {
            clearBoard();
        }
        else
        {
            System.exit(0);
        }
    }

    private void quitGame()
    {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    // ---------- game logic from the console version ----------

    private void clearBoard()
    {
        // sets all the board elements to a space
        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                board[row][col] = " ";
                tiles[row][col].setText(" ");
            }
        }
        player = "X";
        moveCnt = 0;
        statusLabel.setText("Player X's turn");
    }

    private boolean isValidMove(int row, int col)
    {
        return board[row][col].equals(" ");
    }

    private boolean isWin(String player)
    {
        return isColWin(player) || isRowWin(player) || isDiagonalWin(player);
    }

    private boolean isColWin(String player)
    {
        for (int col = 0; col < COL; col++)
        {
            if (board[0][col].equals(player) &&
                board[1][col].equals(player) &&
                board[2][col].equals(player))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isRowWin(String player)
    {
        for (int row = 0; row < ROW; row++)
        {
            if (board[row][0].equals(player) &&
                board[row][1].equals(player) &&
                board[row][2].equals(player))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isDiagonalWin(String player)
    {
        if (board[0][0].equals(player) &&
            board[1][1].equals(player) &&
            board[2][2].equals(player))
        {
            return true;
        }
        if (board[0][2].equals(player) &&
            board[1][1].equals(player) &&
            board[2][0].equals(player))
        {
            return true;
        }
        return false;
    }

    /**
     * Checks for a tie (full board or no win still possible).
     * A tie happens when every row, col and diagonal has both an X and an O.
     */
    private boolean isTie()
    {
        // rows
        for (int row = 0; row < ROW; row++)
        {
            if (!lineHasBoth(board[row][0], board[row][1], board[row][2]))
            {
                return false;
            }
        }
        // cols
        for (int col = 0; col < COL; col++)
        {
            if (!lineHasBoth(board[0][col], board[1][col], board[2][col]))
            {
                return false;
            }
        }
        // diagonals
        if (!lineHasBoth(board[0][0], board[1][1], board[2][2]))
        {
            return false;
        }
        if (!lineHasBoth(board[0][2], board[1][1], board[2][0]))
        {
            return false;
        }
        return true; // every line is blocked
    }

    private boolean lineHasBoth(String a, String b, String c)
    {
        boolean hasX = a.equals("X") || b.equals("X") || c.equals("X");
        boolean hasO = a.equals("O") || b.equals("O") || c.equals("O");
        return hasX && hasO;
    }
}
