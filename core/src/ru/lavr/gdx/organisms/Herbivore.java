package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

public class Herbivore extends Organism {
    private static final Pixmap pixmap;
    private static final Texture texture;

    static {
        pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.FOREST);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
    }

    public Herbivore() {
        super(texture);
    }

    public Herbivore(Vector2 position) {
        super(texture, position);
    }

    @Override
    public void move() {
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        int predatorPosition = getClosePredator();
        if (predatorPosition != 0) {
            int oppositeDirection = CommonUtils.getOppositeDirection(position, predatorPosition);
            position.set(CommonUtils.getDirection(position, oppositeDirection, 1));
            momentum = oppositeDirection;
            return;
        }
        Integer plantIndex = getClosePlantIndex();
        if (plantIndex != null) {
            eatOrganism(plantIndex);
            return;
        }
        randomStep();
    }

    private int getClosePredator() {
        List<Organism> predators = OrganismHolder.getOrganismHolder().getPredators();
        return CommonUtils.getCloseOrganismPosition(position, predators);
    }

    private Integer getClosePlantIndex() {
        List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
        return CommonUtils.getCloseOrganismIndex(position, plants);
    }

    private void eatOrganism(int index) {
        List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
        Organism plant = plants.get(index);
        plant.die();
        plants.remove(index);
    }

    @Override
    public void division() {
        Vector2 randomPosition;
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (isNotValidPosition(randomPosition, 1));
        List<Organism> newHerbivores = OrganismHolder.getOrganismHolder().getNewHerbivores();
        newHerbivores.add(new Herbivore(randomPosition));
    }

    @Override
    public void die() {
    }
}
