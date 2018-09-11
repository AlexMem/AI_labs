import java.util.concurrent.TimeUnit;

public class Start {

    private static final Integer $ = null;

    private static final Field initialField = new Field(new Integer[][]{{$, 4, 3},
                                                                        {6, 2, 1},
                                                                        {7, 5, 8}});
    private static final Field finalField = new Field(new Integer[][]{{1, 2, 3},
                                                                      {4, $, 5},
                                                                      {6, 7, 8}});

    private static final long sleepDuration = TimeUnit.SECONDS.toMillis(5);

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();

        long startTimestamp;
        long finishTimestamp;

        WorkingStack workingStack = new WorkingStack(initialField, finalField);
        Thread worker = new Thread(() -> workingStack.search(mainThread));
        worker.setPriority(Thread.MAX_PRIORITY);

        startTimestamp = System.currentTimeMillis();
        worker.start();
        while (worker.isAlive()) {
            try {
                System.out.println("Work in progress: steps = " + workingStack.getSteps() +
                        ", stack size = " + workingStack.getStack().size() +
                        ", set size = " + workingStack.getFields().size());
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                break;
            }
        }
        finishTimestamp = System.currentTimeMillis();

        System.out.println("Time taken: " + (finishTimestamp - startTimestamp) + " ms");
    }
}
