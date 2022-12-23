package ru.lavr.gdx;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Organism {
    private final Vector2 position = new Vector2();
    private final Texture texture;

    public Organism() {
        Pixmap pixmap = new Pixmap(10, 10, RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0,0,10, 10);
        this.texture = new Texture(pixmap);
        position.set(500, 500);
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public void move() {
        new Rectangle().overlaps(new Rectangle());
        position.add(10,10);
    }
}
