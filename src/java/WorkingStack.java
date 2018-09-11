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

    public WorkingStack(Field initialField, Field finalField) {
        stack.add(new State(initialField));
        fields.add(initialField);
        this.finalField = finalField;
        maxLayer = 2;

        System.out.println("Initial field:");
        System.out.println(initialField);
        System.out.println("Final field:");
        System.out.println(finalField);
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

    public void search(Thread parent) {
        for (; steps != MAX_NUMBER_OF_STEPS; ++maxLayer) {
            while (true) {
                State state = stack.peek();
                if (state.field.equals(finalField)) {
                    System.out.println("Decision found on layer " + stack.size() + "! Steps = " + steps);
                    System.out.println("Found field:");
                    System.out.println(state.field);
                    parent.interrupt();
                    return;
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
                        break;
                    }
                    stack.pop();
                } catch (FieldRepeatsException e) {
                }
            }
        }
        System.out.println("Decision not found absolute!");
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
