package ru.lavr.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Starter extends ApplicationAdapter {
    SpriteBatch batch;
    Organism organism;

    @Override
    public void create() {
        batch = new SpriteBatch();
        organism = new Organism();
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        organism.render(batch);
        organism.move();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        organism.dispose();
    }
}
