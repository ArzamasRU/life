package ru.lavr.gdx.utils;

import static ru.lavr.gdx.constants.Constant.BOTTOM_EDGE;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.LEFT_EDGE;
import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.STEP;
import static ru.lavr.gdx.constants.Constant.UPPER_EDGE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.organisms.OrganismHolder;
import ru.lavr.gdx.organisms.Plant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonUtils {
    private final static Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);
    private final static Rectangle field = new Rectangle(LEFT_EDGE, BOTTOM_EDGE, RIGHT_EDGE, UPPER_EDGE);
    private final static Rectangle changedRectangle = new Rectangle(0, 0, 0, 0);

    public static Vector2 getRandomPosition() {
        Vector2 vector2 = new Vector2();
        float x = MathUtils.random(LEFT_EDGE / CELL_SIZE, RIGHT_EDGE / CELL_SIZE);
        float y = MathUtils.random(BOTTOM_EDGE / CELL_SIZE, UPPER_EDGE / CELL_SIZE);
        return vector2.set(x * CELL_SIZE, y * CELL_SIZE);
    }

    public static int getRandomDirection() {
        return MathUtils.random(1, 9);
    }

    public static Integer getRandomDirection(List<Integer> directions) {
        if (!directions.isEmpty()) {
            return directions.get(MathUtils.random(1, directions.size()) - 1);
        }
        return 0;
    }

    public static Vector2 getDirection(Vector2 position, int direction) {
        return getDirection(position, direction, 1);
    }

    public static Vector2 getDirection(Vector2 position, int direction, int multiplier) {
        Vector2 vector2 = new Vector2(position);
        int multipliedStep = STEP * multiplier;
        if (direction == 1) {
            return vector2.add(-multipliedStep, +multipliedStep);
        } else if (direction == 2) {
            return vector2.add(0, +multipliedStep);
        } else if (direction == 3) {
            return vector2.add(+multipliedStep, +multipliedStep);
        } else if (direction == 4) {
            return vector2.add(-multipliedStep, 0);
        } else if (direction == 6) {
            return vector2.add(+multipliedStep, 0);
        } else if (direction == 7) {
            return vector2.add(-multipliedStep, -multipliedStep);
        } else if (direction == 8) {
            return vector2.add(0, -multipliedStep);
        } else if (direction == 9) {
            return vector2.add(+multipliedStep, -multipliedStep);
        }
        return vector2;
    }

    //    проверяет не перекрывается ли расположение с другими клетками
    public static boolean isValidPosition(Vector2 vector2, List<Organism> organisms) {
        rectangle.x = vector2.x;
        rectangle.y = vector2.y;
        return organisms.stream()
                .filter(Organism::isNotOutOfBorder)
                .map(Organism::getUpdatedRectangle)
                .noneMatch(rectangle::overlaps);
    }

    public static boolean isNotValidPosition(Vector2 vector2, List<Organism> organisms) {
        return !isValidPosition(vector2, organisms);
    }

    //    проверяет не заходит ли клетка за поля
    public static boolean isValidDirection(Vector2 vector2) {
        rectangle.x = vector2.x;
        rectangle.y = vector2.y;
        return field.overlaps(rectangle);
    }

    public static boolean isNotValidDirection(Vector2 vector2) {
        return !isValidDirection(vector2);
    }

    public static boolean isFreeSpace(Vector2 position, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newHerbivores = organismHolder.getNewHerbivores();
        List<Organism> newPredators = organismHolder.getNewPredators();
        List<Organism> herbivores = organismHolder.getHerbivores();
        List<Organism> predators = organismHolder.getPredators();
        return IntStream.range(1, 10)
                .mapToObj(i -> getDirection(position, i, multiplier))
                .anyMatch(newPosition -> isValidPosition(newPosition, herbivores)
                        && isValidPosition(newPosition, newHerbivores)
                        && isValidPosition(newPosition, predators)
                        && isValidPosition(newPosition, newPredators)
                        && isValidDirection(newPosition));
    }

    public static boolean isOrganismClose(Vector2 position, List<Organism> organisms) {
        return IntStream.range(1, 10)
                .mapToObj(i -> getDirection(position, i, 1))
                .anyMatch(newPosition -> isNotValidPosition(newPosition, organisms));
    }

    private static Organism getOrganism(Vector2 position, List<Organism> organisms) {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return organisms.stream()
                .filter(org -> rectangle.overlaps(org.getUpdatedRectangle()))
                .findAny().orElse(null);
    }

    public static Organism getCloseOrganism(Vector2 position, List<Organism> organisms, int multiplier) {
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP * multiplier, -STEP * multiplier);
        changedRectangle.x = vector2.x;
        changedRectangle.y = vector2.y;
