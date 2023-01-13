package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.HERBIVORE_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.HERBIVORE_READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.START_HERBIVORE_FULLNESS;
import static ru.lavr.gdx.constants.Constant.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.Constant.STEP_HERBIVORE_FULLNESS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
        fullness -= STEP_EXHAUSTION;
        if (CommonUtils.isNotFreeSpace(position) || active == false) {
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

    private int getClosePredatorDirection() {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        List<Organism> closeOrganisms = CommonUtils.getCloseOrganisms(this.position, predatorsMap, 1);
        return CommonUtils.getCloseOrganismPosition(position, closeOrganisms);
    }

    private Organism getClosePredator() {
        List<Organism> predators = OrganismHolder.getOrganismHolder().getPredators();
        return CommonUtils.getCloseOrganism(position, predators, 1);
    }

    private Integer getClosePlantIndex() {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        List<Organism> closeOrganisms = CommonUtils.getCloseOrganisms(this.position, plantsMap, 1);
        return CommonUtils.getCloseOrganismIndex(position, closeOrganisms);
    }

    private Organism getClosePlant() {
        Map<Rectangle, List<Organism>> plantsMap = OrganismHolder.getOrganismHolder().getPlantsMap();
        List<Organism> plants = CommonUtils.getCloseOrganisms(this.position, plantsMap, 1);
        return CommonUtils.getCloseOrganism(position, plants);
    }

    private boolean eat() {
        Organism plant = getClosePlant();
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
        Organism predator = getClosePredator();
        if (predator != null) {
            Vector2 prevPosition = position;
            Vector2 newPosition = CommonUtils.getPositionFrom(position, predator.getPosition(), true, 1);
            if (newPosition != null && !prevPosition.equals(newPosition)) {
                updateOrganismsMap(newPosition);
                position.set(newPosition);
                momentum.set(new Vector2(newPosition).sub(prevPosition));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean reproduce() {
        if (fullness >= HERBIVORE_READY_FOR_DIVISION) {
            Vector2 randomPosition;
            if (CommonUtils.isNotFreeSpace(position)) {
                return false;
            }
            Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position));
            if (randomDirection != 0) {
                randomPosition = CommonUtils.getDirection(position, randomDirection);
                new Herbivore(randomPosition);
            }
            fullness -= HERBIVORE_DIVISION_COST;
            return true;
        }
        return false;
    }

    @Override
    public void die() {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        herbivoresMap.get(CommonUtils.getSquare(getUpdatedRectangle())).remove(this);
        active = false;
    }

    @Override
    public void addToOrganismsMap() {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        herbivoresMap.get(CommonUtils.getSquare(getUpdatedRectangle())).add(this);
    }

    @Override
    public void updateOrganismsMap(Vector2 newPosition) {
        Rectangle currentRect = CommonUtils.getSquare(getUpdatedRectangle());
        Rectangle newRect = CommonUtils.getSquare(newPosition);
        if (!currentRect.equals(newRect)) {
            Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
            herbivoresMap.get(currentRect).remove(this);
            herbivoresMap.get(newRect).add(this);
        }
    }
}
