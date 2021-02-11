package program;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringMode {
    static ArrayList<String[]> strings = new ArrayList<>(); // Основной лист, в котором будут храниться массивы строк из файлов

    /**
     * Метод, в котором находится логика сортировки слиянием. Принимает на вход два предварительно
     * отсортированных массива строк
     *
     * @return один общий массив, отсортированный по длине строки в зависимости от флага ascending
     */
    public static String[] sortStringArraysByLength(String[] a1, String[] a2) {
        String[] a3 = new String[a1.length + a2.length];
        int a1Pos = 0, a2Pos = 0, a3Pos = 0;

        if (Main.isAscending()) {
            while (a1Pos < a1.length && a2Pos < a2.length) {
                a3[a3Pos++] = a1[a1Pos].length() < a2[a2Pos].length() ? a1[a1Pos++] : a2[a2Pos++];
            }
        } else {
            while (a1Pos < a1.length && a2Pos < a2.length) {
                a3[a3Pos++] = a1[a1Pos].length() > a2[a2Pos].length() ? a1[a1Pos++] : a2[a2Pos++];
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
     * Основной метод, в котором находится вся логика обработки для строк.
     *
     * @return итоговый лист со всеми итоговыми данными
     */
    public static List<String> mergeString(String[] inFiles) {
        for (String filename : inFiles) {
            String[] arrayFromFile = readStringDataFromFile(filename);
            if (arrayFromFile != null)
                strings.add(checkLengthSorting(arrayFromFile, filename));
        }

        List<String> result = new ArrayList<>(Arrays.asList(strings.get(0)));

        for (int i = 1; i < strings.size(); i++) {
            String[] firstArray = result.toArray(new String[0]);
            String[] temp = sortStringArraysByLength(firstArray, strings.get(i));
            result.clear();
            Collections.addAll(result, temp);
        }
        return result;
    }

    /**
     * Проверяет массив на предварительную сортировку.
     * Строки, не прошедшие проверку не попадают в итоговый массив
     * и считаются ошибочными данными
     *
     * @return массив с предварительно отсортированными строками по длине
     */
    public static String[] checkLengthSorting(String[] array, String filename) {
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
     * Первая строка в массиве считается за верную, строки короче первой
     * не будут включены в итоговый массив и будут считаться ошибочными
     *
     * @return массив с предварительно отсортированными строками в порядке возрастания по длине
     */
    private static String[] checkAscendingSorting(String[] array, String filename) {
        List<String> tempList = new ArrayList<>();

        int i = 0;
        int j = 1;
        tempList.add(array[i]);
        while (j < array.length) {
            if (array[j].length() < array[i].length()) {
                System.out.println("Строка \"" + array[j] + "\" в файле " + filename + " короче строки \"" + array[i] + "\"");
                System.out.println("\"" + array[j] + "\" не будет включена в итоговую сортировку, так как " +
                        "нарушает предварительную сортировку по возрастанию.");
                System.out.println();
                j++;
            } else {
                i = j;
                tempList.add(array[j++]);
            }
        }
        return tempList.toArray(new String[0]);
    }

    /**
     * Вызывается в checkSorting если Main.isAscending() возвращает false
     * <p>
     * Первая строка в массиве считается за верную, строки длиннее первой
     * не будут включены в итоговый массив и будут считаться ошибочными
     *
     * @return массив с предварительно отсортированными строками в порядке убывания по длине
     */
    private static String[] checkDescendingSorting(String[] array, String filename) {
        List<String> tempList = new ArrayList<>();

        int i = 0;
        int j = 1;
        tempList.add(array[i]);
        while (j < array.length) {
            if (array[j].length() > array[i].length()) {
                System.out.println("Строка \"" + array[j] + "\" в файле " + filename + " длиннее строки \"" + array[i] + "\"");
                System.out.println("\"" + array[j] + "\" не будет включена в итоговую сортировку, так как " +
                        "нарушает предварительную сортировку по убыванию.");
                System.out.println();
                j++;
            } else {
                i = j;
                tempList.add(array[j++]);
            }
        }
        return tempList.toArray(new String[0]);
    }

    /**
     * Метод записывает результат в файл, каждый элемент с новой строки
     *
     * @param result итоговый лист, который будем записывать в файл
     * @param outFile собственно сам выходной файл
     */
    public static void writeStringResult(List<String> result, String outFile) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)))) {
            for (String s : result) {
                writer.write(s + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи результата в файл, проверьте достижимость файла");
        }
    }

    /**
     * @param filename Имя файла из которого будем читать строки
     * @return массив строк, сформированный из файла
     */
    public static String[] readStringDataFromFile(String filename) {
        if (filename == null)
            return null;

        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл " + filename + " не найден");
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка: нет доступа к файлу " + filename);
            System.out.println("Этот файл не будет учитываться при сортировке");
            return null;
        }
        return list.toArray(String[]::new);
    }
}
