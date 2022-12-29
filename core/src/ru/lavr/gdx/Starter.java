package ru.lavr.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.lavr.gdx.organisms.Herbivore;
import ru.lavr.gdx.organisms.Organism;
import ru.lavr.gdx.organisms.OrganismHolder;
import ru.lavr.gdx.organisms.Plant;
import ru.lavr.gdx.organisms.Predator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
    private List<Organism> plants;
    private List<Organism> herbivores;
    private List<Organism> predators;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        batch = new SpriteBatch();
        plants = organismHolder.getPlants();
        herbivores = organismHolder.getHerbivores();
        predators = organismHolder.getPredators();
        IntStream.range(0, 100).forEach(i -> plants.add(new Plant(plants)));
        IntStream.range(0, 100).forEach(i -> herbivores.add(new Herbivore(herbivores, predators)));
        IntStream.range(0, 100).forEach(i -> predators.add(new Predator(herbivores, predators)));
    }

    @Override
    public void render() {
        List<Organism> newPlants = new ArrayList<>();
        List<Organism> newHerbivores = new ArrayList<>();
        List<Organism> newPredators = new ArrayList<>();
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();

        herbivores.forEach(organism -> organism.render(batch));
        predators.forEach(organism -> organism.render(batch));
        plants.forEach(organism -> organism.render(batch));

        if (plants.size() < 5000) {
            plants.stream()
                    .filter(org -> org.getNeighbors().size() < 8)
                    .forEach(org -> org.division(newPlants));
            plants.addAll(newPlants);
        }
//        if (herbivores.size() < 1000) {
//            herbivores.forEach(organism -> organism.division(newHerbivores));
//            herbivores.addAll(newHerbivores);
//        }
//        if (predators.size() < 1000) {
//            predators.forEach(organism -> organism.division(newPredators));
//            predators.addAll(newPredators);
//        }

//        herbivoreOrganisms.forEach(
//                organism -> organism.move(plantOrganisms, herbivoreOrganisms, predatorOrganisms, newOrganisms));
//        predatorOrganisms.forEach(organism -> organism.move(herbivoreOrganisms, predatorOrganisms, newOrganisms));
        Gdx.app.log("MyTag", String.valueOf(plants.size()));
        Gdx.app.log("MyTag", String.valueOf(herbivores.size()));
        Gdx.app.log("MyTag", String.valueOf(predators.size()));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
