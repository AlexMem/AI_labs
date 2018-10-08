import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Start {

    private static final int MAX_LAYER = 35;

    private static final Integer $ = null;

    private static final Field initialField = new Field(new Integer[][]{{$, 4, 3},
                                                                        {6, 2, 1},
                                                                        {7, 5, 8}});
    private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                      {4, $, 5},
                                                                      {6, 7, 8}});

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTimestamp;
        long finishTimestamp;

        for (int i = 0; i < MAX_LAYER; i++) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            startTimestamp = System.currentTimeMillis();
            Future<WorkingStack> result = executorService.submit(new WorkingStack(initialField, finalField, i)::search);
            executorService.shutdown();
            WorkingStack workingStack = result.get();
            finishTimestamp = System.currentTimeMillis();

            workingStack.printAllFoundDecisions(false);
            System.out.println("\nTime taken: " + (finishTimestamp - startTimestamp) + " ms\n\n");
        }
    }
}
