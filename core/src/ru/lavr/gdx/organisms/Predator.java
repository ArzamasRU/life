package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

public class Predator extends Organism {
    private static final Pixmap pixmap;
    private static final Texture texture;
    static {
        pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0,0,CELL_SIZE,CELL_SIZE);
        texture = new Texture(pixmap);
    }

//    public Predator(List<Organism> herbivoreOrganisms, List<Organism> predatorOrganisms) {
//        super(texture, herbivoreOrganisms, predatorOrganisms);
//    }

//    public Predator(Vector2 position, List<Organism> organisms, List<Organism> newOrganisms) {
//        super(texture, position, organisms, newOrganisms);
//    }

    @Override
    public void move(List<Organism> organisms, List<Organism> newOrganisms) {

    }

    @Override
    public void division() {
//        Vector2 randomPosition;
//        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
//        List<Organism> newPredators = organismHolder.getNewPredators();
//        List<Organism> predators = organismHolder.getPredators();
//        if (CommonUtils.isNotFreeSpace(position)) {
//            return;
//        }
//        do {
//            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection());
//        } while (CommonUtils.isNotValidPosition(randomPosition, predators)
//                || CommonUtils.isNotValidPosition(randomPosition, newPredators)
//                || CommonUtils.isNotValidPosition(randomPosition, newasdors)
//                || CommonUtils.isNotValidPosition(randomPosition, newPredasds)
//                || CommonUtils.isNotValidDirection(randomPosition));
//        newPredators.add(new Predator(randomPosition, predators, newPredators));
    }
}
