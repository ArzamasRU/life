package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.PREDATOR_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.PREDATOR_READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.PREDATOR_VISION;
import static ru.lavr.gdx.constants.Constant.START_PREDATOR_FULLNESS;
import static ru.lavr.gdx.constants.Constant.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.Constant.STEP_PREDATOR_FULLNESS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
        if (CommonUtils.isNotFreeSpace(position) || active == false) {
            return;
        }
        fullness -= STEP_EXHAUSTION;
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
            if (CommonUtils.isNotFreeSpace(position)) {
                return false;
            }
            Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position));
            randomPosition = CommonUtils.getDirection(position, randomDirection);
            Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
            predatorsMap.get(CommonUtils.getSquare(randomPosition)).add(new Predator(randomPosition));
            fullness -= PREDATOR_DIVISION_COST;
            return true;
        }
        return false;
    }

    @Override
    public void die() {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        predatorsMap.get(CommonUtils.getSquare(getUpdatedRectangle())).remove(this);
        active = false;
    }

    @Override
    public void addToOrganismsMap() {
        Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
        predatorsMap.get(CommonUtils.getSquare(getUpdatedRectangle())).add(this);
    }

    @Override
    public void updateOrganismsMap(Vector2 newPosition) {
        Rectangle currentRect = CommonUtils.getSquare(getUpdatedRectangle());
        Rectangle newRect = CommonUtils.getSquare(newPosition);
        if (!currentRect.equals(newRect)) {
            Map<Rectangle, List<Organism>> predatorsMap = OrganismHolder.getOrganismHolder().getPredatorsMap();
            predatorsMap.get(currentRect).remove(this);
            predatorsMap.get(newRect).add(this);
        }
    }

    private Integer getCloseHerbivoreIndex() {
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        return CommonUtils.getCloseOrganismIndex(position, herbivores);
    }

    private int getCloseHerbivoreFollow() {
        List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
        return CommonUtils.getCloseOrganismPosition(position, herbivores);
    }

    private Organism getCloseHerbivore(int multiplier) {
        Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
        List<Organism> herbivores = CommonUtils.getCloseOrganisms(position, herbivoresMap, multiplier);
        return CommonUtils.getCloseOrganism(position, herbivores, multiplier);
    }

    private boolean eat() {
        Organism herbivore = getCloseHerbivore(1);
        if (herbivore != null) {
            Gdx.app.log("eat ", this + " " + herbivore);
//            Map<Rectangle, List<Organism>> herbivoresMap = OrganismHolder.getOrganismHolder().getHerbivoresMap();
//            Gdx.app.log("eat ", this + " " + herbivore);
//            herbivoresMap.values().stream().flatMap(List::stream)
//                    .forEach(org -> Gdx.app.log("org ", String.valueOf(org)));
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
            Gdx.app.log("follow ", this + " " + herbivore);
            Vector2 newPosition = CommonUtils.getPositionFrom(position, herbivore.getPosition(), false, 1);
            updateOrganismsMap(newPosition);
            position.set(newPosition);
            return true;
        }
        return false;
    }
}
