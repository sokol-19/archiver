## Архиватор
+ [Постановка задачи](readme.md#постановка-задачи)
+ [Краткое описание решения](readme.md#краткое-описание-решения)
+ [Возможные улучшения](readme.md#возможные-улучшения)
+ [Сборка приложения](readme.md#сборка-приложения)
+ [Запуск приложения](readme.md#запуск-приложения)

### Постановка задачи
Написать программу для архивации и деархивации файлов.
Должна принимать входными параметрами файлы и/или директории (разделение пробелом), которые необходимо заархивировать.
На выходе (pipe) должен формироваться файл, содержащий всю информацию о входных файлах.

Например:
./archiver ./test.file ./dir1 ./dir2 ./test2.file > archived

Для деархивации необходимо передать содержимое такого файла в pipe, не указывая входных файлов.
Файлы должны распаковываться в текущую директорию, откуда выполняется программа.

Например:
cat ./archived | ./archiver

Нужно поддержать работу с linux и macos.

Решение передать в виде ссылки на github, сборка maven или gradle, java версии 11 или 8 по желанию.

[к оглавлению](#readme)

### Краткое описание решения
Решение выполнено в виде консольного приложения. Для сборки приложения используется maven. 
Приложение работает в двух режимах:
+ сжатие;
+ разархивация.

Если в командной строке есть входные параметры, то приложение работает в режиме сжатия. 
В этом режиме приложение читает входные параметры из командной строки, 
архивирует каталоги/файлы в соответствии с прочитанными параметрами и 
отправляет сжатые данные в стандартный выходной поток **STDIN**.

Если в командной строке нет входных параметров, то приложение будет запущено в режиме разархивации.
В этом режиме приложение читает сжатые данные из стандартного входного потока **STDOUT** и 
сохраняет их в текущий каталог.

Для вывода ошибок используется стандартный поток для вывода ошибок **STDERR**.

[к оглавлению](#readme)

### Возможные улучшения
+ Текущая версия приложения не проверяет наличие таких каталогов/файлов на диске, которые она будет разархивировать. 
  Если такие каталоги/файлы уже есть на диске, они будут перезаписаны, то программа будет остановлена и архив не будет разархивирован. 
  Можно делать проверку и задавать вопрос, либо добавить ключ, который будет определять поведение программы.

  
+ Если для сжатия будет передан каталог с абсолютным путем, который находится в каталоге отличным от текущего, 
то при разархивации в текущем каталоге будет создан полный путь. Например, если программа запускается из каталога
  /home/user/temp, а архивируется каталог /home/user/test, то разахивации из каталога /home/user/temp, будет создан
  каталоги /home/user/temp/home/user/test. Можно сделать частичное схлопывание каталогов, например,
  /home/user/temp/test

  
+ Сделать возможность задавать ключи, например, степень сжатия и т.д.


+ Вынести детальные настройки в файл конфигурации.

[к оглавлению](#readme)

### Сборка приложения

Сборка jar архив приложения

    mvn clean install

[к оглавлению](#readme)

### Запуск приложения

Пример запуска приложения для сжатия каталогов/файлов:

    java -jar ./arch-1.0.0.jar ./test.file ./dir1 ./dir2 ./test2.file > archived.zip

Пример запуска приложения для разархивации:

    cat archived.zip | java -jar ./arch-1.0.0.jar

[к оглавлению](#readme)
