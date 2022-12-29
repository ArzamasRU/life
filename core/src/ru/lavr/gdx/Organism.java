package ru.lavr.gdx;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_MOMENTUM;
import static ru.lavr.gdx.constants.Constant.MOMENTUM;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class Organism {
    private final List<Organism> neighbors = new ArrayList<>();
    //    каждый раз перед использованием назначать x и y
    private final Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);

    private Vector2 position = new Vector2();
    private Texture texture;
    private boolean outOfBorder;
    public boolean active = true;
    private int momentum = 0;

    public Organism() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, CELL_SIZE, CELL_SIZE);
        texture = new Texture(pixmap);
        Vector2 randomDirection = CommonUtils.getRandomPosition();
        position.set(randomDirection);
    }

    public Organism(boolean outOfBorder) {
        this.outOfBorder = true;
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

    public void move(List<Organism> organisms, List<Organism> newOrganisms, SpriteBatch batch) {
        if (!CommonUtils.isFreeSpace(position, organisms, newOrganisms)) {
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
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
                || CommonUtils.isNotValidDirection(randomPosition));
        position.set(randomPosition);
    }

    public Organism division(List<Organism> organisms, List<Organism> newOrganisms, Batch batch) {
        if (neighbors.size() >= 8) {
            return null;
        }
        Vector2 randomPosition;
        int multiplier = 1;
//        if (CommonUtils.isNotFreeSpace(position, neighbors, newOrganisms, multiplier)) {
//            fullySurrounded = true;
//            return null;
//        }

////        while (CommonUtils.isNotFreeSpace(position, organisms, newOrganisms, multiplier)) {
//            multiplier++;
//            if (STEP * multiplier > 30) {
//                return null;
//            }
//        }
        do {
            randomPosition = CommonUtils.getDirection(position, CommonUtils.getRandomDirection(), multiplier);
        } while (CommonUtils.isNotValidPosition(randomPosition,
//                organisms.stream().filter(Organism::isActive).collect(Collectors.toList())
                organisms
        )
                || CommonUtils.isNotValidPosition(randomPosition, newOrganisms)
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

    public List<Organism> getNeighbors() {
        return neighbors;
    }

    public Rectangle getUpdatedRectangle() {
        this.rectangle.x = this.position.x;
        this.rectangle.y = this.position.y;
        return this.rectangle;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Organism{" +
                "position=" + position +
                ", rectangle=" + rectangle +
                '}';
    }
}
