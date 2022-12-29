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
    protected int momentum = 0;

    public Organism(boolean outOfBorder) {
        this.active = false;
        this.outOfBorder = outOfBorder;
    }

    public Organism(Texture texture) {
        this.texture = texture;
        Vector2 randomPosition;
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (isNotValidPosition(randomPosition, 1));
        this.position = randomPosition;
    }

    public Organism(Texture texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
    }

    public abstract void move();

    public abstract void division();

    public boolean isNotValidPosition(Vector2 position, int multiplier) {
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

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    protected void randomStep() {
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

    protected boolean isMomentumChanged() {
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
