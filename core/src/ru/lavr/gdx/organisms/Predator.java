package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

public class Predator extends Organism {
    private static final Pixmap pixmap;
    private static final Texture texture;

    static {
        pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
    }

    public Predator() {
        super(texture);
    }

    public Predator(Vector2 position) {
        super(texture, position);
    }

    @Override
    public void move() {
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        Integer herbivoreIndex = getCloseHerbivoreIndex();
        if (herbivoreIndex != null) {
            eatOrganism(herbivoreIndex);
            return;
        }
//        int herbivorePosition = getCloseHerbivoreFollow();
//        if (herbivorePosition != 0) {
//            momentum = herbivorePosition;
//            return;
//        }
        randomStep();
    }

    private Integer getCloseHerbivoreIndex() {
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        return CommonUtils.getCloseOrganismIndex(position, herbivores);
    }

    private int getCloseHerbivoreFollow() {
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        return CommonUtils.getCloseOrganismPosition(position, herbivores);
    }

    private void eatOrganism(int index) {
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        herbivores.remove(index);
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
        List<Organism> newPredators = OrganismHolder.getOrganismHolder().getNewPredators();
        newPredators.add(new Predator(randomPosition));
    }

    @Override
    public void die() {
    }
}
