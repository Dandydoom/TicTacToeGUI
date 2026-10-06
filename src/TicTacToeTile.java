import javax.swing.JButton;

/**
 * A Tic Tac Toe specific JButton that remembers its row and col on the board.
 *
 * @author Kirby Fortney
 */
public class TicTacToeTile extends JButton
{
    private int row;
    private int col;

    public TicTacToeTile(int row, int col)
    {
        super();
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }
}
