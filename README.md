# sort-it-for-CFT
Тестовое задание для "Focus Strart" ЦФТ

Эта программа сортирует слиянием предварительно отсортированные файлы и записывает данные в указанный файл.

Для запуска программы используйте командную строку.

Для начала скомпилируйте 3 java класса (Перейдите в папку со скачанными файлами, удерживая Shift, нажмите ПКМ на свободном месте в папке, и выберите "Открыть окно с помощью командной строки)

В командной строке наберите javac *.java

Проверьте что в папке появилось 3 скомпилированных файла с расширением .class

После этого наберите java Main (и набор аргументов, возможные аргументы приведены ниже)

Если на каком-то из этапов произошла ошибка, то возможно не выставлен нужный Path или Classpath в переменных среды. (Для помощи посетите https://www.java.com/ru/download/help/path.html или используйте среду разработки)

Аргументы для запуска:

-a или -d : режим сортировки по возрастанию или убыванию соответственно (если режим не указан, по умолчанию выбирается -a)

-i или -s : тип данных в файле. Целые числа или строки соответственно (необходимо обязательно указать)

Полный путь к файлу для вывода (Прим. D:\FocusStart\out.txt). Если файл по указанному пути не существует - программа создаст его автоматически.

Файлы с предварительно отсортированными данными (Прим. D:\FocusStart\in1.txt). Необходимо указать по крайней мере один. Указываются строго после указания файла для вывода.

Полный пример строки для запуска:

java Main -d -i D:\FocusStart\out.txt D:\FocusStart\in1.txt D:\FocusStart\in2.txt D:\FocusStart\in3.txt

Особенности реализации: 

В программе происходит проверка на предварительную сортировку в зависимости от выбранного режима типа данных и режима сортировки. Пример для целых чисел:

1313

182

1314

183

1316

В итоговую сортировку не войдут 182 и 183 так как они нарушают предварительную сортировку. Первое число в файле считается за верное, поэтому именно от него исходит дальнейшее сравнение.

Строки сравниваются по длине символов, реализация проверки работает по схожему принципу: первая строчка в файле всегда считается верной. Обнаруженные элементы, нарушающие предварительную сортировку будут выведены в консоль с указанием имени файла и элементом, с которым производилось сравнение.
