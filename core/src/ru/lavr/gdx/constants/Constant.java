package ru.lavr.gdx.constants;

public class Constant {
    //    общие константы алгоритма
    public static final int PAUSE = 0;
    public static final int CELL_SIZE = 3;
    public static final int STEP = 3;
    //    вероятность изменения импульса движения (если = 100, приведет к бесконечному циклу)
    public static final int CHANCE_OF_MOMENTUM_CHANGE = 95;
    public static final int MAX_CHANCE = 100;

    //    общие свойства
    public static final int MAX_FULLNESS = 500;
    public static final int STEP_EXHAUSTION = 1;
    public static final int EAT_RANGE = 1;
    public static final int REPRODUCE_RANGE = 1;

    //    свойства plant
    public static final int PLANT_DIVISION_COST = 99;
    public static final int STEP_PLANT_FULLNESS = 1;
    public static final int START_PLANT_FULLNESS = 1;
    public static final int PLANT_READY_FOR_DIVISION = 100;

    //    свойства herbivore
    public static final int HERBIVORE_VISION = 1;
    public static final int HERBIVORE_DIVISION_COST = 200;
    public static final int STEP_HERBIVORE_FULLNESS = 20;
    public static final int START_HERBIVORE_FULLNESS = 300;
    public static final int HERBIVORE_READY_FOR_DIVISION = 400;

    //    свойства predator
    public static final int PREDATOR_VISION = 2;
    public static final int PREDATOR_DIVISION_COST = 200;
    public static final int STEP_PREDATOR_FULLNESS = 40;
    public static final int START_PREDATOR_FULLNESS = 300;
    public static final int PREDATOR_READY_FOR_DIVISION = 400;

    //    значения для ноута
    public static final int UPPER_EDGE = 2500;
    public static final int RIGHT_EDGE = 2500;
    public static final int BOTTOM_EDGE = 0;
    public static final int LEFT_EDGE = 0;
    public static final int WINDOW_HEIGHT = 720;
    public static final int WINDOW_WIDTH = 1280;
    public static final int CAMERA_START_POSITION_X = RIGHT_EDGE / 2;
    public static final int CAMERA_START_POSITION_Y = UPPER_EDGE / 2;

    //     для тестов
//    public static final int UPPER_EDGE = 200;
//    public static final int RIGHT_EDGE = 200;
//    public static final int BOTTOM_EDGE = 0;
//    public static final int LEFT_EDGE = 0;

    public static final double COEF_ADD_QTY_PLANTS = 0.0005;
    public static final double COEF_START_QTY_PREDATORS = 0.0015;
    public static final double COEF_START_QTY_HERBIVORES = 0.003;
    public static final double COEF_SQUARE_SIZE = 0.0001;
    public static final int MAX_QTY_PREDS_AND_HERBS = (UPPER_EDGE * RIGHT_EDGE) / (CELL_SIZE * CELL_SIZE);
    public static final int MAX_QTY_PLANTS = MAX_QTY_PREDS_AND_HERBS / 2;
    public static final int START_QTY_PREDATORS = (int) (MAX_QTY_PREDS_AND_HERBS * COEF_START_QTY_PREDATORS);
    public static final int START_QTY_HERBIVORES = (int) (MAX_QTY_PREDS_AND_HERBS * COEF_START_QTY_HERBIVORES);
    public static final int ADD_QTY_PLANTS = (int) (MAX_QTY_PREDS_AND_HERBS * COEF_ADD_QTY_PLANTS);
    public static final int SQUARE_SIZE = (int) (MAX_QTY_PREDS_AND_HERBS * COEF_SQUARE_SIZE) / CELL_SIZE * CELL_SIZE;
}
