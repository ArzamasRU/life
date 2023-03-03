package ru.lavr.gdx.utils;

import static ru.lavr.gdx.constants.ApplicationConstants.BOTTOM_EDGE;
import static ru.lavr.gdx.constants.LifeConstants.CELL_SIZE;
import static ru.lavr.gdx.constants.LifeConstants.CHANCE_OF_MOMENTUM_CHANGE;
import static ru.lavr.gdx.constants.ApplicationConstants.LEFT_EDGE;
import static ru.lavr.gdx.constants.LifeConstants.MAX_CHANCE;
import static ru.lavr.gdx.constants.ApplicationConstants.RIGHT_EDGE;
import static ru.lavr.gdx.constants.LifeConstants.STEP;
import static ru.lavr.gdx.constants.ApplicationConstants.UPPER_EDGE;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.organisms.Plant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonUtils {
    private final static Rectangle field = new Rectangle(LEFT_EDGE, BOTTOM_EDGE, RIGHT_EDGE, UPPER_EDGE);
    //    assign x and y each time before use
    private final static Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);
    //    assign x, y, width, height each time before use
    private final static Rectangle changedRectangle = new Rectangle(0, 0, 0, 0);

    public static Vector2 getRandomPosition() {
        Vector2 vector2 = new Vector2();
        float x = MathUtils.random(LEFT_EDGE / CELL_SIZE, RIGHT_EDGE / CELL_SIZE);
        float y = MathUtils.random(BOTTOM_EDGE / CELL_SIZE, UPPER_EDGE / CELL_SIZE);
        return vector2.set(x * CELL_SIZE, y * CELL_SIZE);
    }

    public static Integer getRandomDirection(List<Integer> directions) {
        if (directions.isEmpty()) {
            return 0;
        }
        return directions.get(MathUtils.random(1, directions.size()) - 1);
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

    //    checks if the location overlaps with other cells
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

    //    checks if the cell goes beyond the fields
    public static boolean isValidDirection(Vector2 vector2) {
        rectangle.x = vector2.x;
        rectangle.y = vector2.y;
        return field.overlaps(rectangle);
    }

    public static boolean isNotValidDirection(Vector2 vector2) {
        return !isValidDirection(vector2);
    }

    public static boolean isFreeSpace(Vector2 position, Rectangle curRect, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        return IntStream.range(1, 10)
                .mapToObj(i -> getDirection(position, i, multiplier))
                .anyMatch(newPosition -> {
                    Rectangle square = CommonUtils.getSquare(newPosition, curRect);
                    return isValidDirection(newPosition)
                            && isValidPosition(newPosition, herbivoresMap.get(square))
                            && isValidPosition(newPosition, predatorsMap.get(square));
                });
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
        changedRectangle.width = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.height = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        return organisms.stream()
                .filter(organism -> changedRectangle.overlaps(organism.getUpdatedRectangle()))
                .findAny().orElse(null);
    }

    public static List<Organism> getOrganismsInSquare(
            Vector2 position, Rectangle curRect, Map<Rectangle, List<Organism>> organismsMap, int multiplier) {
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP * multiplier, -STEP * multiplier);
        changedRectangle.width = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.height = CELL_SIZE * 2 * multiplier + CELL_SIZE;
        changedRectangle.x = vector2.x;
        changedRectangle.y = vector2.y;
        return getSquares(changedRectangle, curRect).stream()
                .map(organismsMap::get)
                .flatMap(List::stream)
                .filter(organism -> changedRectangle.overlaps(organism.getUpdatedRectangle()))
                .collect(Collectors.toList());
    }

    public static Vector2 getPositionFrom(Vector2 position, Rectangle curRect, Vector2 anotherPosition, boolean opposite) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        return getOppositeDirections(position, anotherPosition, opposite).stream()
                .filter(oppositeDirection -> {
                    Rectangle square = CommonUtils.getSquare(oppositeDirection, curRect);
                    return Objects.nonNull(square)
                            && isValidDirection(oppositeDirection)
                            && isValidPosition(oppositeDirection, predatorsMap.get(square))
                            && isValidPosition(oppositeDirection, herbivoresMap.get(square));
                })
                .findFirst().orElse(null);
    }

    public static List<Vector2> getOppositeDirections(
            Vector2 position, Vector2 anotherPosition, boolean opposite) {
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
            deltaX = STEP;
        } else if (deltaX < 0) {
            deltaX = -STEP;
        }
        if (deltaY > 0) {
            deltaY = STEP;
        } else if (deltaY < 0) {
            deltaY = -STEP;
        }
        return Arrays.asList(new Vector2(position).add(deltaX, deltaY),
                new Vector2(position).add(deltaX, 0),
                new Vector2(position).add(0, deltaY));
    }

    public static boolean isNotFreeSpace(Vector2 position, Rectangle curRect) {
        return !isFreeSpace(position, curRect, 1);
    }

    public static boolean isNotFreeSpace(Vector2 position, Rectangle curRect, int multiplier) {
        return !isFreeSpace(position, curRect, multiplier);
    }

    public static List<Organism> getNeighbors(Vector2 position, Rectangle curRect) {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP, -STEP);
        changedRectangle.width = CELL_SIZE * 3;
        changedRectangle.height = CELL_SIZE * 3;
        changedRectangle.x = vector2.x;
        changedRectangle.y = vector2.y;
        rectangle.x = position.x;
        rectangle.y = position.y;
        List<Organism> neighbors = getSquares(changedRectangle, curRect).stream()
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

    public static Rectangle getSquare(Vector2 position) {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return OrganismHolder.getOrganismHolder().getSquares().keySet().stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .findAny().orElse(null);
    }

    public static Rectangle getSquare(Rectangle rectangle) {
        return OrganismHolder.getOrganismHolder().getSquares().keySet().stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .findAny().orElse(null);
    }

    public static Rectangle getSquare(Vector2 position, Rectangle curRect) {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return OrganismHolder.getOrganismHolder().getSquares().get(curRect).stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .findAny().orElse(null);
    }

    public static List<Rectangle> getSquares(Rectangle rectangle, Rectangle curRect) {
        return OrganismHolder.getOrganismHolder().getSquares().get(curRect).stream()
                .filter(sqr -> sqr.overlaps(rectangle))
                .collect(Collectors.toList());
    }

    public static boolean isMomentumChanged() {
        int random = MathUtils.random(0, MAX_CHANCE);
        return random > CHANCE_OF_MOMENTUM_CHANGE;
    }
}
