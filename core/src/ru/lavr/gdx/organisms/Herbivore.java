package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.LifeConstants.CELL_SIZE;
import static ru.lavr.gdx.constants.LifeConstants.EAT_RANGE;
import static ru.lavr.gdx.constants.LifeConstants.HERBIVORE_DIVISION_COST;
import static ru.lavr.gdx.constants.LifeConstants.HERBIVORE_READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.LifeConstants.HERBIVORE_VISION;
import static ru.lavr.gdx.constants.LifeConstants.MAX_FULLNESS;
import static ru.lavr.gdx.constants.LifeConstants.START_HERBIVORE_FULLNESS;
import static ru.lavr.gdx.constants.LifeConstants.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.LifeConstants.STEP_HERBIVORE_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;
import java.util.Map;

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
        addToOrganismsMap();
        fullness = START_HERBIVORE_FULLNESS;
    }

    public Herbivore(Vector2 position) {
        super(texture, position);
        addToOrganismsMap();
        fullness = START_HERBIVORE_FULLNESS;
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
        if (!runAway()) {
            if (!reproduce()) {
                if (!eat()) {
                    randomStep();
                }
            }
        }
    }

    @Override
    public boolean reproduce() {
        if (fullness >= HERBIVORE_READY_FOR_DIVISION) {
            Vector2 randomPosition;
            if (CommonUtils.isNotFreeSpace(position, currSquare)) {
                return false;
            }
            Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position, currSquare));
            if (randomDirection != 0) {
                randomPosition = CommonUtils.getDirection(position, randomDirection);
                new Herbivore(randomPosition);
                fullness -= HERBIVORE_DIVISION_COST;
                return true;
            }
        }
        return false;
    }

    @Override
    public void die() {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        herbivoresMap.get(currSquare).remove(this);
        active = false;
    }

    @Override
    public void addToOrganismsMap() {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        currSquare.set(CommonUtils.getSquare(getUpdatedRectangle()));
        herbivoresMap.get(currSquare).add(this);
    }

    @Override
    public void updateOrganismsMap(Vector2 newPosition) {
        Rectangle newRect = CommonUtils.getSquare(newPosition, currSquare);
        if (!currSquare.equals(newRect)) {
            Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
            herbivoresMap.get(currSquare).remove(this);
            currSquare.set(newRect);
            herbivoresMap.get(currSquare).add(this);
        }
    }


    private Organism getClosePredator(int multiplier) {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        List<Organism> predators = CommonUtils.getOrganismsInSquare(position, currSquare, predatorsMap, multiplier);
        return CommonUtils.getCloseOrganism(position, predators, multiplier);
    }

    private Organism getClosePlant(int multiplier) {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        List<Organism> plants = CommonUtils.getOrganismsInSquare(position, currSquare, plantsMap, multiplier);
        return CommonUtils.getCloseOrganism(position, plants, multiplier);
    }

    private boolean eat() {
        Organism plant = getClosePlant(EAT_RANGE);
        if (plant != null) {
            plant.die();
            if (fullness < MAX_FULLNESS) {
                fullness += STEP_HERBIVORE_FULLNESS;
            }
            return true;
        }
        return false;
    }

    private boolean runAway() {
        Organism predator = getClosePredator(HERBIVORE_VISION);
        if (predator != null) {
            Vector2 prevPosition = position;
            Vector2 newPosition = CommonUtils.getPositionFrom(position, currSquare, predator.getPosition(), true);
            if (newPosition != null && !prevPosition.equals(newPosition)) {
                updateOrganismsMap(newPosition);
                position.set(newPosition);
                momentum.set(new Vector2(newPosition).sub(prevPosition));
            }
            return true;
        }
        return false;
    }
}
