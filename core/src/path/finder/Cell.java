package path.finder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by hafiz on 12/26/2016.
 */

public class Cell {
    private float fX, fY, fLength;
    ShapeRenderer shape;
    boolean bObstacle;
    float nH, nG, nF;
    Cell ParentCell;
    boolean bHighlight = false;
    boolean bChecked = false;
    boolean bStartingCell = false;
    boolean bTarget = false;

    public Cell(float fX, float fY, float fLength, boolean bObstacle, ShapeRenderer shape) {
        this.fX = fX;
        this.fY = fY;
        this.fLength = fLength;
        this.shape = shape;
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
        } else if (bStartingCell) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.YELLOW);
            shape.rect(fX, fY, fLength, fLength);
            shape.end();
        } else if (bObstacle) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.RED);
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
                shape.setColor(Color.BLACK);
                shape.rect(fX, fY, fLength, fLength);
                shape.end();
            } else {
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(Color.SKY);
                shape.rect(fX, fY, fLength, fLength);
                shape.end();
            }
        }
    }

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
