package ru.lavr.gdx;

import static ru.lavr.gdx.constants.ApplicationConstants.CAMERA_START_POSITION_X;
import static ru.lavr.gdx.constants.ApplicationConstants.CAMERA_START_POSITION_Y;
import static ru.lavr.gdx.constants.ApplicationConstants.PAUSE;
import static ru.lavr.gdx.constants.ApplicationConstants.RIGHT_EDGE;
import static ru.lavr.gdx.constants.ApplicationConstants.UPPER_EDGE;
import static ru.lavr.gdx.constants.ApplicationConstants.WINDOW_HEIGHT;
import static ru.lavr.gdx.constants.ApplicationConstants.WINDOW_WIDTH;
import static ru.lavr.gdx.constants.LifeConstants.ADD_QTY_PLANTS;
import static ru.lavr.gdx.constants.LifeConstants.MAX_QTY_PLANTS;
import static ru.lavr.gdx.constants.LifeConstants.START_QTY_HERBIVORES;
import static ru.lavr.gdx.constants.LifeConstants.START_QTY_PREDATORS;
import static ru.lavr.gdx.constants.LifeConstants.STEP;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.lavr.gdx.holders.OrganismHolder;
import ru.lavr.gdx.organisms.Herbivore;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.organisms.Plant;
import ru.lavr.gdx.organisms.Predator;

import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter implements InputProcessor {
    private final OrthographicCamera camera = new OrthographicCamera();
    private final OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
    private final Viewport extendViewport = new StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT, camera);
    private final List<Organism> plants = organismHolder.getPlants();
    private final List<Organism> herbivores = organismHolder.getHerbivores();
    private final List<Organism> predators = organismHolder.getPredators();

    private SpriteBatch batch;
    private long time;
    private long step;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isUpPressed;
    private boolean isDownPressed;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        camera.position.set(CAMERA_START_POSITION_X, CAMERA_START_POSITION_Y, 0);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        IntStream.range(0, START_QTY_HERBIVORES).forEach(i -> herbivores.add(new Herbivore()));
        IntStream.range(0, START_QTY_PREDATORS).forEach(i -> predators.add(new Predator()));
    }

    @Override
    public void render() {
        step++;
        pause();
        ScreenUtils.clear(1, 1, 1, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        move();
        camera.update();

        plants.forEach(organism -> organism.render(batch));
        herbivores.forEach(organism -> organism.render(batch));
        predators.forEach(organism -> organism.render(batch));

        if (plants.size() < MAX_QTY_PLANTS) {
            IntStream.range(0, ADD_QTY_PLANTS).forEach(i -> plants.add(new Plant()));
        }

        plants.forEach(Organism::move);
        herbivores.forEach(Organism::move);
        predators.forEach(Organism::move);

        organismHolder.updateOrganisms();

        Gdx.app.log("step ", String.valueOf(step));
        Gdx.app.log("plants ", String.valueOf(plants.size()));
        Gdx.app.log("herbivores ", String.valueOf(herbivores.size()));
        Gdx.app.log("predators ", String.valueOf(predators.size()));
        batch.end();
    }

    public void move() {
        float cameraBorder = (float) WINDOW_WIDTH / 2;
        if (isLeftPressed && camera.position.x - cameraBorder > 0) {
            camera.translate(-STEP, 0);
        }
        if (isRightPressed && camera.position.x + cameraBorder < RIGHT_EDGE) {
            camera.translate(STEP, 0);
        }
        if (isUpPressed && camera.position.y + cameraBorder < UPPER_EDGE) {
            camera.translate(0, STEP);
        }
        if (isDownPressed && camera.position.y - cameraBorder > 0) {
            camera.translate(0, -STEP);
        }
    }

    public void pause() {
        long delta = System.currentTimeMillis() - time;
        if (delta > PAUSE) {
            Gdx.app.log("pause = ", String.valueOf(delta));
        } else {
            try {
                Thread.sleep(PAUSE - delta);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        time = System.currentTimeMillis();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void resize(int width, int height) {
        extendViewport.update(width, height);
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.A) {
            isLeftPressed = true;
        } else if (i == Input.Keys.D) {
            isRightPressed = true;
        } else if (i == Input.Keys.W) {
            isUpPressed = true;
        } else if (i == Input.Keys.S) {
            isDownPressed = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        if (i == Input.Keys.A) {
            isLeftPressed = false;
        } else if (i == Input.Keys.D) {
            isRightPressed = false;
        } else if (i == Input.Keys.W) {
            isUpPressed = false;
        } else if (i == Input.Keys.S) {
            isDownPressed = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
