package com.wangxingxing.tools;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author : 王星星
 * date : 2023/9/13 16:25
 * email : 1099420259@qq.com
 * description : 读取Excel中的key，从XML文件中删除string内容
 */
public class DelStringFromXML {

    public static final String DIR_PATH = "D:\\AndroidProject\\PlayMods\\android\\language\\src\\main\\res\\"; // 指定要遍历的目录路径

    public static void main(String[] args) {
        List<String> keyList = readExcelFirstColumn();
        File dir = new File(DIR_PATH);
        listFiles(dir, keyList);
    }

    private static void delStringByKey(File file, List<String> keyList) {
        for (String searchKey : keyList) {
//            deleteLinesWithKey(file, searchKey);
            removeStringsWithKey(file.getAbsolutePath(), searchKey);
        }
        System.out.println("处理文件完成");
    }

    private static void deleteLinesWithKey(File file, String key) {
        try {
            File tempFile = new File(file.getAbsolutePath() + ".tmp");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains(key)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            writer.close();
            reader.close();

            if (!file.delete()) {
                System.err.println("Could not delete the original file.");
                return;
            }

            if (!tempFile.renameTo(file)) {
                System.err.println("Could not rename the temp file to the original file name.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void listFiles(File directory, List<String> keyList) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getAbsolutePath());
                    delStringByKey(file, keyList);
                } else if (file.isDirectory()) {
                    System.out.println("Directory: " + file.getAbsolutePath());
                    listFiles(file, keyList); // 递归遍历子目录
                }
            }
        }
    }

    private static List<String> readExcelFirstColumn() {
        String excelFilePath = "src/main/resources/strings261.xls"; // 指定您的Excel文件路径

        List<String> firstColumnData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new HSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表（如果有多个工作表）

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0); // 获取第一列的单元格

                if (cell != null) {
                    firstColumnData.add(cell.toString()); // 将单元格内容添加到数组中
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        firstColumnData.remove(0);

        // 打印数组中的内容
        for (String data : firstColumnData) {
            System.out.println(data);
        }

        return firstColumnData;
    }

    private static void removeStringsWithKey(String xmlFilePath, String keyToRemove) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(xmlFilePath));

            doc.getDocumentElement().normalize();

            NodeList resourcesList = doc.getElementsByTagName("resources");

            for (int i = 0; i < resourcesList.getLength(); i++) {
                Node resourcesNode = resourcesList.item(i);

                if (resourcesNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element resourcesElement = (Element) resourcesNode;
                    NodeList stringNodes = resourcesElement.getElementsByTagName("string");

                    for (int j = 0; j < stringNodes.getLength(); j++) {
                        Node stringNode = stringNodes.item(j);

                        if (stringNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element stringElement = (Element) stringNode;
                            String name = stringElement.getAttribute("name");

                            if (name.equals(keyToRemove)) {
                                // 找到要删除的元素，从 XML 中删除它
                                resourcesElement.removeChild(stringNode);
                            }
                        }
                    }
                }
            }

            // 保存修改后的 XML 到文件，去除空行
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // 去除 XML 声明

            // 添加以下两行以去除空行
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // 创建一个 StringWriter 以捕获转换后的 XML 内容
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            // 去除空行并将内容写入文件
            String transformedXml = writer.toString().replaceAll("(?m)^[ \t]*\r?\n", ""); // 去除空行
            try (PrintWriter fileWriter = new PrintWriter(new File(xmlFilePath))) {
                fileWriter.write(transformedXml);
            }

//            System.out.println("Key '" + keyToRemove + "' removed from " + xmlFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
