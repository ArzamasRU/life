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
        Vector2 randomPosition;
        do {
            if (isMomentumChanged()) {
                int randomDirection;
                randomDirection = CommonUtils.getRandomDirection();
                randomPosition = CommonUtils.getDirection(position, randomDirection);
                momentum = randomDirection;
            } else {
                randomPosition = CommonUtils.getDirection(position, momentum);
            }
        } while (isNotValidPosition(randomPosition, 1));
        position.set(randomPosition);
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
}
