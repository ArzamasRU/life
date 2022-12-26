package ru.lavr.gdx;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.List;

public class Organism {
    private final Vector2 position = new Vector2();
    private final Texture texture;

    private final Rectangle rectangle = new Rectangle();

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
            randomPosition = CommonUtils.getRandomDirection(position);
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
                || CommonUtils.isNotValidDirection(randomPosition));
        position.set(randomPosition);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
