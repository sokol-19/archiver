package store.sokolov.arch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Утилитный класс для предоставления информации по вложенным каталогам/файлам относительно заданных стартовых точек
 *
 * @author Vladimir Sokolov
 */
public class FileUtils {

    /**
     * Получение информации по всем файлам от точек, переданных в <strong>fileNames</strong>
     *
     * @param fileNames стартовые точки (каталоги/файлы), начиная от которых будет получена информация по всем вложенным элементам (каталогам/файлам)
     * @return мапу всех каталогов и файлов, полученных от стартовых точек (каталогов/файлов), где ключ это относительный путь.
     */
    public static Map<String, File> getAllFilesInfo(List<String> fileNames) throws IOException {
        Map<String, File> allFilesMap = new TreeMap<>();
        if (fileNames == null || fileNames.size() == 0) {
            return allFilesMap;
        }
        // пробегаем по всем каталогам/файлам
        for (String fileName : fileNames) {
            Map<String, File> filesMap = getFilesInfoStart(fileName);
            allFilesMap.putAll(filesMap);
        }
        return allFilesMap;
    }

    /**
     * Стартовый метод для получения информации по всем файлам от точки заданной в <strong>fileName</strong>
     *
     * @param fileName стартовая точка (каталог/файл), начиная с которой будет получена информация всем вложенным элементам (каталогам/файлам)
     * @return мапу всех каталогов и файлов, полученных от стартовой точки (каталога/файла), где ключ это относительный путь.
     */
    public static Map<String, File> getFilesInfoStart(String fileName) throws IOException {
        Map<String, File> filesMap = new TreeMap<>();
        File file = new File(fileName);
        if (file.exists()) {
            getFilesInfo(file, filesMap);
        } else {
            // неверно задан каталог или файл для архивации
            throw new FileNotFoundException("Каталог или файл с именем '" + fileName + "' не существует");
        }
        return filesMap;
    }

    /**
     * Рекурсивный обход каталогов для получения информации по всем вложенным каталогам и файлам.
     * Если <strong>file</strong> является файлом, то заполняется информация только по нему, иначе по всем каталогом и файлов, расположенным внутри него.
     *
     * @param file     каталог или файл, по которым необходимо получить информацию
     * @param filesMap мапа для сохранения информации по всех каталогам и файлам, расположенным в каталоге <strong>file</strong>
     */
    public static void getFilesInfo(File file, Map<String, File> filesMap) throws IOException {
        filesMap.put(getRelativePath(file.getCanonicalPath()), file);
        if (file.isDirectory()) {
            filesMap.put(getRelativePath(file.getCanonicalPath()), file);
            if (file.listFiles() != null) {
                for (File item : file.listFiles()) {
                    getFilesInfo(item, filesMap);
                }
            }
        }
    }

    /**
     * Получение относительного пути (относительно текущего каталога) к каталогу/файлу, заданному с помощью абсолютного пути.
     *
     * @param absolutePath абсолютный путь
     * @return относительный путь (относительно текущего каталога)
     */
    public static String getRelativePath(String absolutePath) {
        String currentDir = System.getProperty("user.dir");
        if (absolutePath == null) {
            return "";
        }
        if ((!absolutePath.contains(currentDir)) || (absolutePath.indexOf(currentDir) > 0)) {
            // файл расположен не в текущем каталоге. вернем полный путь
            return absolutePath;
        } else if (absolutePath.equals(currentDir)) {
            return "";
        } else {
            return absolutePath.substring(currentDir.length() + 1);
        }
    }
}
