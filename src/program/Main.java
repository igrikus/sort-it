package program;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Main {
    private static boolean ascending; //флаг, отвечающий за режим сортировки (по возрастанию или по убыванию)

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String[] allFiles = getFiles(args);
        if (allFiles.length == 0) {
            System.out.println("Не введено имя выходного файла");
            return;
        }

        String outFile = allFiles[0];
        String[] inFiles = Arrays.copyOfRange(allFiles, 1, allFiles.length);
        if (inFiles.length == 0) {
            System.out.println("Не введено входных файлов");
            return;
        }

        ascending = isAscending(args);

        //проверяем файл для вывода, и при необходимости создаем его
        checkOrCreateOutPath(outFile);

        if (isIntMode(args)) {
            IntMode.writeIntResult(IntMode.mergeInt(inFiles), outFile);
        } else if (isStringMode(args)) {
            StringMode.writeStringResult(StringMode.mergeString(inFiles), outFile);
        } else {
            System.out.println("Не задан режим сортировки");
            return;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("\nСортировка успешно выполнена за " + (endTime - startTime) + "мс");
    }

    /**
     * Достает из аргументов имена всех файлов (и выходного и входных) и игнорирует аргументы запуска
     *
     * @return массив со всеми именами файлов
     */
    private static String[] getFiles(String[] args) {
        return Arrays.stream(args).
                filter(s -> !s.equals("-a") //убираем все возможные аргументы запуска
                        && !s.equals("-d")
                        && !s.equals("-s")
                        && !s.equals("-i"))
                .toArray(String[]::new);
    }

    /**
     * Проверяет режим сортировки для целых чисел
     *
     * @return true если в аргументах был параметр -i, иначе false
     */
    private static boolean isIntMode(String[] args) {
        return Arrays.asList(args).contains("-i");
    }

    /**
     * Проверяет режим сортировки для строк
     * @return true если в аргументах был параметр -s, иначе false
     */
    private static boolean isStringMode(String[] args) {
        return Arrays.asList(args).contains("-s");
    }

    /**
     * Проверяет наличие флага -d, если его нету, значит включается режим сортировки по возрастанию
     *
     * @return true если не выставлен флаг -d, иначе false
     */
    private static boolean isAscending(String[] args) {
        return !Arrays.asList(args).contains("-d");
    }

    /**
     * Публичный геттер, используется в двух вспомогательных классах
     *
     * @return флаг, обозначающий режим сортировки
     */
    public static boolean isAscending() {
        return ascending;
    }

    /**
     * Метод проверяет наличие файла для вывода, в противном случае создает его.
     * @param outFile Принимает имя выходного файла
     */
    private static void checkOrCreateOutPath(String outFile) {
        Path path = Paths.get(outFile);
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла для вывода. Проверьте правильность введённого пути");
            }
        }
    }
}
