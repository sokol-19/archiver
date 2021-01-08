package store.sokolov.arch.exception;

/**
 * Класс определения исключения, которое будет выбрасываться при отсутствие потоков
 * @author Vladimir Sokolov
 */
public class NotStreamSpecified extends Exception {
    public NotStreamSpecified(String message) {
        super(message);
    }
}
