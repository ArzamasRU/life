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
        pixmap.fillRectangle(0,0,CELL_SIZE,CELL_SIZE);
        texture = new Texture(pixmap);
    }

    public Herbivore(List<Organism> herbivoreOrganisms, List<Organism> predatorOrganisms) {
        super(texture, herbivoreOrganisms, predatorOrganisms);
    }

    public Herbivore(Vector2 position, List<Organism> organisms, List<Organism> newOrganisms) {
        super(texture, position, organisms, newOrganisms);
    }

    @Override
    public void division() {
        Vector2 randomPosition;
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newHerbivores = organismHolder.getNewHerbivores();
        List<Organism> newPredators = organismHolder.getNewPredators();
        List<Organism> herbivores = organismHolder.getHerbivores();
        List<Organism> predators = organismHolder.getPredators();
        if (CommonUtils.isNotFreeSpace(position, newHerbivores)) {
            return;
        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
        } while (CommonUtils.isNotValidPosition(randomPosition, herbivores)
                || CommonUtils.isNotValidPosition(randomPosition, newHerbivores)
                || CommonUtils.isNotValidDirection(randomPosition));
        newHerbivores.add(new Herbivore(randomPosition, herbivores, newHerbivores));
    }
}
