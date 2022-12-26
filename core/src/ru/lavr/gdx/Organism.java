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
    }

    public Organism(List<Organism> organisms) {
        Vector2 randomPosition;
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (!CommonUtils.isValidPosition(randomPosition, organisms));
        position.set(randomPosition);
        rectangle.x = randomPosition.x;
        rectangle.y = randomPosition.y;
        rectangle.width = CELL_SIZE;
        rectangle.height = CELL_SIZE;
        Gdx.app.log("MyTag", randomPosition.x + " " + randomPosition.y);
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public void move(List<Organism> organisms) {
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
