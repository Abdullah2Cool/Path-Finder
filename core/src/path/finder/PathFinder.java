package path.finder;

import java.util.ArrayList;
import java.util.Collections;

import static path.finder.Main.GridSize;
import static path.finder.Main.fCols;
import static path.finder.Main.fRows;

/**
 * Created by hafiz on 12/27/2016.
 */

public class PathFinder {
    private Cell TargetCell, StartCell;
    private Cell[][] Grid;
    private ArrayList<Cell> OpenList = new ArrayList<Cell>();
    private ArrayList<Cell> ClosedList = new ArrayList<Cell>();
    private ArrayList<Cell> Path = new ArrayList<Cell>();
    private boolean bPathFound = false;
    private boolean bDiagonals;
    private float CalPerFrame = 500 / GridSize;
    private boolean bRunOnce = true;
    private int nPerpendicular, nDiagonal;


    public PathFinder(Cell TargetCell, Cell StartCell, Cell[][] Grid, boolean bDiagonals) {
        this.TargetCell = TargetCell;
        this.StartCell = StartCell;
        this.Grid = Grid;
        this.bDiagonals = bDiagonals;
        Calc_Heuristic();
        OpenList.add(StartCell);
        nPerpendicular = 10;
        nDiagonal = 10;
        CalPerFrame = 10;
    }

    public void FindPath() {
        if (!bPathFound) {
            for (int i = 0; i < CalPerFrame; i++) {
                Cell TempCell;
                Collections.sort(OpenList, new DistanceComparator());
                if (OpenList.size() > 0) {
                    TempCell = OpenList.get(0);
                    ClosedList.add(TempCell);
                    OpenList.remove(TempCell);
                    SetChildren(ClosedList.get(ClosedList.size() - 1));
                }
            }
        } else {
            if (bRunOnce) {
                HighlightPath();
            }
        }
    }

    public void HighlightPath() {
        Cell TempCell;
        for (int i = 0; i < 10; i++) {
            TempCell = Path.get(Path.size() - 1);
            if (TempCell != StartCell) {
                TempCell.bHighlight = true;
                TempCell.getParentCell().bHighlight = true;
                Path.add(TempCell.getParentCell());
            } else {
                bRunOnce = false;
                break;
            }
        }
    }


    public void Calc_Heuristic() {
        int nHDist, nVDist;
        Cell TempCell;
        for (int x = 0; x < fRows; x++) {
            for (int y = 0; y < fCols; y++) {
                TempCell = Grid[x][y];
                if (TempCell.bObstacle == false) {
//                    nHDist = (int) (Math.abs(TempCell.getX() - TargetCell.getX()) / GridSize);
//                    nVDist = (int) (Math.abs(TempCell.getY() - TargetCell.getY()) / GridSize);
//                    TempCell.nH = nHDist + nVDist;
                    TempCell.nH = (int) Math.hypot(TempCell.getX() - TargetCell.getX(), TempCell.getY() - TargetCell.getY());
                }
            }
        }
    }


    public void SetChildren(Cell ParentCell) {
        int x = (int) (ParentCell.getX() / GridSize);
        int y = (int) (ParentCell.getY() / GridSize);
        ClosedList.add(ParentCell);
        OpenList.remove(ParentCell);
        Cell[] ChildCells = new Cell[8];
        for (int i = 0; i < ChildCells.length; i++) {
            ChildCells[i] = null;
        }
        if (y + 1 < fCols) {
            ChildCells[0] = Grid[x][y + 1]; // top
        }
        if (x + 1 < fRows) {
            ChildCells[1] = Grid[x + 1][y]; // right
        }
        if (y - 1 >= 0) {
            ChildCells[2] = Grid[x][y - 1]; // bottom
        }
        if (x - 1 >= 0) {
            ChildCells[3] = Grid[x - 1][y]; // left
        }
        if (bDiagonals) {
            if (x + 1 < fRows && y + 1 < fCols) {
                ChildCells[4] = Grid[x + 1][y + 1]; // top-right
            }
            if (x - 1 >= 0 && y + 1 < fCols) {
                ChildCells[5] = Grid[x - 1][y + 1]; // top-left
            }
            if (x + 1 < fRows && y - 1 >= 0) {
                ChildCells[6] = Grid[x + 1][y - 1]; // bottom-right
            }
            if (x - 1 >= 0 && y - 1 >= 0) {
                ChildCells[7] = Grid[x - 1][y - 1]; // bottom-left
            }
        }

        for (int i = 0; i < ChildCells.length; i++) {
            if (ChildCells[i] == TargetCell) {
                Path.add(ChildCells[i]);
                bPathFound = true;
            }
            if (ChildCells[i] != null) {
                if (ChildCells[i].bObstacle == false) {
                    if (ChildCells[i].ParentCell == null) {
                        ChildCells[i].setParentCell(ParentCell);
                        OpenList.add(ChildCells[i]);
                        ChildCells[i].bChecked = true;
                        if (i <= 3) {
                            ChildCells[i].nG = ParentCell.nG + nPerpendicular;
                        } else {
                            ChildCells[i].nG = ParentCell.nG + nDiagonal;
                        }
                    } else if (OpenList.contains(ChildCells[i])) {
                        if (i <= 3) {
                            if (ParentCell.nG + nPerpendicular < ChildCells[i].nG) {
                                ChildCells[i].setParentCell(ParentCell);
                                ChildCells[i].nG = ParentCell.nG + nPerpendicular;
                            }
                        } else {
                            if (ParentCell.nG + nDiagonal < ChildCells[i].nG) {
                                ChildCells[i].setParentCell(ParentCell);
                                ChildCells[i].nG = ParentCell.nG + nDiagonal;
                            }
                        }
                    }
                }
            }
        }
    }
}
