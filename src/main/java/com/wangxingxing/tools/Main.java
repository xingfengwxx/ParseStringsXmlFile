package com.wangxingxing.tools;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        try {
            File xmlFile = new File("src/main/resources/strings.xml");
            Workbook workbook = new XSSFWorkbook();
            parseStringsXml(xmlFile, workbook);

            FileOutputStream outputStream = new FileOutputStream("src/main/resources/output.xlsx");
            workbook.write(outputStream);
            workbook.close();

            System.out.println("Output saved to output.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseStringsXml(File xmlFile, Workbook workbook) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("string");

            Sheet sheet = workbook.createSheet(xmlFile.getName());
            int rowIdx = 0;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String key = element.getAttribute("name");
                    String content = element.getTextContent();

                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(key);
                    row.createCell(1).setCellValue(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}