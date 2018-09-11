import enums.Direction;
import org.junit.jupiter.api.Test;

class FieldTest {

    private static final Integer $ = null;

    @Test
    void moveTest() {
        Field firstField = new Field(new Integer[][]{{$, 4, 3},
                                                     {6, 2, 1},
                                                     {7, 5, 8}});
        Field secondField = new Field(new Integer[][]{{4, $, 3},
                                                      {6, 2, 1},
                                                      {7, 5, 8}});


        firstField.move(Direction.RIGHT);
        assert firstField.equals(secondField);
    }

    @Test
    void equalsTest() {
        Field firstField = new Field(new Integer[][]{{$, 4, 3},
                                                     {6, 2, 1},
                                                     {7, 5, 8}});
        Field secondField = new Field(new Integer[][]{{$, 4, 3},
                                                      {6, 2, 1},
                                                      {7, 5, 8}});
        Field thirdField = new Field(new Integer[][]{{5, 4, 2},
                                                     {8, 3, 1},
                                                     {6, $, 7}});

        assert firstField != secondField;
        assert firstField.equals(secondField);
        assert firstField.hashCode() == secondField.hashCode();

        assert firstField != thirdField;
        assert !firstField.equals(thirdField);
        assert firstField.hashCode() != thirdField.hashCode();
    }
}