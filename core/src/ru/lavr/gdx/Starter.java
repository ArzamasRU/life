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
import ru.lavr.gdx.utils.CommonUtils;

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
        IntStream.range(0, 1000).forEach(i -> plants.add(new Plant()));
        IntStream.range(0, 100).forEach(i -> herbivores.add(new Herbivore()));
        IntStream.range(0, 100).forEach(i -> predators.add(new Predator()));
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();

        plants.forEach(organism -> organism.render(batch));
        herbivores.forEach(organism -> organism.render(batch));
        predators.forEach(organism -> organism.render(batch));

//        if (plants.size() < 3000) {
//            plants.stream()
//                    .filter(org -> org.getNeighbors().size() < 8)
//                    .forEach(Organism::reproduce);
//        }
//        if (herbivores.size() < 10) {
//            herbivores.forEach(Organism::division);
//        }
//        if (predators.size() < 10) {
//            predators.forEach(Organism::division);
//        }

        plants.forEach(Organism::move);
        herbivores.forEach(Organism::move);
        predators.forEach(Organism::move);

        CommonUtils.updateOrganisms();

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
