package ru.lavr.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.lavr.gdx.organisms.Herbivore;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.organisms.Plant;
import ru.lavr.gdx.organisms.Predator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    List<Organism> plantOrganisms = new ArrayList<>();
    List<Organism> herbivoreOrganisms = new ArrayList<>();
    List<Organism> predatorOrganisms = new ArrayList<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        batch = new SpriteBatch();
        IntStream.range(0, 100).forEach(i -> plantOrganisms.add(new Plant(plantOrganisms)));
        IntStream.range(0, 100).forEach(i -> plantOrganisms.add(new Herbivore(herbivoreOrganisms, predatorOrganisms)));
        IntStream.range(0, 100).forEach(i -> plantOrganisms.add(new Predator(herbivoreOrganisms, predatorOrganisms)));
    }

    @Override
    public void render() {
        List<Organism> newOrganisms = new ArrayList<>();
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        plantOrganisms.forEach(organism -> organism.render(batch));
        herbivoreOrganisms.forEach(organism -> organism.render(batch));
        predatorOrganisms.forEach(organism -> organism.render(batch));

//        plantOrganisms.forEach(organism -> organism.division(plantOrganisms, newOrganisms));
//        herbivoreOrganisms.forEach(
//                organism -> organism.move(plantOrganisms, herbivoreOrganisms, predatorOrganisms, newOrganisms));
//        predatorOrganisms.forEach(organism -> organism.move(herbivoreOrganisms, predatorOrganisms, newOrganisms));
//
//        if (plantOrganisms.size() < 8000) {
//            plantOrganisms.stream()
//                    .filter(Organism::isActive)
//                    .filter(organism -> organism.getNeighbors().size() < 8)
//                    .forEach(organism -> {
//                        Organism newOrganism = organism.division(plantOrganisms, newOrganisms);
//                        if (newOrganism != null) {
//                            newOrganisms.add(newOrganism);
//                        }
//                    });
//            plantOrganisms.addAll(newOrganisms);
//        }
//        CommonUtils.setInactiveOrganisms(allOrganisms);
        Gdx.app.log("MyTag", String.valueOf(plantOrganisms.size()));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
