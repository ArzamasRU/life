package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

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

    @Override
    public void move() {
    }

    public Plant() {
        super(texture);
        updateNeighbors(this.position);
    }

    public Plant(Vector2 position) {
        super(texture, position);
        updateNeighbors(this.position);
    }

    public void division() {
        Vector2 randomPosition;
        if (neighbors.size() >= 8) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (isNotValidPosition(randomPosition, 1));
        List<Organism> newPlants = OrganismHolder.getOrganismHolder().getNewPlants();
        newPlants.add(new Plant(randomPosition));
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
