// Ex_01 Реализовать задание и печать карты для волнового алгоритма

import java.util.Random;

public class Maze {

    static int wall = -11;
    static int space = 0;
    static int startPoint = 1;
    static int finishPoint = -9;
    static int start = startPoint;
    static boolean flag = true;
    static boolean deapEnd = false;
    static int way = -3;

    public static void main(String[] args) {

        // Границы для рандома размеров лабиринта
        int minBoard = 10;
        int maxBoard = 20;

        // Рандомное количетво строк и столбцов лабиринта
        int row = randomNum(minBoard, maxBoard);
        int column = randomNum(minBoard, maxBoard);

        // Создание лабиринта
        int[][] arr = greateArray(row, column);

        // Вывод лабиринта
        printArray("\n Исходный лабиринт \n", arr);

        // Поиск пути
        wayOnMaze(arr);

        if (deapEnd == false) {
            printArray("\n Волновое заполнение \n", arr); // Вывод заполненного лабиринта
            // int[][] arr2 = arr.clone();

            findAWay(arr); // Поиск пути
            // printArray("\n Решенный лабиринт \n", arr); // Вывод решенного лабиринта
            String[][] arr2 = formatArray(arr);
            printArrayString("\n Отформатированный лабиринт \n", arr2);

        } else {
            // printArray("\n Решений нет \n", arr); // Решений нет
            String[][] arr2 = formatArray(arr);
            printArrayString("\n Отформатированный лабиринт \n (решений нет) \n", arr2);
        }

    }

    // *********************************************************

