package ru.lavr.gdx.organisms;

import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.SQUARE_SIZE;
import static ru.lavr.gdx.constants.Constant.UPPER_EDGE;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganismHolder {
    private final static OrganismHolder organismHolder = new OrganismHolder();

    private final List<Organism> plants = new ArrayList<>();
    private final List<Organism> herbivores = new ArrayList<>();
    private final List<Organism> predators = new ArrayList<>();
    private final Map<Rectangle, List<Organism>> plantsMap = new HashMap<>();
    private final Map<Rectangle, List<Organism>> herbivoresMap = new HashMap<>();
    private final Map<Rectangle, List<Organism>> predatorsMap = new HashMap<>();
    private final List<Rectangle> squares = new ArrayList<>();

    {
        for (int x = 0; x < RIGHT_EDGE; x += SQUARE_SIZE) {
            for (int y = 0; y < UPPER_EDGE; y += SQUARE_SIZE) {
                Rectangle square = new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                squares.add(square);
                plantsMap.put(square, new ArrayList<>());
                herbivoresMap.put(square, new ArrayList<>());
                predatorsMap.put(square, new ArrayList<>());
            }
        }
    }

    public List<Rectangle> getSquares() {
        return squares;
    }

    public Map<Rectangle, List<Organism>> getPlantsMap() {
        return plantsMap;
    }

    public Map<Rectangle, List<Organism>> getHerbivoresMap() {
        return herbivoresMap;
    }

    public Map<Rectangle, List<Organism>> getPredatorsMap() {
        return predatorsMap;
    }

    public static OrganismHolder getOrganismHolder() {
        return organismHolder;
    }

    public List<Organism> getPlants() {
        return plants;
    }

    public List<Organism> getHerbivores() {
        return herbivores;
    }

    public List<Organism> getPredators() {
        return predators;
    }
}
