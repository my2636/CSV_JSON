import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Метод parseCSV() вам необходимо реализовать самостоятельно. В этом вам поможет экземпляр класса CSVReader.
 * Передайте в его конструктор файловый ридер FileReader файла fileName. Данную операцию производите либо в блоке try-catch
 * с ресурсами, либо не забудьте закрыть поток после использования. Так же вам потребуется объект класса ColumnPositionMappingStrategy.
 * Используя объект стратении, укажите тип setType() и тип колонок setColumnMapping(). Далее создайте экземпляр CsvToBean
 * с использованием билдера CsvToBeanBuilder. При постройке CsvToBean используйте ранее созданный объект стратегии ColumnPositionMappingStrategy.
 * Созданный экземпляр объекта CsvToBean имеет метод parse(), который вернет вам список сотрудников.
 * <p>
 * Полученный список преобразуйте в строчку в формате JSON. Сделайте это с помощью метода listToJson(), который вам так же
 * предстоит реализовать самостоятельно.
 * String json = listToJson(list);
 * <p>
 * При написании метода listToJson() вам понадобятся объекты типа GsonBuilder и Gson. Для преобразования списка объектов
 * в JSON, требуется определить тип этого спиcка:
 * <p>
 * Type listType = new TypeToken<List<T>>() {}.getType();
 * Получить JSON из экземпляра класса Gson можно с помощью метода toJson(), передав в качестве аргументов список сотрудников и тип списка:
 * <p>
 * String json = gson.toJson(list, listType);
 * Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
 * В этом вам поможет FileWriter и его метод write().
 */

public class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String jsonString = listToJson(list);
        System.out.println(jsonString);
        writeString(jsonString);

    }

    static List<Employee> parseCSV(String[] columnMapping, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csvToBean.parse();

        } catch (Exception e) {
            throw new RuntimeException("Произошла ошибка при парсинге CSV: " + e.getMessage());
        }
    }

    static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    static void writeString(String jsonString) throws IOException {
        try (FileWriter writer = new FileWriter("EmployeeJSONFile.json")) {
            writer.write(jsonString);
        } catch (Exception e) {
            throw new IOException("Не удалось сохранить файл: " + e.getMessage());
        }
    }
}
