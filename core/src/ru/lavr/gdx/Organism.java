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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Organism {
    private final Vector2 position = new Vector2();
    private final Texture texture;
    private final Rectangle rectangle = new Rectangle();

    private boolean fullySurrounded;
    private final Set<Organism> neighbors = new HashSet<>();

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
        List<Organism> neighbors = CommonUtils.getNeighbors(this.position, organisms, null);
        neighbors.stream().map(Organism::getNeighbors).forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
    }

    public Organism(Vector2 position, List<Organism> organisms, List<Organism> newOrganisms) {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        this.position.set(position);
        rectangle.x = position.x;
        rectangle.y = position.y;
        rectangle.width = CELL_SIZE;
        rectangle.height = CELL_SIZE;
        List<Organism> neighbors = CommonUtils.getNeighbors(this.position, organisms, newOrganisms);
        neighbors.stream().map(Organism::getNeighbors).forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
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
        if (fullySurrounded) {
            return null;
        }
        if (neighbors.size() >= 9) {
            fullySurrounded = true;
            return null;
        }
        Vector2 randomPosition;
        int multiplier = 1;
        if (CommonUtils.isNotFreeSpace(position, new ArrayList<>(neighbors), newOrganisms, multiplier)) {
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
        } while (CommonUtils.isNotValidPosition(randomPosition, new ArrayList<>(neighbors))
                || CommonUtils.isNotValidDirection(randomPosition));
        return new Organism(randomPosition, organisms, newOrganisms);
    }

    private boolean isMomentumChanged() {
        if (momentum == 0) {
            return true;
        }
        int random = MathUtils.random(0, MAX_MOMENTUM);
        return random > MOMENTUM;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Set<Organism> getNeighbors() {
        return neighbors;
    }

    public boolean isFullySurrounded() {
        return fullySurrounded;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organism organism = (Organism) o;

        return position.equals(organism.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
