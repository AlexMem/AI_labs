import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Start {

    private static final Integer $ = null;
    private static final Field initialField = new Field(new Integer[][]{{$, 4, 3},
                                                                        {6, 2, 1},
                                                                        {7, 5, 8}});
    private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                      {4, $, 5},
                                                                      {6, 7, 8}});
    /*private static final Field initialField = new Field(new Integer[][]{{5, 8, 3},
                                                                        {4, $, 2},
                                                                        {7, 6, 1}});
    private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                      {4, 5, 6},
                                                                      {7, 8, $}});*/

    public static void main(String[] args) {
        WorkingStack workingStack = new WorkingStack(initialField, finalField);
        workingStack.search();
    }
}