//        m = 1 => 3; m = 2 => 5; m = 3 => 7
        changedRectangle.width = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.height = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        return organisms.stream()
                .filter(organism -> changedRectangle.overlaps(organism.getUpdatedRectangle()))
                .findAny().orElse(null);
    }

    public static Organism getCloseOrganism(Vector2 position, List<Organism> organisms) {
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .mapToObj(i -> getDirection(position, i, 1))
                .map(newPosition -> getOrganism(newPosition, organisms))
                .filter(Objects::nonNull)
                .findAny().orElse(null);
    }

    public static Integer getOrganismIndex(Vector2 position, List<Organism> organisms) {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return IntStream.range(0, organisms.size())
                .filter(i -> rectangle.overlaps(organisms.get(i).getUpdatedRectangle()))
                .boxed()
                .findAny().orElse(null);
    }

    public static Integer getCloseOrganismIndex(Vector2 position, List<Organism> organisms) {
        return IntStream.range(1, 10)
                .mapToObj(i -> getDirection(position, i, 1))
                .map(newPosition -> getOrganismIndex(newPosition, organisms))
                .filter(Objects::nonNull)
                .findAny().orElse(null);
    }

    public static int getCloseOrganismPosition(Vector2 position, List<Organism> organisms) {
        return IntStream.range(1, 10)
                .filter(i -> isNotValidPosition(getDirection(position, i, 1), organisms))
                .findAny().orElse(0);
    }

    public static Vector2 getPositionFrom(Vector2 position, Vector2 anotherPosition, boolean opposite, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        return getOppositeDirections(position, anotherPosition, opposite, multiplier).stream()
                .filter(oppositeDirection -> {
                    Rectangle square = CommonUtils.getSquare(oppositeDirection);
                    return isValidDirection(oppositeDirection)
                            && isValidPosition(oppositeDirection, predatorsMap.get(square))
                            && isValidPosition(oppositeDirection, herbivoresMap.get(square));
                })
                .findFirst().orElse(position);
    }

    public static List<Vector2> getOppositeDirections(
            Vector2 position, Vector2 anotherPosition, boolean opposite, int multiplier) {
        float deltaX;
        float deltaY;
        if (opposite) {
            deltaX = position.x - anotherPosition.x;
            deltaY = position.y - anotherPosition.y;
        } else {
            deltaX = anotherPosition.x - position.x;
            deltaY = anotherPosition.y - position.y;
        }
        if (deltaX > 0) {
            deltaX = STEP * multiplier;
        } else if (deltaX < 0) {
            deltaX = -STEP * multiplier;
        }
        if (deltaY > 0) {
            deltaY = STEP * multiplier;
        } else if (deltaY < 0) {
            deltaY = -STEP * multiplier;
        }
        return Arrays.asList(new Vector2(position).add(deltaX, deltaY),
                new Vector2(position).add(deltaX, 0),
                new Vector2(position).add(0, deltaY));
    }

    public static int getOppositeDirection(Vector2 position, int oppositeFrom) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        return Arrays.stream(getOppositeDirection(oppositeFrom))
                .filter(oppositeDirection -> {
                    Vector2 direction = getDirection(position, oppositeDirection, 1);
                    return isValidDirection(direction)
                            && isValidPosition(direction, herbivoresMap.get(CommonUtils.getSquare(direction)))
                            && isValidPosition(direction, predatorsMap.get(CommonUtils.getSquare(direction)));
                })
                .findFirst().orElse(0);
    }

    public static int[] getOppositeDirection(int direction) {
        if (direction == 1) {
            return new int[]{9, 6, 8, 3, 7};
        } else if (direction == 2) {
            return new int[]{7, 9, 8};
        } else if (direction == 3) {
            return new int[]{7, 4, 8, 1, 9};
        } else if (direction == 4) {
            return new int[]{3, 9, 6};
        } else if (direction == 6) {
            return new int[]{1, 7, 4};
        } else if (direction == 7) {
            return new int[]{3, 2, 6, 1, 9};
        } else if (direction == 8) {
            return new int[]{1, 3, 2};
        } else if (direction == 9) {
            return new int[]{1, 2, 4, 3, 7};
        }
        return new int[]{};
    }

    public static boolean isNotFreeSpace(Vector2 position) {
        return !isFreeSpace(position, 1);
    }

    public static boolean isNotFreeSpace(Vector2 position, int multiplier) {
        return !isFreeSpace(position, multiplier);
    }

    public static List<Organism> getNeighbors(Vector2 position) {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP, -STEP);
        changedRectangle.width = CELL_SIZE * 3;
        changedRectangle.height = CELL_SIZE * 3;
        changedRectangle.x = vector2.x;
        changedRectangle.y = vector2.y;
        rectangle.x = position.x;
        rectangle.y = position.y;
        List<Organism> neighbors = getSquares(changedRectangle).stream()
                .map(plantsMap::get)
                .flatMap(List::stream)
                .filter(organism -> changedRectangle.overlaps(organism.getUpdatedRectangle()))
                .filter(organism -> !rectangle.overlaps(organism.getUpdatedRectangle()))
                .collect(Collectors.toList());
        IntStream.range(1, 10)
                .mapToObj(i -> CommonUtils.getDirection(position, i))
                .filter(CommonUtils::isNotValidDirection)
                .forEach(outOfBorder -> neighbors.add(new Plant(true)));
        return neighbors;
    }

    public static List<Organism> getCloseOrganisms(
            Vector2 position, Map<Rectangle, List<Organism>> organismsMap, int multiplier) {
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP * multiplier, -STEP * multiplier);
        changedRectangle.width = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.height = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.x = vector2.x;
        changedRectangle.y = vector2.y;
