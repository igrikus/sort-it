package program;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IntMode {
    static List<int[]> ints = new ArrayList<>(); // Основной лист, в котором будут храниться массивы чисел из файлов

    /**
     * Метод, в котором находится логика сортировки слиянием. Принимает на вход два предварительно
     * отсортированных массива чисел
     *
     * @return один общий массив, отсортированный в зависимости от флага ascending
     */
    public static int[] sortIntArrays(int[] a1, int[] a2) {
        int[] a3 = new int[a1.length + a2.length];
        int a1Pos = 0, a2Pos = 0, a3Pos = 0;

        if (Main.isAscending()) {
            while (a1Pos < a1.length && a2Pos < a2.length) {
                a3[a3Pos++] = a1[a1Pos] < a2[a2Pos] ? a1[a1Pos++] : a2[a2Pos++];
            }
        } else {
            while (a1Pos < a1.length && a2Pos < a2.length) {
                a3[a3Pos++] = a1[a1Pos] > a2[a2Pos] ? a1[a1Pos++] : a2[a2Pos++];
            }
        }
        // проверяем остались ли элементы в каком-то из массивов и, если да, то дописываем что осталось
        if (a1Pos < a1.length) {
            System.arraycopy(a1, a1Pos, a3, a3Pos, a1.length - a1Pos);
        } else if (a2Pos < a2.length) {
            System.arraycopy(a2, a2Pos, a3, a3Pos, a2.length - a2Pos);
        }

        return a3;
    }

    /**
     * Основной метод, в котором находится вся логика обработки для чисел.
     *
     * @return итоговый лист со всеми итоговыми данными
     */
    public static List<Integer> mergeInt(String[] inFiles) {
        // проходимся в цикле по всем входным файлам
        for (String filename : inFiles) {
            int[] arrayFromFile = readIntDataFromFile(filename); // читаем данные из файла в массив
            if (arrayFromFile != null)
                ints.add(checkSorting(arrayFromFile, filename)); // проверяем на предварительную сортировку и добавляем в основной лист
        }

        List<Integer> result = new ArrayList<>(); // итоговый лист, который в конце будем возвращать
        for (int j : ints.get(0)) { // заполняем его данными из самого первого файла
            result.add(j);
        }

        for (int i = 1; i < ints.size(); i++) {
            int[] firstArray = result.stream().mapToInt(integer -> integer).toArray();
            int[] temp = sortIntArrays(firstArray, ints.get(i));
            result.clear();
            for (int j : temp) {
                result.add(j);
            }
        }
        return result;
    }

    /**
     * Проверяет массив на предварительную сортировку.
     * Числа, не прошедшие проверку не попадают в итоговый массив
     * и считаются ошибочными данными
     *
     * @return массив с предварительно отсортированными числами
     */
    public static int[] checkSorting(int[] array, String filename) {
        if (array == null)
            return null;

        if (Main.isAscending())
            return checkAscendingSorting(array, filename);
        else
            return checkDescendingSorting(array, filename);
    }

    /**
     * Вызывается в checkSorting если Main.isAscending() возвращает true
     * <p>
     * Первое число в массиве считается за верное, числа меньше первого
     * не будут включены в итоговый массив и будут считаться ошибочными
     *
     * @return массив с предварительно отсортированными числами в порядке возрастания
     */
    private static int[] checkAscendingSorting(int[] array, String filename) {
        List<Integer> tempList = new ArrayList<>();

        int i = 0;
        int j = 1;
        tempList.add(array[i]);
        while (j < array.length) {
            if (array[j] < array[i]) {
                System.out.println("Число " + array[j] + " в файле " + filename + " меньше числа " + array[i]);
                System.out.println(array[j] + " не будет включено в итоговую сортировку, так как нарушает предварительную сортировку по возрастанию.");
                System.out.println();
                j++;
            } else {
                i = j;
                tempList.add(array[j++]);
            }
        }
        return tempList.stream().mapToInt(integer -> integer).toArray();
    }

    /**
     * Вызывается в checkSorting если Main.isAscending() возвращает false
     * <p>
     * Первое число в массиве считается за верное, числа больше первого
     * не будут включены в итоговый массив и будут считаться ошибочными
     *
     * @return массив с предварительно отсортированными числами в порядке убывания
     */
    private static int[] checkDescendingSorting(int[] array, String filename) {
        List<Integer> tempList = new ArrayList<>();

        int i = 0;
        int j = 1;
        tempList.add(array[i]);
        while (j < array.length) {
            if (array[j] > array[i]) {
                System.out.println("Число " + array[j] + " в файле " + filename + " больше числа " + array[i]);
                System.out.println(array[j] + " не будет включено в итоговую сортировку, так как нарушает предварительную сортировку по убыванию.");
                System.out.println();
                j++;
            } else {
                i = j;
                tempList.add(array[j++]);
            }
        }
        return tempList.stream().mapToInt(integer -> integer).toArray();
    }

    /**
     * Метод записывает результат в файл, каждый элемент с новой строки
     *
     * @param result итоговый лист, который будем записывать в файл
     * @param outFile собственно сам выходной файл
     */
    public static void writeIntResult(List<Integer> result, String outFile) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)))) {
            for (Integer integer : result) {
                writer.write(integer + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи результата в файл, проверьте достижимость файла");
        }
    }

    /**
     * @param filename Имя файла из которого будем читать числа
     * @return массив чисел, сформированный из файла
     */
    public static int[] readIntDataFromFile(String filename) {
        if (filename == null)
            return null;

        String string = null;

        ArrayList<Integer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
            while (reader.ready()) {
                try {
                    string = reader.readLine();
                    list.add(Integer.parseInt(string));
                } catch (NumberFormatException e) {
                    System.out.println(string + " в файле " + filename + " не является числом и не может быть отсортировано в этом режиме");
                    System.out.println();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл " + filename + " не найден");
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка: нет доступа к файлу " + filename);
            System.out.println("Этот файл не будет учитываться при сортировке");
            return null;
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