    // Создание лабиринта
    public static int[][] greateArray(int row, int column) {

        int[][] array = new int[row][column];

        // Подготовка лабиринта (создание границ и рандомное заполнение
        // начиная со второй строки через строку)
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (i == 0 || j == 0 || i == array.length - 1 || j == array[i].length - 1) {
                    array[i][j] = wall;
                } else {
                    array[i][j] = randomNum(wall, wall + 2);
                    if (array[i][j] != wall) {
                        array[i][j] = space;
                    }
                }
            }
        }

        // Создание точек входа и выхода
        int a = randomNum(1, row - 2);
        int b = randomNum(1, column - 2);
        int c = randomNum(1, row - 2);
        int d = randomNum(1, column - 2);
        while (a == c && b == d) {
            c = randomNum(1, row - 2);
            d = randomNum(1, column - 2);
        }
        array[a][b] = startPoint;
        array[c][d] = finishPoint;

        return array;
    }

    // Рандом числа в заданный границах
    public static int randomNum(int min, int max) {
        Random r = new Random();
        int randNum = r.nextInt(max + 1 - min) + min;
        return randNum;
    }

    // *********************************************************

    // Печать лабиринта
    public static void printArray(String a, int[][] array) {
        System.out.println(a + "__________________\n");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(String.format("%3d", array[i][j]));
            }
            System.out.println("");
        }
        System.out.println("__________________");
    }

    // *********************************************************

    // Метод заполнения лабиринта волновым алгоритмом
    public static int[][] wayOnMaze(int[][] array) {

        // Количество "пустых" ячеек в лабиринте
        int a = findNum(array, space);

        while (a > 0 && flag == true && deapEnd == false) {

            // Делаем копию массива для проверки будут ли зменения
            int[][] arrayTest = new int[array.length][];
            for (int i = 0; i < array.length; i++) {
                arrayTest[i] = array[i].clone();
            }

            // Реализация шага волнового алгоритма
            for (int i = 1; i < array.length - 1; i++) {
                for (int j = 1; j < array[i].length - 1; j++) {
                    if (array[i][j] == start) {
                        array[i - 1][j] = stepWave(array[i - 1][j]);
                        if (flag == false) {
                            break;
                        }
                        array[i][j + 1] = stepWave(array[i][j + 1]);
                        if (flag == false) {
                            break;
                        }
                        array[i + 1][j] = stepWave(array[i + 1][j]);
                        if (flag == false) {
                            break;
                        }
                        array[i][j - 1] = stepWave(array[i][j - 1]);
                        if (flag == false) {
                            break;
                        }
                    }
                }
            }
            start++;
            a = findNum(array, space);

            // Проверяем были изменения в лабиринте или нет
            if (flag == true) {
                int count = 0;
                for (int i = 0; i < array.length; i++) {
                    for (int j = 0; j < array[i].length; j++) {
                        if (array[i][j] != arrayTest[i][j]) {
                            count++;
                        }
                    }
                }
                if (count == 0) {
                    flag = false;
                    deapEnd = true;
                }
            }
        }
        return array;
    }

    // Метод поиска числа в массиве
    public static Integer findNum(int[][] array, int num) {
        int count = 0;
        for (int i = 1; i < array.length - 1; i++) {
            for (int j = 1; j < array[i].length - 1; j++) {
                if (array[i][j] == num)
                    ;
                {
                    count++;
                }
            }
        }
        return count;
    }

    // Делаем шаг, если не пришли к финишу
    public static Integer stepWave(int a) {

        if (a == space) {
            a = start + 1;
        } else if (a == finishPoint) {
            flag = false;

        }
        return a;
    }

    // *********************************************************

    // Поиск пути от финиша к старту
    public static int[][] findAWay(int[][] array) {
        Boolean flagFindAWay = true;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == finishPoint) {
                    while (flagFindAWay == true) {
                        int temp = minNum(array[i - 1][j], array[i][j + 1], array[i + 1][j], array[i][j - 1]);

                        if (array[i - 1][j] == startPoint
                                || array[i][j + 1] == startPoint
                                || array[i + 1][j] == startPoint
                                || array[i][j - 1] == startPoint) {
                            flagFindAWay = false;
                        } else {
                            if (temp == 1) {
                                i--;
                                array[i][j] = way;
                            } else if (temp == 2) {
                                j++;
                                array[i][j] = way;
                            } else if (temp == 3) {
                                i++;
                                array[i][j] = way;
                            } else if (temp == 4) {
                                j--;
                                array[i][j] = way;
                            } else {
                                flagFindAWay = false;
                            }
                        }
                    }
                }
            }
        }
        return array;
    }

    // Поиск минимального числа из 4-х возможных направлений
    public static Integer minNum(int a, int b, int c, int d) {
        Boolean flagA = checkNum(a);
        Boolean flagB = checkNum(b);
        Boolean flagC = checkNum(c);
        Boolean flagD = checkNum(d);

        if (flagA == true
                && (flagB == false || a <= b)
                && (flagC == false || a <= c)
                && (flagD == false || a <= d)) {
            return 1;

        } else if (flagB == true
                && (flagA == false || b < a)
                && (flagC == false || b <= c)
                && (flagD == false || b <= d)) {
            return 2;

        } else if (flagC == true
                && (flagA == false || c < a)
                && (flagB == false || c < b)
                && (flagD == false || c <= d)) {
            return 3;

        } else if (flagD == true
                && (flagA == false || d < a)
                && (flagB == false || d < b)
                && (flagC == false || d < c)) {
            return 4;

        } else {
            return 5;
        }
    }

    // Проверка на то, что в ячейке число из волнового алгоритма
    public static Boolean checkNum(int num) {
        if (num != wall
                && num != finishPoint
                && num != space
                && num != way
                && num != startPoint) {
            return true;
        } else {
            return false;
        }
    }

    // *********************************************************

    // Приведение лабиринта к удобочитаемому виду
    public static String[][] formatArray(int[][] array) {
        int row = array.length;
        int column = array[0].length;
        String[][] arrayStr = new String[row][column];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == wall) {
                    arrayStr[i][j] = "MWM";
                } else if (array[i][j] == way) {
                    arrayStr[i][j] = " * ";
                } else if (array[i][j] == startPoint) {
                    arrayStr[i][j] = " S ";
                } else if (array[i][j] == finishPoint) {
                    arrayStr[i][j] = " F ";
                } else {
                    arrayStr[i][j] = "   ";
                }
            }
        }
        return arrayStr;
    }

    // *********************************************************

    // Печать отформатированного лабиринта
    public static void printArrayString(String a, String[][] array) {
        System.out.println(a + "__________________\n");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(String.format(array[i][j]));
            }
            System.out.println("");
        }
        System.out.println("__________________");
    }
}