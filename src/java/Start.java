import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Start {

    private static final int MAX_LAYER = 24;

    private static final Integer $ = null;
    /*
        private static final Field initialField = new Field(new Integer[][]{{$, 4, 3},
                                                                            {6, 2, 1},
                                                                            {7, 5, 8}});
        private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                          {4, $, 5},
                                                                          {6, 7, 8}});*/
    private static final Field initialField = new Field(new Integer[][]{{5, 8, 3},
                                                                        {4, $, 2},
                                                                        {7, 6, 1}});
    private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                      {4, 5, 6},
                                                                      {7, 8, $}});

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTimestamp;
        long finishTimestamp;
        long timeTaken;
        HashMap<Integer, Long> time = new HashMap<>();

        int maxLayer = 24;
//        for (int layer = 0; layer <= MAX_LAYER; layer++) {
        WorkingStack workingStack = new WorkingStack(initialField, finalField, maxLayer);
        startTimestamp = System.currentTimeMillis();
        workingStack.search();
        finishTimestamp = System.currentTimeMillis();

        workingStack.printAllFoundDecisions(true);
        timeTaken = finishTimestamp - startTimestamp;
        time.put(maxLayer, timeTaken);
        System.out.println("\nTime taken: " + timeTaken + " ms\n\n");
//        }
        time.forEach((layer, timeT) -> System.out.println(layer + ": " + timeT + " ms"));
    }
}
