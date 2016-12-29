package path.finder;

import java.util.Comparator;

/**
 * Created by hafiz on 12/28/2016.
 */

public class DistanceComparator implements Comparator <Cell> {
    @Override
    public int compare(Cell cell1, Cell cell2) {
        int f1, f2;
        f1 = cell1.getnF();
        f2 = cell2.getnF();
        if (f1 > f2) {
            return 1;
        } else if (f1 < f2) {
            return -1;
        }
        return 0;
    }
}
