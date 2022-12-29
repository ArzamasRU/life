package ru.lavr.gdx.organisms;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_MOMENTUM;
import static ru.lavr.gdx.constants.Constant.MOMENTUM;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Organism {
    protected final List<Organism> neighbors = new ArrayList<>();
    //    каждый раз перед использованием назначать x и y
    private final Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);

    protected Vector2 position = new Vector2();
    private Texture texture;
    private boolean outOfBorder;
    public boolean active = true;
    private int momentum = 0;

    public Organism(boolean outOfBorder) {
        this.active = false;
        this.outOfBorder = outOfBorder;
    }

    public Organism(Texture texture, List<Organism> plantOrganisms) {
        Vector2 randomPosition;
        this.texture = texture;
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (CommonUtils.isNotValidPosition(randomPosition, plantOrganisms));
        position.set(randomPosition);
        List<Organism> neighbors = CommonUtils.getNeighbors(this.position, plantOrganisms, null);
        neighbors.stream()
                .filter(Organism::isNotOutOfBorder)
                .map(Organism::getNeighbors)
                .forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
    }

    public Organism(Texture texture, Vector2 position, List<Organism> plantOrganisms, List<Organism> newPlants) {
        this.texture = texture;
        this.position.set(position);
        List<Organism> neighbors = CommonUtils.getNeighbors(this.position, plantOrganisms, newPlants);
        neighbors.stream()
                .filter(Organism::isNotOutOfBorder)
                .map(Organism::getNeighbors)
                .forEach(ns -> ns.add(this));
        this.neighbors.addAll(neighbors);
    }

    public Organism(Texture texture, List<Organism> herbivoreOrganisms, List<Organism> predatorOrganisms) {
        Vector2 randomPosition;
        this.texture = texture;
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (CommonUtils.isNotValidPosition(randomPosition, herbivoreOrganisms)
                || CommonUtils.isNotValidPosition(randomPosition, predatorOrganisms));
        position.set(randomPosition);
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
                randomPosition = CommonUtils.getDirection(position, randomDirection);
                momentum = randomDirection;
            } else {
                randomPosition = CommonUtils.getDirection(position, momentum);
            }
        } while (CommonUtils.isNotValidPosition(randomPosition, organisms)
                || CommonUtils.isNotValidDirection(randomPosition));
        position.set(randomPosition);
    }

    public abstract void division();

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

    public boolean isOutOfBorder() {
        return outOfBorder;
    }

    public boolean isNotOutOfBorder() {
        return !outOfBorder;
    }

    @Override
    public String toString() {
        return "Organism{" +
                "position=" + position +
                ", rectangle=" + rectangle +
                '}';
    }
}
