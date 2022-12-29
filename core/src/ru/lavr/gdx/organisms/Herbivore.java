package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
        Vector2 randomPosition;
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        List<Organism> predators = OrganismHolder.getOrganismHolder().getPredators();
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (CommonUtils.isNotValidPosition(randomPosition, herbivores)
                || CommonUtils.isNotValidPosition(randomPosition, predators));
        this.position = randomPosition;
    }

    public Herbivore(Vector2 position) {

        super(texture);
    }

    @Override
    public void move(List<Organism> organisms, List<Organism> newOrganisms) {

    }

    @Override
    public void division() {
        Vector2 randomPosition;
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (isValidPosition(randomPosition, 1));
        List<Organism> newHerbivores = OrganismHolder.getOrganismHolder().getNewHerbivores();
        newHerbivores.add(new Herbivore(randomPosition));
    }
}
