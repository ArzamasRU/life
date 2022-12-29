package ru.lavr.gdx.organisms;

import java.util.ArrayList;
import java.util.List;

public class OrganismHolder {
    private final static OrganismHolder organismHolder = new OrganismHolder();

    private List<Organism> plants = new ArrayList<>();
    private List<Organism> herbivores = new ArrayList<>();
    private List<Organism> predators = new ArrayList<>();
    private List<Organism> newPlants = new ArrayList<>();
    private List<Organism> newHerbivores = new ArrayList<>();
    private List<Organism> newPredators = new ArrayList<>();

    public static OrganismHolder getOrganismHolder() {
        return organismHolder;
    }

    public List<Organism> getNewPlants() {
        return newPlants;
    }

    public void setNewPlants(List<Organism> newPlants) {
        this.newPlants = newPlants;
    }

    public List<Organism> getNewHerbivores() {
        return newHerbivores;
    }

    public void setNewHerbivores(List<Organism> newHerbivores) {
        this.newHerbivores = newHerbivores;
    }

    public List<Organism> getNewPredators() {
        return newPredators;
    }

    public void setNewPredators(List<Organism> newPredators) {
        this.newPredators = newPredators;
    }

    public List<Organism> getPlants() {
        return plants;
    }

    public void setPlants(List<Organism> plants) {
        this.plants = plants;
    }

    public List<Organism> getHerbivores() {
        return herbivores;
    }

    public void setHerbivores(List<Organism> herbivores) {
        this.herbivores = herbivores;
    }

    public List<Organism> getPredators() {
        return predators;
    }

    public void setPredators(List<Organism> predators) {
        this.predators = predators;
    }
}
