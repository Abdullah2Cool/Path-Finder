package path.finder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor {
    float GridSize;
    float scrWidth, scrHeight;
    float fRows, fCols;
    Cell[][] Grid;
    ArrayList<Cell> OpenList = new ArrayList<>();
    ArrayList<Cell> ClosedList = new ArrayList<>();
    ArrayList<Cell> Path = new ArrayList<>();
    ShapeRenderer shape;
    Cell CurrentCell, TargetCell;
    int nHMoves, nVMoves;
    Random random;
    boolean bPathFound = false;
    boolean bSideWaysOnly = true;

    @Override
    public void create() {
        GridSize = 15;
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        fRows = (int) (scrWidth / GridSize);
        fCols = (int) (scrHeight / GridSize);
        Grid = new Cell[(int) fRows][(int) fCols];
        random = new Random();
        for (int x = 0; x < fRows; x++) {
            for (int y = 0; y < fCols; y++) {
                if (random.nextInt(10) + 1 <= 3) {
                    Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, true);
                } else {
                    Grid[x][y] = new Cell(x * GridSize, y * GridSize, GridSize, false);
                }
            }
        }
        shape = new ShapeRenderer();
        CurrentCell = Grid[random.nextInt((int) fRows)][random.nextInt((int) fCols)];
        nHMoves = (int) (CurrentCell.getX() / GridSize);
        nVMoves = (int) (CurrentCell.getY() / GridSize);

        int nRandX, nRandY;
        while (true) {
            nRandX = random.nextInt((int) fRows);
            nRandY = random.nextInt((int) fCols);
            if (Grid[nRandX][nRandY].bObstacle == false) {
                TargetCell = Grid[nRandX][nRandY];
                TargetCell.bTarget = true;
                break;
            }
        }

        int nHDist, nVDist;
        Cell TempCell;
        for (int x = 0; x < fRows; x++) {
            for (int y = 0; y < fCols; y++) {
                TempCell = Grid[x][y];
                if (TempCell.bObstacle == false) {
                    nHDist = (int) (Math.abs(TempCell.getX() - TargetCell.getX()) / GridSize);
                    nVDist = (int) (Math.abs(TempCell.getY() - TargetCell.getY()) / GridSize);
                    TempCell.nH = nHDist + nVDist;
                }
            }
        }
        ClosedList.add(CurrentCell);
        SetChildren(CurrentCell);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < fRows; i++) {
            for (int j = 0; j < fCols; j++) {
                Grid[i][j].show();
//                if (Grid[i][j].getX() == CurrentCell.getX() && Grid[i][j].getY() == CurrentCell.getY()) {
//                    System.out.println(Grid[i][j].nH);
//                }
//                if (Grid[i][j].getParentCell() == CurrentCell) {
//                    Grid[i][j].bObstacle = true;
////                    System.out.println(Grid[i][j].getX());
////                    System.out.println(Grid[i][j].getY());
////                    System.out.println();
//                } else {
//                    Grid[i][j].bObstacle = false;
//                }
//                if (Grid[i][j].getX() == CurrentCell.getX() && Grid[i][j].getY() == CurrentCell.getY()) {
//                    System.out.println("(" + i + " , " + j + ") : " + Grid[i][j].nG);
//                }
            }
        }
        Cell TempCell;
        if (bPathFound == false) {
            for (int i = 0; i < OpenList.size(); i++) {
                if (ClosedList.contains(OpenList.get(i))) {
                    OpenList.remove(OpenList.get(i));
                }
            }
            Collections.sort(OpenList, Cell.CellF);
            for (int i = 0; i < OpenList.size(); i++) {
                ClosedList.add(OpenList.get(i));
                TempCell = ClosedList.get(ClosedList.size() - 1);
                SetChildren(TempCell);
            }
        } else {
            Collections.sort(Path, Cell.CellF);
            Path.get(0).bHighlight = true;
            for (int i = 1; i < Path.size(); i++) {
                if (Path.get(i).bHighlight == true) {
                    Path.get(i).bHighlight = false;
                }
            }
            for (int x = 0; x < fRows; x++) {
                for (int y = 0; y < fCols; y++) {
                    if (Grid[x][y].bHighlight == true && Grid[x][y] != CurrentCell) {
                        Grid[x][y].getParentCell().bHighlight = true;
                    }
                }
            }
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(CurrentCell.getX(), CurrentCell.getY(), GridSize, GridSize);
        shape.end();

    }

    void SetChildren(Cell ParentCell) {
        int x = (int) (ParentCell.getX() / GridSize);
        int y = (int) (ParentCell.getY() / GridSize);
        Cell ChildCell;
        if (y + 1 < fCols) {
            ChildCell = Grid[x][y + 1];
            if (ChildCell.bObstacle == false) {
                if (ChildCell.ParentCell == null) {
                    ChildCell.setParentCell(ParentCell); // Top
                    ChildCell.nG = ParentCell.nG + 10;
                    OpenList.add(ChildCell);
                    ChildCell.bChecked = true;
                } else if (OpenList.contains(ChildCell)) {
                    if (ParentCell.nG + 10 < ChildCell.nG) {
                        ChildCell.setParentCell(ParentCell);
                        ChildCell.nG = ParentCell.nG + 10;
                    }
                }
            }
            if (ChildCell.bTarget) {
                System.out.println("Top : Found it");
                ParentCell.bHighlight = true;
                bPathFound = true;
                Path.add(ParentCell);
            }
        }
        if (x + 1 < fRows) {
            ChildCell = Grid[x + 1][y];
            if (ChildCell.bObstacle == false) {
                if (ChildCell.ParentCell == null) {
                    ChildCell.setParentCell(ParentCell); // Right
                    ChildCell.nG = ParentCell.nG + 10;
                    OpenList.add(ChildCell);
                    ChildCell.bChecked = true;
                } else if (OpenList.contains(ChildCell)) {
                    if (ParentCell.nG + 10 < ChildCell.nG) {
                        ChildCell.setParentCell(ParentCell);
                        ChildCell.nG = ParentCell.nG + 10;
                    }
                }
            }
            if (ChildCell.bTarget) {
                System.out.println("Right : Found it");
                ParentCell.bHighlight = true;
                bPathFound = true;
                Path.add(ParentCell);
            }
        }
        if (y - 1 >= 0) {
            ChildCell = Grid[x][y - 1];
            if (ChildCell.bObstacle == false) {
                if (ChildCell.ParentCell == null) {
                    ChildCell.setParentCell(ParentCell); // Bottom
                    ChildCell.nG = ParentCell.nG + 10;
                    OpenList.add(ChildCell);
                    ChildCell.bChecked = true;
                } else if (OpenList.contains(ChildCell)) {
                    if (ParentCell.nG + 10 < ChildCell.nG) {
                        ChildCell.setParentCell(ParentCell);
                        ChildCell.nG = ParentCell.nG + 10;
                    }
                }
            }
            if (ChildCell.bTarget) {
                System.out.println("Bottom : Found it");
                ParentCell.bHighlight = true;
                bPathFound = true;
                Path.add(ParentCell);
            }
        }
        if (x - 1 >= 0) {
            ChildCell = Grid[x - 1][y];
            if (ChildCell.bObstacle == false) {
                if (ChildCell.ParentCell == null) {
                    ChildCell.setParentCell(ParentCell); // Left
                    ChildCell.nG = ParentCell.nG + 10;
                    OpenList.add(ChildCell);
                    ChildCell.bChecked = true;
                } else if (OpenList.contains(ChildCell)) {
                    if (ParentCell.nG + 10 < ChildCell.nG) {
                        ChildCell.setParentCell(ParentCell);
                        ChildCell.nG = ParentCell.nG + 10;
                    }
                }
            }
            if (ChildCell.bTarget) {
                System.out.println("Left : Found it");
                ParentCell.bHighlight = true;
                bPathFound = true;
                Path.add(ParentCell);
            }
        }
        if (!bSideWaysOnly) {
            if (x + 1 < fRows && y + 1 < fCols) {
                ChildCell = Grid[x + 1][y + 1];
                if (ChildCell.bObstacle == false) {
                    if (ChildCell.ParentCell == null) {
                        ChildCell.setParentCell(ParentCell); // Top-Right
                        ChildCell.nG = ParentCell.nG + 14;
                        OpenList.add(ChildCell);
                        ChildCell.bChecked = true;
                    } else if (OpenList.contains(ChildCell)) {
                        if (ParentCell.nG + 14 < ChildCell.nG) {
                            ChildCell.setParentCell(ParentCell);
                            ChildCell.nG = ParentCell.nG + 14;
                        }
                    }
                }
                if (ChildCell.bTarget) {
                    System.out.println("Top-Right : Found it");
                    ParentCell.bHighlight = true;
                    bPathFound = true;
                    Path.add(ParentCell);
                }
            }
            if (x - 1 >= 0 && y + 1 < fCols) {
                ChildCell = Grid[x - 1][y + 1];
                if (ChildCell.bObstacle == false) {
                    if (ChildCell.ParentCell == null) {
                        ChildCell.setParentCell(ParentCell); // Top-Left
                        ChildCell.nG = ParentCell.nG + 14;
                        OpenList.add(ChildCell);
                        ChildCell.bChecked = true;
                    } else if (OpenList.contains(ChildCell)) {
                        if (ParentCell.nG + 14 < ChildCell.nG) {
                            ChildCell.setParentCell(ParentCell);
                            ChildCell.nG = ParentCell.nG + 14;
                        }
                    }
                }
                if (ChildCell.bTarget) {
                    System.out.println("Top-Left : Found it");
                    ParentCell.bHighlight = true;
                    bPathFound = true;
                    Path.add(ParentCell);
                }
            }
            if (x + 1 < fRows && y - 1 >= 0) {
                ChildCell = Grid[x + 1][y - 1];
                if (ChildCell.bObstacle == false) {
                    if (ChildCell.ParentCell == null) {
                        ChildCell.setParentCell(ParentCell); // Bottom-Right
                        ChildCell.nG = ParentCell.nG + 14;
                        OpenList.add(ChildCell);
                        ChildCell.bChecked = true;
                    } else if (OpenList.contains(ChildCell)) {
                        if (ParentCell.nG + 14 < ChildCell.nG) {
                            ChildCell.setParentCell(ParentCell);
                            ChildCell.nG = ParentCell.nG + 14;
                        }
                    }
                }
                if (ChildCell.bTarget) {
                    System.out.println("Bottom-Right : Found it");
                    ParentCell.bHighlight = true;
                    bPathFound = true;
                    Path.add(ParentCell);
                }
            }

            if (x - 1 >= 0 && y - 1 >= 0) {
                ChildCell = Grid[x - 1][y - 1];
                if (ChildCell.bObstacle == false) {
                    if (ChildCell.ParentCell == null) {
                        ChildCell.setParentCell(ParentCell); // Bottom-Left
                        ChildCell.nG = ParentCell.nG + 14;
                        OpenList.add(ChildCell);
                        ChildCell.bChecked = true;
                    } else if (OpenList.contains(ChildCell)) {
                        if (ParentCell.nG + 14 < ChildCell.nG) {
                            ChildCell.setParentCell(ParentCell);
                            ChildCell.nG = ParentCell.nG + 14;
                        }
                    }
                }
                if (ChildCell.bTarget) {
                    System.out.println("Bottom-Left : Found it");
                    ParentCell.bHighlight = true;
                    bPathFound = true;
                    Path.add(ParentCell);
                }
            }
        }
    }

    @Override
    public void dispose() {
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT && nHMoves + 1 < fRows) {
            CurrentCell = Grid[(int) ((CurrentCell.getX() / GridSize) + 1)][(int) ((CurrentCell.getY() / GridSize))];
            nHMoves++;
        }
        if (keycode == Input.Keys.LEFT && nHMoves - 1 >= 0) {
            CurrentCell = Grid[(int) ((CurrentCell.getX() / GridSize) - 1)][(int) ((CurrentCell.getY() / GridSize))];
            nHMoves--;
        }
        if (keycode == Input.Keys.UP && nVMoves + 1 < fCols) {
            CurrentCell = Grid[(int) ((CurrentCell.getX() / GridSize))][(int) ((CurrentCell.getY() / GridSize) + 1)];
            nVMoves++;
        }
        if (keycode == Input.Keys.DOWN && nVMoves - 1 >= 0) {
            CurrentCell = Grid[(int) ((CurrentCell.getX() / GridSize))][(int) ((CurrentCell.getY() / GridSize) - 1)];
            nVMoves--;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
