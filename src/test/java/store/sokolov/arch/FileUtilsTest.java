package store.sokolov.arch;

import org.junit.jupiter.api.*;
import store.sokolov.arch.data.PrepareData;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static store.sokolov.arch.data.PrepareData.*;

class FileUtilsTest {

    @BeforeAll
    static void setUpAll() throws IOException {
        // Создаем тестовые данные
        PrepareData.createTestData();
    }

    @AfterAll
    static void tearDownAll() {
        // удаляем тестовые данные
        removeAllTestFiles(new File(testDirLevel1));
    }

    @Test
    void getAllFilesInfo() throws IOException {
        Map<String, File> testFilesMap = new TreeMap<>();
        Map<String, File> filesMap = FileUtils.getAllFilesInfo(null);
        assertEquals(testFilesMap, filesMap);
        assertEquals(filesMap.size(),  0);

        filesMap = FileUtils.getAllFilesInfo(new ArrayList<>());
        assertEquals(testFilesMap, filesMap);
        assertEquals(filesMap.size(), 0);

        testFilesMap.put(testDirLevel1, new File(testDirLevel1));
        testFilesMap.put(testDirLevel2, new File(testDirLevel2));
        testFilesMap.put(testDirLevel3, new File(testDirLevel3));
        testFilesMap.put(pathToTestFile, new File(pathToTestFile));
        filesMap = FileUtils.getAllFilesInfo(new ArrayList<>(Arrays.asList(testDirLevel1, pathToTestFile)));
        assertEquals(testFilesMap, filesMap);
        assertEquals(filesMap.size(), 4);

        assertThrows(FileNotFoundException.class, () -> FileUtils.getAllFilesInfo(new ArrayList<>(Arrays.asList(testDirLevel1, pathToTestFile, UUID.randomUUID().toString()))));
    }

    @Test
    void getFilesInfoStart() throws IOException {
        assertThrows(NullPointerException.class, () -> FileUtils.getFilesInfoStart(null));
        assertThrows(FileNotFoundException.class, () -> FileUtils.getFilesInfoStart(UUID.randomUUID().toString()));

        Map<String, File> filesMap = FileUtils.getFilesInfoStart(pathToTestFile);
        Map<String, File> testFilesMap = new TreeMap<>();
        testFilesMap.put(pathToTestFile, testFile);
        FileUtils.getFilesInfo(testFile, filesMap);
        assertEquals(filesMap, testFilesMap);
    }

    @Test
    void getFilesInfo() throws IOException {
        assertThrows(NullPointerException.class, () -> FileUtils.getFilesInfo(null, null));

        Map<String, File> filesMap = new TreeMap<>();
        Map<String, File> testFilesMap = new TreeMap<>();
        testFilesMap.put(pathToTestFile, testFile);
        FileUtils.getFilesInfo(testFile, filesMap);
        assertEquals(filesMap, testFilesMap);

        filesMap = new TreeMap<>();
        testFilesMap = new TreeMap<>();
        String testFilePath111 = UUID.randomUUID().toString();
        testFilesMap.put(testFilePath111, new File(testFilePath111));
        FileUtils.getFilesInfo(new File(testFilePath111), filesMap);
        assertEquals(filesMap, testFilesMap);
    }

    @Test
    void getRelativePath() {
        assertEquals("", FileUtils.getRelativePath(null));
        assertEquals("", FileUtils.getRelativePath(""));
        assertEquals("", FileUtils.getRelativePath(currentDir));
        assertEquals("test", FileUtils.getRelativePath(currentDir + File.separator + "test"));
        assertEquals("test.txt", FileUtils.getRelativePath(currentDir + File.separator + "test.txt"));
        assertEquals(File.separator + "test1" + File.separator + "test2" + File.separator + "test.txt"
                , FileUtils.getRelativePath(File.separator + "test1" + File.separator + "test2" + File.separator + "test.txt"));
    }
}