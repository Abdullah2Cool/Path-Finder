package path.finder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Comparator;

/**
 * Created by hafiz on 12/26/2016.
 */

public class Cell {
    private float fX, fY, fLength;
    ShapeRenderer shape;
    boolean bObstacle;
    boolean bTarget;
    float nH, nG, nF;
    Cell ParentCell;
    boolean bHighlight = false;
    boolean bChecked = false;

    public Cell(float fX, float fY, float fLength, boolean bObstacle) {
        this.fX = fX;
        this.fY = fY;
        this.fLength = fLength;
        shape = new ShapeRenderer();
        this.bObstacle = bObstacle;
        nH = 0;
        nG = 0;
        nF = nG + nH;
        ParentCell = null;
    }

    public void show() {
        if (bTarget) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.WHITE);
            shape.rect(fX, fY, fLength, fLength);
            shape.end();
        } else if (bObstacle) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.BLACK);
            shape.rect(fX, fY, fLength, fLength);
            shape.end();
        } else {
            nF = nH + nG;
            if (bHighlight) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(Color.GREEN);
                shape.rect(fX, fY, fLength, fLength);
                shape.end();
            } else if (bChecked) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(Color.SLATE);
                shape.rect(fX, fY, fLength, fLength);
                shape.end();
            } else {
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(nH / 255 * 1.5f, nH / 255 * 1.5f, nH / 255 * 1.5f, 1f);
                shape.rect(fX, fY, fLength, fLength);
                shape.end();
            }
        }
    }

    public static Comparator<Cell> CellF = new Comparator<Cell>() {

        public int compare(Cell c1, Cell c2) {

            int F1 = c1.getnF();
            int F2 = c1.getnF();

	   /*For ascending order*/
            return F1 - F2;
        }
    };

    public float getX() {
        return fX;
    }

    public float getY() {
        return fY;
    }

    public int getnF() {
        return (int) nF;
    }

    public void setParentCell(Cell cell) {
        ParentCell = cell;
    }

    public Cell getParentCell() {
        return ParentCell;
    }
}
