package store.sokolov.arch.exception;

/**
 * Класс определения исключения, которое будет выбрасываться при невозможности разархивировать
 * @author Vladimir Sokolov
 */
public class ArchiveCorrupted extends Exception {
    public ArchiveCorrupted(String message) {
        super(message);
    }
}
