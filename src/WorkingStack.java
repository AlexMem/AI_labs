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
                    System.out.println("Decision found on layer " + stack.size() + "! Steps = " + steps);
//                    stack.forEach(System.out::println);
//                    parent.interrupt();
//                    ret
                    winnerStackSet.add((Stack<State>) stack.clone());
                }

                try {
                    State nextState = state.nextState();
                    ++steps;
                    stack.push(nextState);
                    fields.add(nextState.field);
                } catch (NoAvailableDirectionsException e) {
                    if (stack.size() == 1 && e instanceof NoAvailableDirectionsException) {
                        stack.peek().recountAvailableDirections();
                        fields.clear();
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
            winnerStackSet.forEach(states -> {
                System.out.println("\nDecision found on layer " + states.size());
                states.forEach(System.out::println);
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

        public void recountAvailableDirections() {
            availableDirections = countAvailableDirections();
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