package ru.lavr.gdx.organisms;

import static ru.lavr.gdx.constants.Constant.CELL_SIZE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Organism {
    protected final List<Organism> neighbors = new ArrayList<>();
    //    каждый раз перед использованием назначать x и y
    private final Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);

    protected Vector2 position;
    private Texture texture;
    private boolean outOfBorder;
    public boolean active = true;
    protected final Vector2 momentum = new Vector2();
    protected int fullness = 0;

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

    public abstract boolean reproduce();

    public abstract void die();

    public abstract void addToOrganismsMap();

    public abstract void updateOrganismsMap(Vector2 newPosition);

    public boolean isNotValidPosition(Vector2 position, int multiplier) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Rectangle square = CommonUtils.getSquare(position);
        return CommonUtils.isNotValidDirection(position)
                || CommonUtils.isNotValidPosition(position, predatorsMap.get(square))
                || CommonUtils.isNotValidPosition(position, herbivoresMap.get(square))
                || CommonUtils.isNotValidDirection(position);
    }

    public List<Integer> getAvailableDirections(Vector2 position) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .filter(i -> {
                    Vector2 direction = CommonUtils.getDirection(position, i);
                    Rectangle square = CommonUtils.getSquare(direction);
                    return CommonUtils.isValidDirection(direction)
                            && CommonUtils.isValidPosition(direction, predatorsMap.get(square))
                            && CommonUtils.isValidPosition(direction, herbivoresMap.get(square));
                })
                .boxed()
                .collect(Collectors.toList());
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    protected void randomStep() {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Vector2 newPosition = new Vector2(position).add(momentum);
        Rectangle square = CommonUtils.getSquare(newPosition);
        if (CommonUtils.isMomentumChanged()
                || CommonUtils.isNotValidDirection(newPosition)
                || CommonUtils.isNotValidPosition(newPosition, predatorsMap.get(square))
                || CommonUtils.isNotValidPosition(newPosition, herbivoresMap.get(square))
        ) {
            int randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position));
            newPosition = CommonUtils.getDirection(position, randomDirection);
            momentum.set(new Vector2(newPosition).sub(position));
        }
        position.set(newPosition);
        updateOrganismsMap(position);
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

    public int getFullness() {
        return fullness;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Organism{" +
                "position=" + position +
                '}';
    }
}
