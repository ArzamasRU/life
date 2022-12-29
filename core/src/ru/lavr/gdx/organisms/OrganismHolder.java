package ru.lavr.gdx.organisms;

import java.util.ArrayList;
import java.util.List;

public class OrganismHolder {
    private List<Organism> plants = new ArrayList<>();
    private List<Organism> herbivores = new ArrayList<>();
    private List<Organism> predators = new ArrayList<>();
//    private List<Organism> newPlants = new ArrayList<>();
//    private List<Organism> newHerbivores = new ArrayList<>();
//    private List<Organism> newPredators = new ArrayList<>();

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
