package ru.lavr.gdx.organisms;

import static ru.lavr.gdx.constants.LifeConstants.CELL_SIZE;
import static ru.lavr.gdx.constants.LifeConstants.REPRODUCE_RANGE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Organism {
    protected final List<Organism> neighbors = new ArrayList<>();
    protected final Vector2 momentum = new Vector2();
    protected final Rectangle currSquare = new Rectangle();
    private final Texture texture;
    //    assign x and y each time before use
    private final Rectangle rectangle = new Rectangle(0, 0, CELL_SIZE, CELL_SIZE);

    protected Vector2 position;
    protected boolean active = true;
    protected int fullness = 1;
    private boolean outOfBorder;

    public Organism(boolean outOfBorder) {
        this.active = false;
        this.outOfBorder = outOfBorder;
        this.texture = null;
    }

    public Organism(Texture texture) {
        this.texture = texture;
        Vector2 randomPosition;
        do {
            randomPosition = CommonUtils.getRandomPosition();
        } while (isNotValidPosition(randomPosition, REPRODUCE_RANGE));
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

    public List<Integer> getAvailableDirections(Vector2 position, Rectangle curRect) {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        return IntStream.range(1, 10)
                .filter(i -> i != 5)
                .filter(i -> {
                    Vector2 direction = CommonUtils.getDirection(position, i);
                    Rectangle square = CommonUtils.getSquare(direction, curRect);
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
        Rectangle square = CommonUtils.getSquare(newPosition, currSquare);
        if (CommonUtils.isMomentumChanged()
                || CommonUtils.isNotValidDirection(newPosition)
                || CommonUtils.isNotValidPosition(newPosition, predatorsMap.get(square))
                || CommonUtils.isNotValidPosition(newPosition, herbivoresMap.get(square))
        ) {
            int randomDirection = CommonUtils.getRandomDirection(getAvailableDirections(position, currSquare));
            if (randomDirection != 0) {
                newPosition = CommonUtils.getDirection(position, randomDirection);
                momentum.set(new Vector2(newPosition).sub(position));
                updateOrganismsMap(newPosition);
                position.set(newPosition);
                return;
            }
        }
        updateOrganismsMap(newPosition);
        position.set(newPosition);
    }

    public List<Organism> getNeighbors() {
        return neighbors;
    }

    public Rectangle getUpdatedRectangle() {
        rectangle.x = position.x;
        rectangle.y = position.y;
        return rectangle;
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

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Organism{" +
                "position=" + position +
                '}';
    }
}
