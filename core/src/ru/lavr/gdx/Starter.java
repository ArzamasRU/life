package ru.lavr.gdx;

import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.STEP;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    Organism organism;
    List<Organism> organisms = new ArrayList<>();
    private OrthographicCamera camera;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, 1500, 480);
        batch = new SpriteBatch();
        IntStream.range(0, 1).forEach(i -> organisms.add(new Organism(organisms)));
    }

    @Override
    public void render() {
        List<Organism> newOrganisms = new ArrayList<>();
        ScreenUtils.clear(1, 1, 1, 1);
//        camera.update();
//        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        organisms.forEach(organism -> organism.render(batch));
//        organisms.forEach(organism -> organism.move(organisms, newOrganisms));
        if (organisms.size() < 9000) {
            organisms.forEach(organism -> {
                Organism newOrganism = organism.division(organisms, newOrganisms);
                if (newOrganism != null) {
                    newOrganisms.add(newOrganism);
                }
            });
            organisms.addAll(newOrganisms);
        } else {
            organisms.stream()
                    .filter(organism -> organisms.stream()
                            .anyMatch(org -> {
                                Vector2 position1 = organism.getPosition();
                                Rectangle rectangle1 = new Rectangle(position1.x, position1.y, CELL_SIZE, CELL_SIZE);
                                Vector2 position2 = org.getPosition();
                                Rectangle rectangle2 = new Rectangle(position2.x, position2.y, CELL_SIZE, CELL_SIZE);
                                return rectangle1.overlaps(rectangle2) && !organism.equals(org);
                            }))
                    .forEach(System.out::println);
        }

        Gdx.app.log("MyTag", String.valueOf(organisms.size()));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        organism.dispose();
    }
}
