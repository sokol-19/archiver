package store.sokolov.arch.data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Класс для создания и удаления тестовых данных для использования в тестировании
 * @author Vladimir Sokolov
 */
public class PrepareData {
    // текущий каталог
    public static String currentDir;
    // стартовая точка, первый уровень
    public static String testDirLevel1;
    // второй уровень
    public static String testDirLevel2;
    // третий уровень
    public static String testDirLevel3;
    // путь к тестовому файлу
    public static String pathToTestFile;
    // тело тестового файла
    public static String bodyTestFile;
    // тестовый файл
    public static File testFile;

    /**
     * Создание тестовых данных
     */
    public static void createTestData() throws IOException {
        currentDir = System.getProperty("user.dir");
        // Создаем тестовые данные
        testDirLevel1 = UUID.randomUUID().toString();
        testDirLevel2 = testDirLevel1 + File.separator + UUID.randomUUID();
        testDirLevel3 = testDirLevel2 + File.separator + UUID.randomUUID();

        File dir = new File(testDirLevel3);
        dir.mkdirs();
        pathToTestFile = testDirLevel3 + File.separator + UUID.randomUUID() + ".txt";

        testFile = new File(pathToTestFile);
        bodyTestFile = UUID.randomUUID().toString();
        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(testFile))) {
            writer.write(bodyTestFile.getBytes(StandardCharsets.UTF_8));
            writer.flush();
        }
    }

    /**
     * Удаляет все тестовые каталоги и файлы, начиная с указанного
     *
     * @param file стартовая точка удаления
     */
    public static void removeAllTestFiles(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File item : file.listFiles()) {
                removeAllTestFiles(item);
            }
        }
        file.delete();
    }
}
