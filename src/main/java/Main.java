import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
 * в JSON, требуется определить тип этого списка:
 * <p>
 * Type listType = new TypeToken<List<T>>() {}.getType();
 * Получить JSON из экземпляра класса Gson можно с помощью метода toJson(), передав в качестве аргументов список сотрудников и тип списка:
 * <p>
 * String json = gson.toJson(list, listType);
 * Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
 * В этом вам поможет FileWriter и его метод write().
 * -----------------
 * Для получения списка сотрудников из XML документа используйте метод parseXML():
 * <p>
 * List<Employee> list = parseXML("data.xml");
 * При реализации метода parseXML() вам необходимо получить экземпляр класса Document с использованием DocumentBuilderFactory
 * и DocumentBuilder через метод parse(). Далее получите из объекта Document корневой узел Node с помощью метода getDocumentElement().
 * Из корневого узла извлеките список узлов NodeList с помощью метода getChildNodes(). Пройдитесь по списку узлов и
 * получите из каждого из них Element. У элементов получите значения, с помощью которых создайте экземпляр класса Employee.
 * Так как элементов может быть несколько, организуйте всю работу в цикле. Метод parseXML() должен возвращать список сотрудников.
 * <p>
 * С помощью ранее написанного метода listToJson() преобразуйте список в JSON и запишите его в файл c помощью метода writeString().
 */

public class Main {
    static int writingCount = 1;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        // парсинг из csv в json, запись
        List<Employee> CSVEmployeesList = parseCSV(columnMapping, fileName);
        String jsonString = listToJson(CSVEmployeesList);
        writeString(jsonString);

        // парсинг из xml в json, запись
        List<Employee> XMLEmployeesList = parseXML("data.xml");
        String xmlJsonString = listToJson(XMLEmployeesList);
        writeString(xmlJsonString);
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
        try {
            String pathForWriteFile = "data.json";
            if (!(writingCount == 1)) {
                String[] split = pathForWriteFile.split("\\.");
                pathForWriteFile = split[0] + writingCount + "." + split[1];
            }
            FileWriter writer = new FileWriter(pathForWriteFile);
            writer.write(jsonString);
            writer.close();
            writingCount++;

        } catch (Exception e) {
            throw new IOException("Не удалось сохранить файл: " + e.getMessage());
        }
    }

    static List<Employee> parseXML(String filePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filePath));
        Node root = doc.getDocumentElement();
        return getEmployees(root);
    }

    /**
     * При реализации метода parseXML() вам необходимо получить экземпляр класса Document с использованием DocumentBuilderFactory
     * и DocumentBuilder через метод parse(). Далее получите из объекта Document корневой узел Node с помощью метода getDocumentElement().
     * Из корневого узла извлеките список узлов NodeList с помощью метода getChildNodes().   Пройдитесь по списку узлов и
     * получите из каждого из них Element. У элементов получите значения, с помощью которых создайте экземпляр класса Employee.
     * Так как элементов может быть несколько, организуйте всю работу в цикле. Метод parseXML() должен возвращать список сотрудников.
     * С помощью ранее написанного метода listToJson() преобразуйте список в JSON и запишите его в файл c помощью метода writeString().
     */

    private static List<Employee> getEmployees(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<Employee> employeeList = new ArrayList<>(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                NodeList list2 = element.getChildNodes();
                String[] elementContent = new String[list2.getLength()];

                for (int j = 0; j < list2.getLength(); j++) {
                    String content = list2.item(j).getTextContent();
                    elementContent[j] = content;
                }

                Employee employee = new Employee(Long.parseLong(elementContent[1]), elementContent[3], elementContent[5],
                        elementContent[7], Integer.parseInt(elementContent[9]));
                employeeList.add(employee);
            }
        }

        return employeeList;
    }
}
// if (Node.ELEMENT_NODE == node.getNodeType() && node.getNodeName().equals("employee")