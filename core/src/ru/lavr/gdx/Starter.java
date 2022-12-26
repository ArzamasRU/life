package ru.lavr.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    Organism organism;
    List<Organism> organisms = new ArrayList<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);
        batch = new SpriteBatch();
        IntStream.range(0, 100).forEach(i -> organisms.add(new Organism(organisms)));
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        organisms.forEach(organism -> organism.render(batch));
        organisms.forEach(organism -> organism.move(organisms));
//        organism.move();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        organism.dispose();
    }
}
