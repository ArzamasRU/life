package ru.lavr.gdx.constants;

public class Constant {
    //    общие константы алгоритма
    public static final int PAUSE = 0;
    public static final int CELL_SIZE = 10;
    public static final int STEP = 10;
    //    вероятность изменения импульса движения (если = 100, приведет к бесконечному циклу)
    public static final int CHANCE_OF_MOMENTUM_CHANGE = 90;
    public static final int MAX_CHANCE = 100;
    public static final int SQUARE_SIZE = STEP * 10;

    //    общие свойства
    public static final int MAX_FULLNESS = 300;
    public static final int STEP_EXHAUSTION = 1;

    //    свойства plant
    public static final int PLANT_DIVISION_COST = 150;
    public static final int STEP_PLANT_FULLNESS = 1;
    public static final int START_PLANT_FULLNESS = 0;
    public static final int PLANT_READY_FOR_DIVISION = 100;

    //    свойства herbivore
    public static final int HERBIVORE_DIVISION_COST = 60;
    public static final int STEP_HERBIVORE_FULLNESS = 20;
    public static final int START_HERBIVORE_FULLNESS = 150;
    public static final int HERBIVORE_READY_FOR_DIVISION = 160;

    //    свойства predator
    public static final int PREDATOR_VISION = 2;
    public static final int PREDATOR_DIVISION_COST = 60;
    public static final int STEP_PREDATOR_FULLNESS = 30;
    public static final int START_PREDATOR_FULLNESS = 150;
    public static final int PREDATOR_READY_FOR_DIVISION = 160;

    //    значения для ноута
    public static final int UPPER_EDGE = 720;
    public static final int RIGHT_EDGE = 1280;
    public static final int BOTTOM_EDGE = 0;
    public static final int LEFT_EDGE = 0;

    //    значения для большого монитора
//    public static final int UPPER_EDGE = 1800;
//    public static final int RIGHT_EDGE = 3800;
//    public static final int BOTTOM_EDGE = 150;
//    public static final int LEFT_EDGE = 0;

    //     для тестов
//    public static final int UPPER_EDGE = 50;
//    public static final int RIGHT_EDGE = 50;
//    public static final int BOTTOM_EDGE = 0;
//    public static final int LEFT_EDGE = 0;
}
