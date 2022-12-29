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

    public Plant(List<Organism> plantOrganisms) {
        super(texture, plantOrganisms);
    }

    public Plant(Vector2 position, List<Organism> organisms, List<Organism> newOrganisms) {
        super(texture, position, organisms, newOrganisms);
    }

    public void division(List<Organism> newPlants) {
        Vector2 randomPosition;
        List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
        if (neighbors.size() >= 8) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (CommonUtils.isNotValidPosition(randomPosition, plants)
                || CommonUtils.isNotValidPosition(randomPosition, newPlants)
                || CommonUtils.isNotValidDirection(randomPosition));
        newPlants.add(new Plant(randomPosition, plants, newPlants));
    }
}
