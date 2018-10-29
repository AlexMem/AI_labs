import enums.Direction;
import exceptions.FieldRepeatsException;
import exceptions.NoAvailableDirectionsException;

import java.util.*;

public class WorkingStack {

    private final Stack<State> stack = new OwnStack<>();
    private final Set<Field> fields = new HashSet<>();
    private final Field finalField;
    private long steps;
    private int layer;

    public WorkingStack(Field initialField, Field finalField) {
        this.finalField = finalField;
        stack.add(new State(initialField));
        fields.add(initialField);
        this.layer = 1;
    }

    @SuppressWarnings("unchecked")
    public void search() {
        long startTime;
        long decisionFoundTime;
        startTime = System.currentTimeMillis();
        while (true) {
            System.out.println("Current layer: " + (stack.size() - 1));
            State state = stack.peek();
            if (state.field.equals(finalField)) {
                decisionFoundTime = System.currentTimeMillis();
                System.out.println("\t\nDecision found on layer " + (stack.size() - 1));
                System.out.println("\tHashCode = " + stack.hashCode());
                System.out.println("\tSteps done = " + steps);
                System.out.println("\tTime = " + (decisionFoundTime - startTime) + " ms");
                return;
            }

            try {
                State nextState = state.nextState();
                ++steps;
                stack.push(nextState);
                fields.add(nextState.field);
            } catch (NoAvailableDirectionsException e) {
                if (stack.size() == 1) {
                    break;
                }
                fields.remove(stack.pop().field);
            } catch (FieldRepeatsException e) {
            }
        }

        System.out.println("Decision was not found!");
    }

    class State {

        private Field field;
        private List<Direction> availableDirections;

        public State(Field field) {
            this.field = field;
            availableDirections = countAvailableDirections();
        }

        public State nextState() throws NoAvailableDirectionsException, FieldRepeatsException {
            if (availableDirections.isEmpty()) {
                throw new NoAvailableDirectionsException();
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State)) {
                return false;
            }
            return field.equals(((State) o).field);
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
            Map<Direction, Field> map = new HashMap<>();
            availableDirections.forEach(direction -> map.put(direction, new Field(field).move(direction)));
            return map.entrySet().stream().min(Comparator.comparingInt(o -> o.getValue().computeH1(finalField))).get().getKey();
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
