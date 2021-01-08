package store.sokolov.arch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.sokolov.arch.data.PrepareData;
import store.sokolov.arch.exception.ArchiveCorrupted;
import store.sokolov.arch.exception.NotFilesSpecified;
import store.sokolov.arch.exception.NotStreamSpecified;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static store.sokolov.arch.data.PrepareData.*;

class ZipArchiverTest {
    private ZipArchiver zipArchiver;

    @BeforeEach
    void setUp() throws IOException {
        // Создаем тестовые данные
        PrepareData.createTestData();
        zipArchiver = new ZipArchiver();
    }

    @AfterEach
    void tearDown() {
        // удаляем тестовые данные
        removeAllTestFiles(new File(testDirLevel1));
    }

    @Test
    void zip() throws IOException, NotFilesSpecified, NotStreamSpecified {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        assertThrows(NotFilesSpecified.class, () -> zipArchiver.zip(null, byteArrayOutputStream));
        assertThrows(NotFilesSpecified.class, () -> zipArchiver.zip(new ArrayList<>(), byteArrayOutputStream));
        assertThrows(NotStreamSpecified.class, () -> zipArchiver.zip(Arrays.asList(testDirLevel1), null));

        // перенаправим стандартный выходной поток в ByteArrayOutputStream
        zipArchiver.zip(Arrays.asList(testDirLevel1, pathToTestFile), byteArrayOutputStream);
        assertTrue(byteArrayOutputStream.size() > 0);
    }

    @Test
    void unZipRubbish() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Тестирование".getBytes(StandardCharsets.UTF_8));
        assertThrows(ArchiveCorrupted.class, () -> zipArchiver.unZip(byteArrayInputStream));
    }

    @Test
    void unZip() throws IOException, NotFilesSpecified, NotStreamSpecified {
        assertThrows(NotStreamSpecified.class, () -> zipArchiver.unZip(null));
        //сформируем правильный архив
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zipArchiver.zip(Arrays.asList(testDirLevel1, pathToTestFile), byteArrayOutputStream);

        // сохраним путь к тестовому файлу
        String pathToTestFileBeforeZip = testFile.getPath();
        // удалим каталог вместе с подкаталогами и тестовым файлом, который заархивировали
        removeAllTestFiles(new File(testDirLevel1));

        // передадим сформированный архив в стандартный входной поток
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        assertDoesNotThrow(() -> zipArchiver.unZip(byteArrayInputStream));

        // получим файл после разархивации, проверим его наличие и совпадения путей
        File testFileAfterUnZip = new File(pathToTestFile);
        assertTrue(testFileAfterUnZip.exists());
        String pathToTestFileAfterUnZip = testFileAfterUnZip.getPath();
        assertEquals(pathToTestFileBeforeZip, pathToTestFileAfterUnZip);

        // сравним тело файла после распаковки
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(testFileAfterUnZip.toPath(), baos);
        assertEquals(bodyTestFile, baos.toString());
    }
}