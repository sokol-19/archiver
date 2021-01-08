package store.sokolov.arch;

import store.sokolov.arch.exception.ArchiveCorrupted;
import store.sokolov.arch.exception.NotFilesSpecified;
import store.sokolov.arch.exception.NotStreamSpecified;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Реализация этого интерфейса позволит заархивировать каталоги/файлы заданные в списке с передачей сжатых данные в поток и
 * разархивировать архив, полученный из потока в текущий каталог.
 * @author Vladimir Sokolov
 */
public interface Archiver {
    /**
     * Сжатие всех каталогов/файлов с учетом иерархии заданных списком <strong>fileNames</strong>.
     * Сжатые данные отправляются в указанный поток.
     * @param fileNames список каталогов/файлов для архивации
     * @param outputStream поток для передачи сжатых каталогов/файлов
     * @throws NotFilesSpecified выбрасывается при отсутствие каталогов/файлов для архивации
     * @throws NotStreamSpecified выбрасывается при отсутствие потока для передачи сжатых данных
     * @throws IOException выбрасывается при возникновении ошибок при работе с файлами
     */
    void zip(List<String> fileNames, OutputStream outputStream) throws NotFilesSpecified, IOException, NotStreamSpecified;

    /**
     * Метод для распаковки архива, переданного через указанный поток.
     * @param inputStream поток для чтения сжатых каталогов/файлов
     * @throws IOException выбрасывается при возникновении ошибок при работе с файлами
     * @throws ArchiveCorrupted выбрасывается, если архив поврежден
     * @throws NotStreamSpecified выбрасывается, если не задан входной стрим для чтения сжатых данных
     */
    void unZip(InputStream inputStream) throws IOException, ArchiveCorrupted, NotStreamSpecified;
}
