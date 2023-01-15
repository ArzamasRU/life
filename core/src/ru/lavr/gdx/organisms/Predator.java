package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.EAT_RANGE;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.PREDATOR_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.PREDATOR_READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.PREDATOR_VISION;
import static ru.lavr.gdx.constants.Constant.START_PREDATOR_FULLNESS;
import static ru.lavr.gdx.constants.Constant.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.Constant.STEP_PREDATOR_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;
import java.util.Map;

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
        addToOrganismsMap();
        fullness = START_PREDATOR_FULLNESS;
    }

    public Predator(Vector2 position) {
        super(texture, position);
        addToOrganismsMap();
        fullness = START_PREDATOR_FULLNESS;
    }

    @Override
    public void move() {
        if (active == false) {
            return;
        }
        fullness -= STEP_EXHAUSTION;
        if (CommonUtils.isNotFreeSpace(position, currSquare)) {
            eat();
            return;
        }
        if (!reproduce()) {
            if (!eat()) {
                if (!follow()) {
                    randomStep();
                }
            }
        }
    }

    @Override
    public boolean reproduce() {
        if (fullness >= PREDATOR_READY_FOR_DIVISION) {
            Vector2 randomPosition;
            if (CommonUtils.isNotFreeSpace(position, currSquare)) {
                return false;
            }
            Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position, currSquare));
            if (randomDirection != 0) {
                randomPosition = CommonUtils.getDirection(position, randomDirection);
                new Predator(randomPosition);
                fullness -= PREDATOR_DIVISION_COST;
                return true;
            }
        }
        return false;
    }

    @Override
    public void die() {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        predatorsMap.get(currSquare).remove(this);
        active = false;
    }

    @Override
    public void addToOrganismsMap() {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        currSquare.set(CommonUtils.getSquare(getUpdatedRectangle()));
        predatorsMap.get(currSquare).add(this);
    }

    @Override
    public void updateOrganismsMap(Vector2 newPosition) {
        Rectangle newRect = CommonUtils.getSquare(newPosition, currSquare);
        if (!currSquare.equals(newRect)) {
            Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
            predatorsMap.get(currSquare).remove(this);
            currSquare.set(newRect);
            predatorsMap.get(currSquare).add(this);
        }
    }

    private Organism getCloseHerbivore(int multiplier) {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        List<Organism> herbivores = CommonUtils.getOrganismsInSquare(position, currSquare, herbivoresMap, multiplier);
        return CommonUtils.getCloseOrganism(position, herbivores, multiplier);
    }

    private boolean eat() {
        Organism herbivore = getCloseHerbivore(EAT_RANGE);
        if (herbivore != null) {
            herbivore.die();
            if (fullness < MAX_FULLNESS) {
                fullness += STEP_PREDATOR_FULLNESS;
            }
            return true;
        }
        return false;
    }

    private boolean follow() {
        Organism herbivore = getCloseHerbivore(PREDATOR_VISION);
        if (herbivore != null) {
            Vector2 newPosition = CommonUtils.getPositionFrom(position, currSquare, herbivore.getPosition(), false);
            if (newPosition != null) {
                updateOrganismsMap(newPosition);
                position.set(newPosition);
            }
            return true;
        }
        return false;
    }
}
