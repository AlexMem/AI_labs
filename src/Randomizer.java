import java.util.concurrent.ThreadLocalRandom;
public final class Randomizer {
    private final static ThreadLocalRandom randomizer = ThreadLocalRandom.current();
    public static int pick(Integer... array) {
        return array[randomizer.nextInt(array.length)];
    }
    public static Direction pick(Direction... array) {
        return array[randomizer.nextInt(array.length)];
    }
}