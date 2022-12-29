package ru.lavr.gdx.utils;

import static ru.lavr.gdx.constants.Constant.BOTTOM_EDGE;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.LEFT_EDGE;
import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.STEP;
import static ru.lavr.gdx.constants.Constant.UPPER_EDGE;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.organisms.OrganismHolder;
import ru.lavr.gdx.organisms.Plant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonUtils {
    private final static Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);
    private final static Rectangle field = new Rectangle(LEFT_EDGE, BOTTOM_EDGE, RIGHT_EDGE, UPPER_EDGE);
    private final static Rectangle neighborsRectangle = new Rectangle(0, 0, CELL_SIZE * 3, CELL_SIZE * 3);

    public static Vector2 getRandomPosition() {
        Vector2 vector2 = new Vector2();
        float x = MathUtils.random(LEFT_EDGE / CELL_SIZE, RIGHT_EDGE / CELL_SIZE);
        float y = MathUtils.random(BOTTOM_EDGE / CELL_SIZE, UPPER_EDGE / CELL_SIZE);
        return vector2.set(x * CELL_SIZE, y * CELL_SIZE);
    }

    public static int getRandomDirection() {
        return MathUtils.random(1, 9);
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
        } else if (direction == 5) {
            return vector2.add(0, 0);
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
                .map(Organism::getUpdatedRectangle)
                .noneMatch(rectangle::overlaps);
    }

    //    проверяет не заходит ли клетка за поля
    public static boolean isValidDirection(Vector2 vector2) {
        rectangle.x = vector2.x;
        rectangle.y = vector2.y;
        return field.overlaps(rectangle);
    }

    public static boolean isNotValidPosition(Vector2 vector2, List<Organism> organisms) {
        return !isValidPosition(vector2, organisms);
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

    public static boolean isNotFreeSpace(Vector2 position) {
        return !isFreeSpace(position, 1);
    }

    public static boolean isNotFreeSpace(Vector2 position, int multiplier) {
        return !isFreeSpace(position, multiplier);
    }

    public static List<Organism> getNeighbors(Vector2 position) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newPlants = organismHolder.getNewPlants();
        List<Organism> plants = organismHolder.getPlants();
        Vector2 vector2 = new Vector2(position);
        vector2.add(-STEP, -STEP);
        neighborsRectangle.x = vector2.x;
        neighborsRectangle.y = vector2.y;
        List<Organism> neighbors = plants.stream()
                .filter(organism -> neighborsRectangle.overlaps(organism.getUpdatedRectangle()))
                .collect(Collectors.toList());
        if (!newPlants.isEmpty()) {
            neighbors.addAll(newPlants.stream()
                    .filter(organism -> neighborsRectangle.overlaps(organism.getUpdatedRectangle()))
                    .collect(Collectors.toList()));
        }
        IntStream.range(1, 10)
                .mapToObj(i -> CommonUtils.getDirection(position, i))
                .filter(CommonUtils::isNotValidDirection)
                .forEach(outOfBorder -> neighbors.add(new Plant(true)));
        return neighbors;
    }

    private long updateNeighbors(Vector2 position) {
        return IntStream.range(1, 10)
                .mapToObj(i -> CommonUtils.getDirection(position, i))
                .filter(CommonUtils::isNotValidDirection)
                .count();
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
        plants.addAll(newPlants);
        herbivores.addAll(newHerbivores);
        predators.addAll(newPredators);
        newPlants.clear();
        newHerbivores.clear();
        newPredators.clear();
    }
}
