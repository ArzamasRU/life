package ru.lavr.gdx.holders;

import static ru.lavr.gdx.constants.ApplicationConstants.RIGHT_EDGE;
import static ru.lavr.gdx.constants.ApplicationConstants.UPPER_EDGE;
import static ru.lavr.gdx.constants.LifeConstants.CELL_SIZE;
import static ru.lavr.gdx.constants.LifeConstants.SQUARE_SIZE;
import static ru.lavr.gdx.constants.LifeConstants.STEP;

import com.badlogic.gdx.math.Rectangle;
import ru.lavr.gdx.organisms.Herbivore;
import ru.lavr.gdx.organisms.Organism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrganismHolder {
    private final static OrganismHolder organismHolder = new OrganismHolder();

    private final List<Organism> plants = new ArrayList<>();
    private final List<Organism> herbivores = new ArrayList<>();
    private final List<Organism> predators = new ArrayList<>();
    private final Map<Rectangle, List<Organism>> plantsMap = new HashMap<>();
    private final Map<Rectangle, List<Organism>> herbivoresMap = new HashMap<>();
    private final Map<Rectangle, List<Organism>> predatorsMap = new HashMap<>();
    private final Map<Rectangle, List<Rectangle>> squares = new HashMap<>();

    {
        for (int x = 0; x < RIGHT_EDGE; x += SQUARE_SIZE) {
            for (int y = 0; y < UPPER_EDGE; y += SQUARE_SIZE) {
                Rectangle square = new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                squares.put(square, new ArrayList<>());
                plantsMap.put(square, new ArrayList<>());
                herbivoresMap.put(square, new ArrayList<>());
                predatorsMap.put(square, new ArrayList<>());
            }
        }
        squares.forEach((square, neighbors) -> neighbors.addAll(OrganismHolder.this.getCloseSquares(square)));
    }

    public Map<Rectangle, List<Rectangle>> getSquares() {
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

    public void updateOrganisms() {
        OrganismHolder organismHolder = OrganismHolder.getOrganismHolder();
        List<Organism> plants = organismHolder.getPlants();
        List<Organism> herbivores = organismHolder.getHerbivores();
        List<Organism> predators = organismHolder.getPredators();
        Map<Rectangle, List<Organism>> plantsMap = organismHolder.getPlantsMap();
        Map<Rectangle, List<Organism>> herbivoresMap = organismHolder.getHerbivoresMap();
        Map<Rectangle, List<Organism>> predatorsMap = organismHolder.getPredatorsMap();

        plantsMap.values().forEach(orgs -> orgs.removeIf(org -> org.getFullness() <= 0 || !org.isActive()));
        herbivoresMap.values().forEach(orgs -> orgs.removeIf(
                org -> !((Herbivore) org).isLastStep() && (org.getFullness() <= 0 || !org.isActive())
        ));
        predatorsMap.values().forEach(orgs -> orgs.removeIf(org -> org.getFullness() <= 0 || !org.isActive()));

        plants.clear();
        herbivores.clear();
        predators.clear();

        plants.addAll(plantsMap.values().stream().flatMap(List::stream).collect(Collectors.toList()));
        herbivores.addAll(herbivoresMap.values().stream().flatMap(List::stream).collect(Collectors.toList()));
        predators.addAll(predatorsMap.values().stream().flatMap(List::stream).collect(Collectors.toList()));
    }

    private List<Rectangle> getCloseSquares(Rectangle rectangle) {
        Rectangle bigRect = new Rectangle(rectangle.x - STEP, rectangle.y - STEP,
                rectangle.width + CELL_SIZE * 2, rectangle.height + CELL_SIZE * 2);
        return squares.keySet().stream()
                .filter(sqr -> sqr.overlaps(bigRect))
                .collect(Collectors.toList());
    }
}
