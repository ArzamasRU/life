package ru.lavr.gdx.organisms;

import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.MAX_MOMENTUM;
import static ru.lavr.gdx.constants.Constant.MOMENTUM;

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

    protected Vector2 position;
    private Texture texture;
    private boolean outOfBorder;
    public boolean active = true;
    private int momentum = 0;

    public Organism() {
    }

    public Organism(boolean outOfBorder) {
        this.active = false;
        this.outOfBorder = outOfBorder;
    }

    public Organism(Texture texture) {
        this.texture = texture;
    }

//    public Organism(Texture texture, List<Organism> plantOrganisms) {
//        Vector2 randomPosition;
//        this.texture = texture;
//        do {
//            randomPosition = CommonUtils.getRandomPosition();
//        } while (CommonUtils.isNotValidPosition(randomPosition, plantOrganisms));
//        position.set(randomPosition);
//        List<Organism> neighbors = CommonUtils.getNeighbors(this.position, plantOrganisms, null);
//        neighbors.stream()
//                .filter(Organism::isNotOutOfBorder)
//                .map(Organism::getNeighbors)
//                .forEach(ns -> ns.add(this));
//        this.neighbors.addAll(neighbors);
//    }

//    public Organism(Texture texture, List<Organism> herbivoreOrganisms, List<Organism> predatorOrganisms) {
//        Vector2 randomPosition;
//        this.texture = texture;
//        do {
//            randomPosition = CommonUtils.getRandomPosition();
//        } while (CommonUtils.isNotValidPosition(randomPosition, herbivoreOrganisms)
//                || CommonUtils.isNotValidPosition(randomPosition, predatorOrganisms));
//        position.set(randomPosition);
//    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public abstract void move(List<Organism> organisms, List<Organism> newOrganisms);

    public boolean isValidPosition(Vector2 position, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> newHerbivores = organismHolder.getNewHerbivores();
        List<Organism> newPredators = organismHolder.getNewPredators();
        List<Organism> herbivores = organismHolder.getHerbivores();
        List<Organism> predators = organismHolder.getPredators();
        return CommonUtils.isNotValidPosition(position, herbivores)
                || CommonUtils.isNotValidPosition(position, newHerbivores)
                || CommonUtils.isNotValidPosition(position, predators)
                || CommonUtils.isNotValidPosition(position, newPredators)
                || CommonUtils.isNotValidDirection(position);
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
