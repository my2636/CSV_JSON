import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**Метод parseCSV() вам необходимо реализовать самостоятельно. В этом вам поможет экземпляр класса CSVReader.
Передайте в его конструктор файловый ридер FileReader файла fileName. Данную операцию производите либо в блоке try-catch
с ресурсами, либо не забудьте закрыть поток после использования. Так же вам потребуется объект класса ColumnPositionMappingStrategy.
Используя объект стратении, укажите тип setType() и тип колонок setColumnMapping(). Далее создайте экземпляр CsvToBean
с использованием билдера CsvToBeanBuilder. При постройке CsvToBean используйте ранее созданный объект стратегии ColumnPositionMappingStrategy.
Созданный экземпляр объекта CsvToBean имеет метод parse(), который вернет вам список сотрудников.

Полученный список преобразуйте в строчку в формате JSON. Сделайте это с помощью метода listToJson(), который вам так же
предстоит реализовать самостоятельно.

String json = listToJson(list);
При написании метода listToJson() вам понадобятся объекты типа GsonBuilder и Gson. Для преобразования списка объектов
в JSON, требуется определить тип этого спика:

Type listType = new TypeToken<List<T>>() {}.getType();
Получить JSON из экземпляра класса Gson можно с помощью метода toJson(), передав в качестве аргументов список сотрудников и тип списка:

String json = gson.toJson(list, listType);
Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
В этом вам поможет FileWriter и его метод write().*/

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
    }

    static List<Employee> parseCSV(String[] columnMapping, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<>()

        } catch (Exception e) {

        }
    }
}
