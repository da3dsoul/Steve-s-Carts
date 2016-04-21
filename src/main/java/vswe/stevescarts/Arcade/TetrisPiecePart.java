package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class TetrisPiecePart
{
    private TetrisBlock block;
    private int offX;
    private int offY;

    public TetrisPiecePart(TetrisBlock block, int offX, int offY)
    {
        this.block = block;
        this.offX = offX;
        this.offY = offY;
    }

    public void render(ArcadeTetris game, GuiMinecart gui, int x, int y)
    {
        this.block.render(game, gui, x + this.offX, y + this.offY);
    }

    public void rotate(int offSet)
    {
        this.block.rotate();
        int temp = this.offX;
        this.offX = -this.offY + offSet;
        this.offY = temp;
    }

    public void placeInBoard(TetrisBlock[][] board, int x, int y)
    {
        board[x + this.offX][y + this.offY] = this.block;
    }

    public boolean canMoveTo(TetrisBlock[][] board, int x, int y)
    {
        return this.isValidAt(board, x + this.offX, y + this.offY);
    }

    public boolean isValidAt(TetrisBlock[][] board, int x, int y)
    {
        return x >= 0 && x < board.length && y < board[0].length ? (y < 0 ? true : board[x][y] == null) : false;
    }

    public boolean canRotate(TetrisBlock[][] board, int x, int y, int offSet)
    {
        return this.isValidAt(board, x - this.offY + offSet, y + this.offX);
    }

    public boolean canPlaceInBoard(int y)
    {
        return y + this.offY >= 0;
    }
}