//        rectangle.x = position.x;
//        rectangle.y = position.y;
//        Gdx.app.log("getCloseOrganisms ", String.valueOf(changedRectangle));
        return getSquares(changedRectangle).stream()
                .map(organismsMap::get)
                .flatMap(List::stream)
                .filter(organism -> {
                    Gdx.app.log("herbivore organism", changedRectangle + " " + organism);
//                    if (changedRectangle.overlaps(organism.getUpdatedRectangle())) {
//                        Gdx.app.log("overlaps! ", changedRectangle + " " + organism);
//                    }
                    return changedRectangle.overlaps(organism.getUpdatedRectangle());
                })
//                .filter(organism -> !rectangle.overlaps(organism.getUpdatedRectangle()))
                .collect(Collectors.toList());
    }

    public static void setInactiveOrganisms(List<Organism> organisms) {
        organisms.stream()
                .filter(org -> org.getNeighbors().size() >= 8)
                .filter(org -> org.getNeighbors().stream()
                        .map(Organism::getNeighbors)
                        .allMatch(ns -> ns.size() >= 8))
                .forEach(org -> org.setActive(false));
    }

    public static void updateOrganisms() {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> plants = organismHolder.getPlants();
        List<Organism> herbivores = organismHolder.getHerbivores();
        List<Organism> predators = organismHolder.getPredators();
        List<Organism> newPlants = organismHolder.getNewPlants();
        List<Organism> newHerbivores = organismHolder.getNewHerbivores();
        List<Organism> newPredators = organismHolder.getNewPredators();

        herbivores.removeAll(herbivores.stream()
                .filter(org -> org.getFullness() <= 0)
                .collect(Collectors.toList()));
        predators.removeAll(predators.stream()
                .filter(org -> org.getFullness() <= 0)
                .collect(Collectors.toList()));

        plants.addAll(newPlants.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        herbivores.addAll(newHerbivores.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        predators.addAll(newPredators.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        newPlants.clear();
        newHerbivores.clear();
        newPredators.clear();
    }

    public static Rectangle getSquare(Vector2 position) {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return OrganismHolder.getOrganismHolder().getSquares().stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .findAny().orElseThrow(() -> new RuntimeException("square is not found! " + rectangle));
    }

    public static Rectangle getSquare(Rectangle rectangle) {
        return OrganismHolder.getOrganismHolder().getSquares().stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .findAny().orElseThrow(() -> new RuntimeException("square is not found!"));
    }

    public static List<Rectangle> getSquares(Rectangle rectangle) {
        return OrganismHolder.getOrganismHolder().getSquares().stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .collect(Collectors.toList());
    }
}
