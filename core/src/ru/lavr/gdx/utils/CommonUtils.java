package ru.lavr.gdx.utils;

import static ru.lavr.gdx.constants.Constant.BOTTOM_EDGE;
import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.LEFT_EDGE;
import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.STEP;
import static ru.lavr.gdx.constants.Constant.UPPER_EDGE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.lavr.gdx.Organism;

import java.util.List;

public class CommonUtils {
    public static Vector2 getRandomPosition() {
        Vector2 vector2 = new Vector2();
        float x = MathUtils.random(LEFT_EDGE / CELL_SIZE, RIGHT_EDGE / CELL_SIZE);
        float y = MathUtils.random(BOTTOM_EDGE / CELL_SIZE,  UPPER_EDGE / CELL_SIZE);
        return vector2.set(x * CELL_SIZE, y * CELL_SIZE);
    }

    public static Vector2 getRandomDirection(Vector2 position) {
        Vector2 vector2 = new Vector2(position);
        int direction = MathUtils.random(1, 9);
        if (direction == 1) {
            return vector2.add(-STEP, +STEP);
        } else if (direction == 2) {
            return vector2.add(0, +STEP);
        } else if (direction == 3) {
            return vector2.add(+STEP, +STEP);
        } else if (direction == 4) {
            return vector2.add(-STEP, 0);
        } else if (direction == 5) {
            return vector2.add(0, 0);
        } else if (direction == 6) {
            return vector2.add(+STEP, 0);
        } else if (direction == 7) {
            return vector2.add(-STEP, -STEP);
        } else if (direction == 8) {
            return vector2.add(0, -STEP);
        } else if (direction == 9) {
            return vector2.add(+STEP, -STEP);
        }
        return null;
    }

    public static int getRandomDirection() {
        return MathUtils.random(1, 9);
    }

    public static Vector2 getRandomDirection(Vector2 position, int direction) {
        Vector2 vector2 = new Vector2(position);
        if (direction == 1) {
            return vector2.add(-STEP, +STEP);
        } else if (direction == 2) {
            return vector2.add(0, +STEP);
        } else if (direction == 3) {
            return vector2.add(+STEP, +STEP);
        } else if (direction == 4) {
            return vector2.add(-STEP, 0);
        } else if (direction == 5) {
            return vector2.add(0, 0);
        } else if (direction == 6) {
            return vector2.add(+STEP, 0);
        } else if (direction == 7) {
            return vector2.add(-STEP, -STEP);
        } else if (direction == 8) {
            return vector2.add(0, -STEP);
        } else if (direction == 9) {
            return vector2.add(+STEP, -STEP);
        }
        return null;
    }

    public static boolean isValidPosition(Vector2 vector2, List<Organism> organisms) {
        Rectangle rectangle = new Rectangle(vector2.x, vector2.y, CELL_SIZE, CELL_SIZE);
        return organisms.stream()
                .map(Organism::getRectangle)
                .noneMatch(rectangle::overlaps);
    }

    public static boolean isValidDirection(Vector2 vector2) {
        Rectangle field = new Rectangle(LEFT_EDGE, BOTTOM_EDGE, RIGHT_EDGE, UPPER_EDGE);
        Rectangle cell = new Rectangle(vector2.x, vector2.y, CELL_SIZE, CELL_SIZE);
        return cell.overlaps(field);
    }

    public static boolean isNotValidPosition(Vector2 vector2, List<Organism> organisms) {
        return !isValidPosition(vector2, organisms);
    }

    public static boolean isNotValidDirection(Vector2 vector2) {
        return !isValidDirection(vector2);
    }
}
