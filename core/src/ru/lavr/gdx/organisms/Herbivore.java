package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.HERBIVORE_DIVISION_COST;
import static ru.lavr.gdx.constants.Constant.MAX_FULLNESS;
import static ru.lavr.gdx.constants.Constant.READY_FOR_DIVISION;
import static ru.lavr.gdx.constants.Constant.STEP_EXHAUSTION;
import static ru.lavr.gdx.constants.Constant.STEP_HERBIVORE_FULLNESS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        fullness = 100;
    }

    public Herbivore(Vector2 position) {
        super(texture, position);
        fullness = 100;
    }

    @Override
    public void move() {
        if (CommonUtils.isNotFreeSpace(position)) {
            return;
        }
        fullness -= STEP_EXHAUSTION;
//        Gdx.app.log("MyTag", String.valueOf(fullness));
        if (!runAway()) {
            if (!reproduce()) {
                if (!eat()) {
                    randomStep();
                }
            }
        }
    }

    private int getClosePredatorDirection() {
        List<Organism> predators = OrganismHolder.getOrganismHolder().getPredators();
        return CommonUtils.getCloseOrganismPosition(position, predators);
    }

    private Organism getClosePredator() {
        List<Organism> predators = OrganismHolder.getOrganismHolder().getPredators();
        return CommonUtils.getCloseOrganism(position, predators);
    }

    private Integer getClosePlantIndex() {
        List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
        return CommonUtils.getCloseOrganismIndex(position, plants);
    }

    private boolean eat() {
        Integer plantIndex = getClosePlantIndex();
        if (plantIndex != null) {
//            если в remove() передавать Integer будет вызываться метод с remove(Object), а не remove(int)
            int index = plantIndex;
            List<Organism> plants = OrganismHolder.getOrganismHolder().getPlants();
            Organism plant = plants.get(index);
            plant.die();
            plants.remove(index);
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
            Vector2 oppositePosition = CommonUtils.getOppositePosition(position, predator.getPosition());
            position.set(oppositePosition);
//            momentum = oppositeDirection;
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
            List<Organism> newHerbivores = OrganismHolder.getOrganismHolder().getNewHerbivores();
            newHerbivores.add(new Herbivore(randomPosition));
            fullness -= HERBIVORE_DIVISION_COST;
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getAvailableDirections(Vector2 position) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newHerbivores = organismHolder.getNewHerbivores();
        List<Organism> herbivores = organismHolder.getHerbivores();
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .filter(i -> {
                    Vector2 direction = CommonUtils.getDirection(position, i);
                    return CommonUtils.isValidPosition(direction, herbivores)
                            && CommonUtils.isValidPosition(direction, newHerbivores)
                            && CommonUtils.isValidDirection(direction);
                })
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public void die() {
    }
}
