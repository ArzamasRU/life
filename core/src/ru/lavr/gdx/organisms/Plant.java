package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.PLANT_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.STEP_PLANT_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

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
        updateNeighbors(this.position);
    }

    public Plant(Vector2 position) {
        super(texture, position);
        updateNeighbors(this.position);
    }

    @Override
    public boolean reproduce() {
        Vector2 randomPosition;
        if (neighbors.size() >= 8) {
            return false;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (isNotValidPosition(randomPosition, 1));
        List<Organism> newPlants = OrganismHolder.getOrganismHolder().getNewPlants();
        newPlants.add(new Plant(randomPosition));
        fullness -= PLANT_DIVISION_COST;
        return true;
    }

    @Override
    public void move() {
        if (fullness < MAX_FULLNESS) {
            fullness += STEP_PLANT_FULLNESS;
        }
        if (fullness >= READY_FOR_DIVISION) {
            reproduce();
        }
    }

    @Override
    public void die() {
        getNeighbors().forEach(neighbor -> neighbor.getNeighbors().remove(this));
    }

    @Override
    public boolean isNotValidPosition(Vector2 position, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newPlants = organismHolder.getNewPlants();
        List<Organism> plants = organismHolder.getPlants();
        return CommonUtils.isNotValidPosition(position, plants)
                || CommonUtils.isNotValidPosition(position, newPlants)
                || CommonUtils.isNotValidDirection(position);
    }

    private void updateNeighbors(Vector2 position) {
        List<Organism> neighbors = CommonUtils.getNeighbors(position);
        neighbors.stream()
                .filter(Organism::isNotOutOfBorder)
                .map(Organism::getNeighbors)
                .forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
    }
}
