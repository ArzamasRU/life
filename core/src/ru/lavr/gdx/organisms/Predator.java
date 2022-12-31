package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.PREDATOR_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.Constant.STEP_PREDATOR_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        fullness = 100;
    }

    public Predator(Vector2 position) {
        super(texture, position);
        fullness = 100;
    }

    @Override
    public void move() {
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        fullness -= STEP_EXHAUSTION;
        if (!reproduce()) {
            if (!eat()) {
                randomStep();
            }
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

    private boolean eat() {
        Integer herbivoreIndex = getCloseHerbivoreIndex();
        if (herbivoreIndex != null) {
//            если в remove() передавать Integer будет вызываться метод с remove(Object), а не remove(int)
            int index = herbivoreIndex;
            List<Organism> herbivores = OrganismHolder.getOrganismHolder().getHerbivores();
            herbivores.remove(index);
            if (fullness < MAX_FULLNESS) {
                fullness += STEP_PREDATOR_FULLNESS;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean reproduce() {
        if (fullness >= READY_FOR_DIVISION) {
            Vector2 randomPosition;
            if (CommonUtils.isNotFreeSpace(position)) {
                return false;
            }
//            do {
//                randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
//            } while (isNotValidPosition(randomPosition, 1));
            Integer randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position));
            randomPosition = CommonUtils.getDirection(position, randomDirection);
            List<Organism> newPredators = OrganismHolder.getOrganismHolder().getNewPredators();
            newPredators.add(new Predator(randomPosition));
            fullness -= PREDATOR_DIVISION_COST;
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getAvailableDirections(Vector2 position) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newPredators = organismHolder.getNewPredators();
        List<Organism> predators = organismHolder.getPredators();
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .filter(i -> {
                    Vector2 direction = CommonUtils.getDirection(position, i);
                    return CommonUtils.isValidPosition(direction, predators)
                            && CommonUtils.isValidPosition(direction, newPredators)
                            && CommonUtils.isValidDirection(direction);
                })
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public void die() {
    }
}
