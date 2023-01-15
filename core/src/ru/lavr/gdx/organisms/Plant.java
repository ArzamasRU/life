package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.PLANT_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.PLANT_READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.START_PLANT_FULLNESS;
import static ru.lavr.gdx.constants.Constant.STEP_PLANT_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Plant extends Organism {
    private static final Pixmap pixmap;
    private static final Texture texture;

    static {
        pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.CHARTREUSE);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
    }

    public Plant(boolean outOfBorder) {
        super(outOfBorder);
    }

    public Plant() {
        super(texture);
        addToOrganismsMap();
        updateNeighbors(position);
        fullness = START_PLANT_FULLNESS;
    }

    public Plant(Vector2 position) {
        super(texture, position);
        addToOrganismsMap();
        updateNeighbors(position);
        fullness = START_PLANT_FULLNESS;
    }

    @Override
    public boolean reproduce() {
        Vector2 randomPosition;
        if (neighbors.size() >= 8) {
            return false;
        }
        Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position, currSquare));
        if (randomDirection != 0) {
            randomPosition = CommonUtils.getDirection(position, randomDirection);
            new Plant(randomPosition);
            fullness -= PLANT_DIVISION_COST;
            return true;
        }
        return false;
    }

    @Override
    public void move() {
        if (active == false) {
            return;
        }
        if (fullness < MAX_FULLNESS) {
            fullness += STEP_PLANT_FULLNESS;
        }
        if (fullness >= PLANT_READY_FOR_DIVISION) {
            reproduce();
        }
    }

    @Override
    public void die() {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        plantsMap.get(currSquare).remove(this);
        getNeighbors().forEach(neighbor -> neighbor.getNeighbors().remove(this));
        active = false;
    }

    @Override
    public boolean isNotValidPosition(Vector2 position, int multiplier) {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        List<Organism> plants = plantsMap.get(CommonUtils.getSquare(position));
        return CommonUtils.isNotValidDirection(position)
                || CommonUtils.isNotValidPosition(position, plants);
    }

    @Override
    public List<Integer> getAvailableDirections(Vector2 position, Rectangle curRect ) {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .filter(i -> {
                    Vector2 direction = CommonUtils.getDirection(position, i);
                    return CommonUtils.isValidDirection(direction)
                            && CommonUtils.isValidPosition(direction, getNeighbors())
                            && CommonUtils.isValidPosition(direction, plantsMap.get(CommonUtils.getSquare(direction, curRect)));
                })
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public void addToOrganismsMap() {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        currSquare.set(CommonUtils.getSquare(getUpdatedRectangle()));
        plantsMap.get(currSquare).add(this);
    }

    @Override
    public void updateOrganismsMap(Vector2 newPosition) {
    }

    private void updateNeighbors(Vector2 position) {
        List<Organism> neighbors = CommonUtils.getNeighbors(position, currSquare);
        neighbors.stream()
                .filter(Organism::isNotOutOfBorder)
                .map(Organism::getNeighbors)
                .forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
    }
}
