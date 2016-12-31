package path.finder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class Main extends ApplicationAdapter {
    public static float GridSize;
    float scrWidth, scrHeight;
    public static float fRows, fCols;
    Cell[][] Grid;
    Cell TempStartCell, TempTargetCell;
    PathFinder path;
    public static Random random;
    ShapeRenderer shape;

    @Override
    public void create() {
        GridSize = 10;
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        fRows = (int) (scrWidth / GridSize);
        fCols = (int) (scrHeight / GridSize);
        random = new Random();
        shape = new ShapeRenderer();
        Grid = CreateGrid(fRows, fCols, true);

        while (true) {
            TempStartCell = Grid[random.nextInt((int) fRows)][random.nextInt((int) fCols)];
            if (TempStartCell.bObstacle == false) {
                TempStartCell.bStartingCell = true;
                break;
            }
        }
        while (true) {
            TempTargetCell = Grid[random.nextInt((int) fRows)][random.nextInt((int) fCols)];
            if (TempTargetCell.bStartingCell == false && TempTargetCell.bObstacle == false) {
                TempTargetCell.bTarget = true;
                break;
            }
        }
        path = new PathFinder(TempTargetCell, TempStartCell, Grid, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < fRows; i++) {
            for (int j = 0; j < fCols; j++) {
                Grid[i][j].show();
            }
        }
        path.FindPath();
    }

    @Override
    public void dispose() {
        shape.dispose();
    }

    public Cell[][] CreateGrid(float fRows, float fCols, boolean bRandom) {
        Cell[][] Grid;
        Grid = new Cell[(int) fRows][(int) (fCols)];
        for (int x = 0; x < fRows; x++) {
            for (int y = 0; y < fCols; y++) {
                if (bRandom) {
                    if (random.nextInt(10) + 1 <= 3) {
                        Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, true, shape);
                    } else {
                        Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, false, shape);
                    }
                } else {
                    if (x % 2 == 0 && y % 2 == 0) {
                        Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, true, shape);
                    } else {
                        Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, false, shape);
                    }
                }
//                Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, false, shape);
            }
        }
        return Grid;
    }
}
