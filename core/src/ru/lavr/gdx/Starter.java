package ru.lavr.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.lavr.gdx.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    List<Organism> allOrganisms = new ArrayList<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        batch = new SpriteBatch();
        IntStream.range(0, 1).forEach(i -> allOrganisms.add(new Organism(allOrganisms)));
    }

    @Override
    public void render() {
        int j;
        List<Organism> newOrganisms = new ArrayList<>();
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        allOrganisms.forEach(organism -> organism.render(batch));
//        organisms.forEach(organism -> organism.move(organisms, newOrganisms));
        if (allOrganisms.size() < 8000) {
            allOrganisms.stream()
                    .filter(Organism::isActive)
                    .filter(organism -> organism.getNeighbors().size() < 8)
                    .forEach(organism -> {
                        Organism newOrganism = organism.division(allOrganisms, newOrganisms, batch);
                        if (newOrganism != null) {
                            newOrganisms.add(newOrganism);
                        }
                    });
            allOrganisms.addAll(newOrganisms);
        }
//        CommonUtils.setInactiveOrganisms(allOrganisms);
        Gdx.app.log("MyTag", String.valueOf(allOrganisms.size()));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
