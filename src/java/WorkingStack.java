import enums.Direction;
import exceptions.FieldRepeatsException;
import exceptions.MaxLayerReachedException;
import exceptions.NoAvailableDirectionsException;
import utils.Randomizer;

import java.util.*;

public class WorkingStack {

    private final static long MAX_NUMBER_OF_STEPS = Long.MAX_VALUE;

    private final Stack<State> stack = new Stack<>();
    private final Set<Field> fields = new HashSet<>();
    private final Field finalField;
    private long steps;
    private int maxLayer;

    private final Set<Stack<State>> winnerStackSet = new HashSet<>();

    public WorkingStack(Field initialField, Field finalField) {
        stack.add(new State(initialField));
        fields.add(initialField);
        this.finalField = finalField;
        maxLayer = 2;

        /*System.out.println("Initial field:");
        System.out.println(initialField);
        System.out.println("Final field:");
        System.out.println(finalField);*/
    }

    public Stack<State> getStack() {
        return stack;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public long getSteps() {
        return steps;
    }

    public int getMaxLayer() {
        return maxLayer;
    }

    public int getCurrentLayer() {
        return stack.size();
    }

    @SuppressWarnings("unchecked")
    public void search(Thread parent) {
        for (; maxLayer <= 17; ++maxLayer) {
            while (true) {
                State state = stack.peek();
                if (state.field.equals(finalField)) {
                    System.out.print("Decision found on layer " + stack.size() + "! Steps = " + steps);
                    System.out.println(" " + winnerStackSet.contains(stack));
//                    stack.forEach(System.out::println);
//                    parent.interrupt();
//                    ret
                    if (!winnerStackSet.contains(stack)) {
                        winnerStackSet.add((Stack<State>) stack.clone());
                    }
                }

                try {
                    State nextState = state.nextState();
                    ++steps;
                    stack.push(nextState);
                    fields.add(nextState.field);
                } catch (NoAvailableDirectionsException | MaxLayerReachedException e) {
                    if (stack.size() == 1 && e instanceof NoAvailableDirectionsException) {
                        stack.peek().recountAvailableDirections();
                        fields.clear();
                        steps = 0;
                        break;
                    }
                    fields.remove(stack.pop().field);
                } catch (FieldRepeatsException e) {
                }
            }
        }

        if (winnerStackSet.isEmpty()) {
            System.out.println("Decision not found absolute!");
        } else {
            winnerStackSet.forEach(stack -> {
                System.out.println("\nDecision found on layer " + stack.size());
                stack.forEach(System.out::println);
                System.out.println("HashCode = " + stack.hashCode());
            });
        }

        parent.interrupt();
    }

    private class State {

        private Field field;
        private List<Direction> availableDirections;

        public State(Field field) {
            this.field = field;
            availableDirections = countAvailableDirections();
        }

        public State nextState() throws NoAvailableDirectionsException, FieldRepeatsException, MaxLayerReachedException {
            if (availableDirections.isEmpty()) {
                throw new NoAvailableDirectionsException();
            }

            if (stack.size() == maxLayer) {
                throw new MaxLayerReachedException();
            }

            Direction direction = chooseDirection();
            availableDirections.remove(direction);
            Field newField = new Field(field);
            newField.move(direction);

            if (fields.contains(newField)) {
                throw new FieldRepeatsException();
            }

            return new State(newField);
        }

        public void recountAvailableDirections() {
            availableDirections = countAvailableDirections();
        }

        @Override
        public int hashCode() {
            return field.hashCode();
        }

        @Override
        public String toString() {
            return field.toString();
        }

        private Direction chooseDirection() {
            return Randomizer.pick(availableDirections.toArray(new Direction[0]));
        }

        private List<Direction> countAvailableDirections() {
            if (field.getEmptyPlacePosition().x == 0 && field.getEmptyPlacePosition().y == 0) {
                return new ArrayList<>(Arrays.asList(Direction.DOWN, Direction.RIGHT));
            }

            if (field.getEmptyPlacePosition().x == 0 && field.getEmptyPlacePosition().y == field.getSize() - 1) {
                return new ArrayList<>(Arrays.asList(Direction.DOWN, Direction.LEFT));
            }

            if (field.getEmptyPlacePosition().x == field.getSize() - 1 && field.getEmptyPlacePosition().y == 0) {
                return new ArrayList<>(Arrays.asList(Direction.UP, Direction.RIGHT));
            }

            if (field.getEmptyPlacePosition().x == field.getSize() - 1 && field.getEmptyPlacePosition().y == field.getSize() - 1) {
                return new ArrayList<>(Arrays.asList(Direction.UP, Direction.LEFT));
            }


            if (field.getEmptyPlacePosition().x == 0) {
                return new ArrayList<>(Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.RIGHT));
            }

            if (field.getEmptyPlacePosition().x == field.getSize() - 1) {
                return new ArrayList<>(Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT));
            }

            if (field.getEmptyPlacePosition().y == 0) {
                return new ArrayList<>(Arrays.asList(Direction.UP, Direction.DOWN, Direction.RIGHT));
            }

            if (field.getEmptyPlacePosition().y == field.getSize() - 1) {
                return new ArrayList<>(Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT));
            }


            return new ArrayList<>(Arrays.asList(Direction.values()));
        }
    }
}
