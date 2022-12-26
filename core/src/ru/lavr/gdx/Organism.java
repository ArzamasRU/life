package ru.lavr.gdx;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_MOMENTUM;
import static ru.lavr.gdx.constants.Constant.MOMENTUM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

public class Organism {
    private final Vector2 position = new Vector2();
    private final Texture texture;
    private final Rectangle rectangle = new Rectangle();

    private int momentum = 0;

    public Organism() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        Vector2 randomDirection = CommonUtils.getRandomPosition();
        position.set(randomDirection);
        rectangle.x = randomDirection.x;
        rectangle.y = randomDirection.y;
        rectangle.width = CELL_SIZE;
        rectangle.height = CELL_SIZE;
        Gdx.app.log("MyTag", position.x + " " + position.y);
    }

    public Organism(List<Organism> organisms) {
        Vector2 randomPosition;
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms));
        position.set(randomPosition);
        rectangle.x = randomPosition.x;
        rectangle.y = randomPosition.y;
        rectangle.width = CELL_SIZE;
        rectangle.height = CELL_SIZE;
    }

    public Organism(Vector2 position) {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        this.position.set(position);
        rectangle.x = position.x;
        rectangle.y = position.y;
        rectangle.width = CELL_SIZE;
        rectangle.height = CELL_SIZE;
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public void move(List<Organism> organisms, List<Organism> newOrganisms) {
        if (!CommonUtils.isFreeSpace(position, organisms, newOrganisms)) {
            return;
        }
        Vector2 randomPosition;
        do {
            if (isMomentumChanged()) {
                int randomDirection;
                randomDirection = CommonUtils.getRandomDirection();
                randomPosition = CommonUtils.getRandomDirection(position, randomDirection);
                momentum = randomDirection;
            } else {
                randomPosition = CommonUtils.getRandomDirection(position, momentum);
            }
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
                || CommonUtils.isNotValidDirection(randomPosition));
        position.set(randomPosition);
    }

    public Organism division(List<Organism> organisms, List<Organism> newOrganisms) {
        Vector2 randomPosition;
        int multiplier = 1;
        if (CommonUtils.isNotFreeSpace(position, organisms, newOrganisms, multiplier)) {
            return null;
        }
//        while (CommonUtils.isNotFreeSpace(position, organisms, newOrganisms, multiplier)) {
//            multiplier++;
//            if (STEP * multiplier > 30) {
//                return null;
//            }
//        }
        do {
            randomPosition = CommonUtils.getRandomDirection(position, CommonUtils.getRandomDirection(), multiplier);
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
                || CommonUtils.isNotValidDirection(randomPosition));
//        Gdx.app.log("MyTag 1", position.x + " " + position.y);
//        Gdx.app.log("MyTag 2", randomPosition.x + " " + randomPosition.y);
        return new Organism(randomPosition);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    private boolean isMomentumChanged() {
        if (momentum == 0) {
            return true;
        }
        int random = MathUtils.random(0, MAX_MOMENTUM);
        return random > MOMENTUM;
    }
}
