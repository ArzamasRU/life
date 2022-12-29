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

    public Plant() {
        super(texture);
        Vector2 randomPosition;
        List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (CommonUtils.isNotValidPosition(randomPosition, plants));
        this.position = randomPosition;
        updateNeighbors(randomPosition);
    }

    public Plant(Vector2 position) {
        super(texture);
        this.position = position;
        updateNeighbors(position);
    }

    public void move(List<Organism> organisms, List<Organism> newOrganisms) {
//        if (!CommonUtils.isFreeSpace(position, organisms, newOrganisms)) {
//            return;
//        }
//        Vector2 randomPosition;
//        do {
//            if (isMomentumChanged()) {
//                int randomDirection;
//                randomDirection = CommonUtils.getRandomDirection();
//                randomPosition = CommonUtils.getDirection(position, randomDirection);
//                momentum = randomDirection;
//            } else {
//                randomPosition = CommonUtils.getDirection(position, momentum);
//            }
//        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
//                || CommonUtils.isNotValidDirection(randomPosition));
//        position.set(randomPosition);
    }

    public void division() {
        Vector2 randomPosition;
        if (neighbors.size() >= 8) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (isValidPosition(randomPosition, 1));
        List<Organism> newPlants = OrganismHolder.getOrganismHolder().getNewPlants();
        newPlants.add(new Plant(randomPosition));
    }

    @Override
    public boolean isValidPosition(Vector2 position, int multiplier) {
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
