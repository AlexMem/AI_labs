import com.sun.istack.internal.NotNull;
import enums.Direction;
import utils.IntegerMatrixPrinter;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class Field {

//        0  1  2
//       -------- y
//    0 | _  4  3
//    1 | 6  2  1
//    2 | 7  5  8
//      x
//
//    field[2][0] == 3
//    null == empty cage
//
//    directions:
//      0 - up
//      1 - right
//      2 - down
//      3 - left

    private final static int SIMPLE_NUMBER = 7;

    private Integer[][] field;
    private int size;
    private Point emptyPlacePosition;

    public Field(Integer[][] field) {
        this.field = field;
        this.size = field.length;
        emptyPlacePosition = new Point();
        computeCurrentEmptyPlacePosition();
    }

    public Field(Field field) {
        this.field = copyOfField(field.field);
        this.size = field.size;
        this.emptyPlacePosition = new Point(field.emptyPlacePosition);
    }

    public Integer[][] getField() {
        return field;
    }

    public int getSize() {
        return size;
    }

    public Point getEmptyPlacePosition() {
        return emptyPlacePosition;
    }

    public Field move(Direction direction) {
        switch (direction) {
            case UP:
                swapEmptyPlaceWith(emptyPlacePosition.x - 1, emptyPlacePosition.y);
                break;
            case RIGHT:
                swapEmptyPlaceWith(emptyPlacePosition.x, emptyPlacePosition.y + 1);
                break;
            case DOWN:
                swapEmptyPlaceWith(emptyPlacePosition.x + 1, emptyPlacePosition.y);
                break;
            case LEFT:
                swapEmptyPlaceWith(emptyPlacePosition.x, emptyPlacePosition.y - 1);
                break;
        }
        return this;
    }

    public int computeH1(Field finalField) {
        if (size != finalField.size) {
            throw new IllegalArgumentException("Field's sizes incompatible");
        }

        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!Objects.equals(field[i][j], finalField.field[i][j])) {
                    ++count;
                }
            }
        }
        return count;
    }

    public int computeH2(Field finalField) {
        if (size != finalField.size) {
            throw new IllegalArgumentException("Field's sizes incompatible");
        }

        int count = 0;
        Point coordsOfNumber;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(finalField.field[j][i] != null) {
                    coordsOfNumber = getCoordsOf(finalField.field[j][i]);
                    count += Math.abs(j - coordsOfNumber.getY()) + Math.abs(i - coordsOfNumber.getX());
                }
            }
        }
        return count;
    }

    private Point getCoordsOf(Integer number) {
        if (number == null) {
            return getEmptyPlacePosition();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Objects.equals(field[i][j], number)) {
                    return new Point(j, i);
                }
            }
        }
        throw new RuntimeException("Number not found");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || Arrays.deepEquals(field, ((Field) obj).field);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != null) {
                    hashCode += (i + 1) * (j + 1) * field[i][j] * SIMPLE_NUMBER;
                }
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return IntegerMatrixPrinter.writeToString(field);
    }

    private void swapEmptyPlaceWith(int x, int y) {
        field[emptyPlacePosition.x][emptyPlacePosition.y] = field[x][y];
        field[x][y] = null;
        emptyPlacePosition.setLocation(x, y);
    }

    private Point computeCurrentEmptyPlacePosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == null) {
                    emptyPlacePosition.setLocation(i, j);
                    return emptyPlacePosition;
                }
            }
        }
        throw new RuntimeException("Empty place not found");
    }

    private Integer[][] copyOfField(@NotNull Integer[][] original) {
        Integer[][] copy = new Integer[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original.length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
}
