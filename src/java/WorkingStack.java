import enums.Direction;
import exceptions.FieldRepeatsException;
import exceptions.MaxLayerReachedException;
import exceptions.NoAvailableDirectionsException;
import utils.Randomizer;

import java.util.*;

public class WorkingStack {

    private final static long MAX_NUMBER_OF_STEPS = Long.MAX_VALUE;

    private final Stack<State> stack = new OwnStack<>();
    private final Set<Field> fields = new HashSet<>();
    private final Field finalField;
    private long steps;
    private int layer;
    private /*final*/ int maxLayer;

    private final Map<Stack<State>, Long> decisions = new HashMap<>();

    public WorkingStack(Field initialField, Field finalField, int maxLayer) {
        this.finalField = finalField;
        stack.add(new State(initialField));
        fields.add(initialField);
        this.layer = 1;
        this.maxLayer = maxLayer;
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

    public int getLayer() {
        return layer;
    }

    public int getCurrentLayer() {
        return stack.size() - 1;
    }

    public Map<Stack<State>, Long> getDecisions() {
        return decisions;
    }

    @SuppressWarnings("unchecked")
    public WorkingStack search() {
        long startTime;
        long decisionFoundTime;
        for (; layer <= maxLayer; ++layer) {
            startTime = System.currentTimeMillis();
            while (true) {
                State state = stack.peek();
                if (state.field.equals(finalField)) {
                    decisionFoundTime = System.currentTimeMillis();
                    if (!decisions.containsKey(stack)) {
                        ((OwnStack<State>) stack).setSteps(steps);
                        decisions.put((Stack<State>) stack.clone(), decisionFoundTime - startTime);
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
                        fields.add(stack.peek().field);
                        steps = 0;
                        break;
                    }
                    fields.remove(stack.pop().field);
                } catch (FieldRepeatsException e) {
                }
            }
        }

        return this;
    }

    public void printAllFoundDecisions(boolean detailed) {
        if (decisions.isEmpty()) {
            System.out.println("Decisions not found absolute!");
        } else {
            System.out.println("Max layer = " + maxLayer + ". Found " + decisions.size() + " decisions:");
            decisions.keySet().stream().sorted(Comparator.comparingInt(Stack::size))
                     .forEach(stack -> {
                         System.out.println("\t\nDecision found on layer " + (stack.size() - 1));
                         System.out.println("\tHashCode = " + stack.hashCode());
                         System.out.println("\tSteps done = " + ((OwnStack<State>) stack).getSteps());
                         System.out.println("\tTime = " + decisions.get(stack) + " ms");
                         if (detailed) {
                             stack.forEach(System.out::println);
                         }
                     });
        }
    }

    class State {

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

            if (stack.size() > layer) {
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
