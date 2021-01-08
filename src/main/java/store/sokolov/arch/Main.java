package store.sokolov.arch;

import store.sokolov.arch.exception.ArchiveCorrupted;
import store.sokolov.arch.exception.NotFilesSpecified;
import store.sokolov.arch.exception.NotStreamSpecified;

import java.util.Arrays;

/**
 * Стартовый класс
 */
public class Main {

    /**
     * Стартовый метод для запуска архиватора.
     * <p>Если есть входные параметры, то запускается сжатие с выводом результата в стандартный поток вывода,
     * иначе разархивация данных, поступающих из стандартного входного потока.
     *
     * @param args входные параметры
     */
    public static void main(String[] args) {
        try {
            Archiver archiver = new ZipArchiver();
            if (args == null || args.length == 0) {
                // разархивация
                archiver.unZip(System.in);
            } else {
                // архивация
                archiver.zip(Arrays.asList(args), System.out);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
