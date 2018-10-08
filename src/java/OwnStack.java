import java.util.Stack;

public class OwnStack<E> extends Stack<E> {

    private long steps;

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getSteps() {
        return steps;
    }

    @Override
    public synchronized Object clone() {
        OwnStack<E> clone = ((OwnStack<E>) super.clone());
        clone.steps = steps;
        return clone;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OwnStack)) {
            return false;
        }

        final OwnStack<E> stack = ((OwnStack<E>) o);
        if (elementCount != stack.elementCount) {
            return false;
        }

        for (int i = 0; i < elementCount; i++) {
            if(!elementData[i].equals(stack.elementData[i])) {
                return false;
            }
        }

        return true;
    }
}
