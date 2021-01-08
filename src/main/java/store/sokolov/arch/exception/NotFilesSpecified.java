package store.sokolov.arch.exception;

/**
 * Класс определения исключения, которое будет выбрасываться при отсутствие каталогов/файлов для архивации
 * @author Vladimir Sokolov
 */
public class NotFilesSpecified extends Exception {
    public NotFilesSpecified(String message) {
        super(message);
    }
}
