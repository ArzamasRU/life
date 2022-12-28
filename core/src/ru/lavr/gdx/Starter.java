package ru.lavr.gdx;

import static ru.lavr.gdx.constants.Constant.CELL_SIZE;
import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.STEP;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    List<Organism> organisms = new ArrayList<>();
    private OrthographicCamera camera;
    public int i;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, 1280, 720);
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
        if (organisms.size() < 5000) {
            organisms.forEach(organism -> {
                Organism newOrganism = organism.division(organisms, newOrganisms, batch);
                if (newOrganism != null) {
                    newOrganisms.add(newOrganism);
                }
            });
            organisms.addAll(newOrganisms);
            organisms.forEach(organism -> organism.render(batch));
        } else {
            int k = 0;
            for (int j = 0; j <= RIGHT_EDGE; j = j + 10) {
                if (j >= RIGHT_EDGE) {
                    k = k + 10;
                    j = 0;
                }
                i++;
                if (i >= organisms.size()) {
                    break;
                }
                Gdx.app.log("MyTag0 ", j + " " + k);
                Organism organism = organisms.get(i);
                organism.setPosition(new Vector2(j, k));
                organism.setRectangle(new Rectangle(j, k, CELL_SIZE, CELL_SIZE));
            }
//            organisms.forEach(organism -> organism.move(organisms, newOrganisms, batch));

//            Gdx.app.log("MyTag", i + " " + organisms.size());
//            if (i + 100 < organisms.size()) {
//                int k = i + 100;
//                for (int j = i; j < k; j++) {
//                    i++;
//                    Organism org = organisms.get(j);
//                    Vector2 randomPosition;
//                    do {
//                        randomPosition = CommonUtils.getRandomPosition();
//                    } while (CommonUtils.isNotValidPosition(randomPosition, organisms));
//                    org.setPosition(randomPosition);
//                    org.setRectangle(new Rectangle(randomPosition.x, randomPosition.y, CELL_SIZE, CELL_SIZE));
//                }
//            }

//            organisms.forEach(org -> {
//                Vector2 randomPosition;
//                do {
//                    randomPosition = CommonUtils.getRandomPosition();
//                } while (CommonUtils.isNotValidPosition(randomPosition, organisms));
//                org.setPosition(randomPosition);
//                org.setRectangle(new Rectangle(randomPosition.x, randomPosition.y, CELL_SIZE, CELL_SIZE));
//            });

//            organisms.forEach(org -> System.out.println(org.getPosition()));

//            organisms.stream()
//                    .filter(organism -> organisms.stream()
//                            .anyMatch(org -> {
//                                Vector2 position1 = organism.getPosition();
//                                Rectangle rectangle1 = new Rectangle(position1.x, position1.y, CELL_SIZE, CELL_SIZE);
//                                Vector2 position2 = org.getPosition();
//                                Rectangle rectangle2 = new Rectangle(position2.x, position2.y, CELL_SIZE, CELL_SIZE);
//                                return rectangle1.overlaps(rectangle2) && !organism.equals(org);
//                            }))
//                    .forEach(organisms -> Gdx.app.log("MyTag 111 ", organisms.toString()));
        }
        Gdx.app.log("MyTag", String.valueOf(organisms.size()));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
