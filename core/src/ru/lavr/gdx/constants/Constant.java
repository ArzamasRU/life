package ru.lavr.gdx.constants;

public class Constant {
    //    общие константы алгоритма
    public static final int CELL_SIZE = 10;
    public static final int STEP = 10;
    //    импульс движения от MOMENTUM до MAX_MOMENTUM (MOMENTUM = MAX_MOMENTUM приведет к бесконечному циклу)
    public static final int MOMENTUM = 8;
    public static final int MAX_MOMENTUM = 10;

    //    общие свойства
    public static final int MAX_FULLNESS = 150;
    public static final int READY_FOR_DIVISION = 100;
    public static final int STEP_EXHAUSTION = 1;

    //    свойства plant
    public static final int PLANT_DIVISION_COST = 100;
    public static final int STEP_PLANT_FULLNESS = 10;

    //    свойства herbivore
    public static final int HERBIVORE_DIVISION_COST = 50;
    public static final int STEP_HERBIVORE_FULLNESS = 5;

    //    свойства predator
    public static final int PREDATOR_DIVISION_COST = 50;
    public static final int STEP_PREDATOR_FULLNESS = 100;
    public static final int PREDATOR_VISION = 2;

    //    значения для desctop
    public static final int UPPER_EDGE = 720;
    public static final int RIGHT_EDGE = 1280;
    public static final int BOTTOM_EDGE = 0;
    public static final int LEFT_EDGE = 0;

    //    значения для большого монитора
//    public static final int UPPER_EDGE = 1800;
//    public static final int RIGHT_EDGE = 3800;
//    public static final int BOTTOM_EDGE = 150;
//    public static final int LEFT_EDGE = 0;

//    значения для монитора ноутбука
//    public static final int UPPER_EDGE = 800;
//    public static final int RIGHT_EDGE = 1800;
//    public static final int BOTTOM_EDGE = 100;
//    public static final int LEFT_EDGE = 0;

    //     для тестов
//    public static final int UPPER_EDGE = 50;
//    public static final int RIGHT_EDGE = 50;
//    public static final int BOTTOM_EDGE = 0;
//    public static final int LEFT_EDGE = 0;
}
