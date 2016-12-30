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
    private ArrayList<Cell> OpenList = new ArrayList<>();
    private ArrayList<Cell> ClosedList = new ArrayList<>();
    private ArrayList<Cell> Path = new ArrayList<>();
    private boolean bPathFound = false;
    private boolean bNoDiagonals;
    private int CalculationsPerFrame = 1;


    public PathFinder(Cell TargetCell, Cell StartCell, Cell[][] Grid, boolean bNoDiagonals) {
        this.TargetCell = TargetCell;
        this.StartCell = StartCell;
        this.Grid = Grid;
        this.bNoDiagonals = bNoDiagonals;
        Calc_Heuristic();
        OpenList.add(StartCell);
        SetChildren(StartCell);
    }

    public void FindPath() {
        if (!bPathFound) {
            for (int i = 0; i < CalculationsPerFrame; i++) {
                Cell TempCell;
                Collections.sort(OpenList, new DistanceComparator());
                if (OpenList.size() > 0) {
                    TempCell = OpenList.get(0);
                    ClosedList.add(TempCell);
                    OpenList.remove(TempCell);
                }
                SetChildren(ClosedList.get(ClosedList.size() - 1));
            }
        }
        if (bPathFound) {
            HighlightPath();
        }
    }

    public void HighlightPath() {
        Collections.sort(Path, new DistanceComparator()); // arrange the pats in ascending order
        Path.get(0).bHighlight = true; // highlight the first cell of the shortest path
        for (int x = 0; x < fRows; x++) {
            for (int y = 0; y < fCols; y++) {
                if (Grid[x][y].bHighlight == true && Grid[x][y].getParentCell() != StartCell) {
                    Grid[x][y].getParentCell().bHighlight = true;
                }
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
                    nHDist = (int) (Math.abs(TempCell.getX() - TargetCell.getX()) / GridSize);
                    nVDist = (int) (Math.abs(TempCell.getY() - TargetCell.getY()) / GridSize);
                    TempCell.nH = nHDist + nVDist;
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
        if (!bNoDiagonals) {
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
            if (ChildCells[i] != null) {
                if (ChildCells[i].bObstacle == false) {
                    if (ChildCells[i].ParentCell == null) {
                        ChildCells[i].setParentCell(ParentCell);
                        OpenList.add(ChildCells[i]);
                        ChildCells[i].bChecked = true;
                        if (i <= 3) {
                            ChildCells[i].nG = ParentCell.nG + 10;
                        } else {
                            ChildCells[i].nG = ParentCell.nG + 14;
                        }
                    } else if (OpenList.contains(ChildCells[i])) {
                        if (i <= 3) {
                            if (ParentCell.nG + 10 < ChildCells[i].nG) {
                                ChildCells[i].setParentCell(ParentCell);
                                ChildCells[i].nG = ParentCell.nG + 10;
                            }
                        } else {
                            if (ParentCell.nG + 14 < ChildCells[i].nG) {
                                ChildCells[i].setParentCell(ParentCell);
                                ChildCells[i].nG = ParentCell.nG + 14;
                            }
                        }
                    }
                    if (ChildCells[i].getX() == TargetCell.getX() && ChildCells[i].getY() == TargetCell.getY()) {
                        System.out.println("Found");
                        System.out.println(ChildCells[i].getX());
                        System.out.println(ChildCells[i].getY());
                        System.out.println("Target");
                        System.out.println(TargetCell.getX());
                        System.out.println(TargetCell.getY());
                        System.out.println();
//                        System.out.println("Path Found");
                        bPathFound = true;
                        Path.add(ParentCell);
                    }
                }
            }
        }
    }
}
