package store.sokolov.arch;

import store.sokolov.arch.exception.ArchiveCorrupted;
import store.sokolov.arch.exception.NotFilesSpecified;
import store.sokolov.arch.exception.NotStreamSpecified;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Реализация интерфейса <tt>IArchiver</tt>. Позволяет сжать каталоги/файлы, переданные списком с выводом в OutputStream.
 * Данные для разархивации получаются из InputStream.
 *
 * @author Vldimir Sokolov
 */
public class ZipArchiver implements Archiver {

    /**
     * Метод считывает указанные каталоги/файлы, сжимает их и отправляет результат в OutputStream
     *
     * @param fileNames список каталогов/файлов для архивации
     * @param outputStream поток для передачи сжатых каталогов/файлов
     * @throws NotFilesSpecified выбрасывается при отсутствие каталогов/файлов для архивации
     * @throws NotStreamSpecified выбрасывается при отсутствие потока для передачи сжатых данных
     * @throws IOException выбрасывается при возникновении ошибок при работе с файлами
     */
    @Override
    public void zip(List<String> fileNames, OutputStream outputStream) throws NotFilesSpecified, IOException, NotStreamSpecified {
        // получаем список каталогов/файлов для архивации
        Map<String, File> files = FileUtils.getAllFilesInfo(fileNames);
        if (files == null || files.isEmpty()) {
            throw new NotFilesSpecified("Не заданы каталоги/файлы для архивации");
        }
        if (outputStream == null) {
            throw new NotStreamSpecified("Не задан выходной поток OutputStream");
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
        for (File file : files.values()) {
            ZipEntry zipEntry;
            if (file.isFile()) {
                // файл
                zipEntry = new ZipEntry(file.getPath());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(file.toPath(), zipOutputStream);
                zipEntry.setTime(file.lastModified());
            } else {
                // каталог
                zipEntry = new ZipEntry(file.getPath() + File.separator);
                zipOutputStream.putNextEntry(zipEntry);
            }
        }
        zipOutputStream.flush();
        zipOutputStream.finish();
    }

    /**
     * Метод распаковывает каталоги/файлы из указанного стрима и сохраняет их в текущий каталог.
     * @param inputStream поток для чтения сжатых каталогов/файлов
     * @throws IOException выбрасывается при возникновении ошибок при работе с файлами
     * @throws ArchiveCorrupted выбрасывается, если архив поврежден
     * @throws NotStreamSpecified выбрасывается, если не задан входной стрим для чтения сжатых данных
     */
    @Override
    public void unZip(InputStream inputStream) throws IOException, ArchiveCorrupted, NotStreamSpecified {
        String currentDir = System.getProperty("user.dir");
        if (inputStream == null) {
            throw new NotStreamSpecified("Не задан входной поток InputStream");
        }
        try(ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                // в архиве мусор
                throw new ArchiveCorrupted("Невозможно причитать данные из архива");
            }
            do {
                // запись относительно текущего каталога
                File file = new File(currentDir, zipEntry.getName());
                if (file.exists()) {
                    // TODO: Если нужно предпринять какие-то действия при условии существования такого файла
                    //  , то дописываем код обработки здесь.
                }
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(file.toPath());
                } else {
                    // проверим систему на наличие необходимых каталогов,
                    // если архивировались файлы без указания каталогов
                    if (file.getAbsolutePath().lastIndexOf(File.separator) != -1) {
                        String directoryName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator));
                        File directory = new File(directoryName);
                        if (!directory.exists()) {
                            // Каталога не существует, значит файл архивировался без каталога, но сохранен с указанием пути.
                            // Создадим необходимую структуру каталогов.
                            // TODO Если нужно будет сохранять их в текущий каталог без вложенные, то нужно будет заменить directory.mkdirs() на код, который это сделает.
                            Files.createDirectories(file.toPath());
                        }
                    }
                    // разархивируем файл и сохраним на диске
                    Files.copy(zipInputStream, file.toPath());
                    file.setLastModified(zipEntry.getTime());
                }
            } while ((zipEntry = zipInputStream.getNextEntry()) != null);
        }
    }
}
