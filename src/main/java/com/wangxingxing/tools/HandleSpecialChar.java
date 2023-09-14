package com.wangxingxing.tools;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class HandleSpecialChar {

    public static final String DIR_PATH = "D:\\AndroidProject\\PlayMods\\android\\language\\src\\main\\res\\";

    public static final String DIR_RES = "src/main/resources/handle_special_char";

    public static void main(String[] args) {
        File stringsDir = new File(DIR_PATH);
        traverseFolder(stringsDir);

//        testHandleSpecialChar();
    }

    public static void traverseFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归遍历子文件夹
                        traverseFolder(file);
                    } else {
                        // 处理文件
                        System.out.println("文件: " + file.getAbsolutePath());

                        // 在这里可以对文件进行操作，例如读取、写入或其他处理
                        // 例如，如果文件是strings.xml，可以在此处添加对其内容的转义处理
                        if (file.getName().startsWith("strings") && file.getName().contains("261")) {
                            // 执行转义处理
                            processStringsXML(file);
                        }
                    }
                }
            }
        }
    }

    public static void processStringsXML(File xmlFile) {
        try {
            // 加载strings.xml文件
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // 获取根元素
            Element root = document.getDocumentElement();

            // 遍历<resources>下的子节点
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // 只处理<string>元素
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("string")) {
                    Element stringElement = (Element) node;

                    // 获取字符串的内容
                    String content = stringElement.getTextContent();
//                    System.out.println("content: " + content);

                    // 转义处理
                    String escapedContent = StringEscapeUtils.escapeXml(content);

                    // 更新字符串内容
                    stringElement.setTextContent(escapedContent);
                }
            }

            // 将更新后的XML保存到文件
            // 这里假设保存到同一个文件
            // 如果需要保存到不同文件，可以创建一个新的Document并写入到新文件
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("转义处理完成: " + xmlFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testHandleSpecialChar() {
        String input = "Hello & World aaa \' aaa";
        String escaped = StringEscapeUtils.escapeXml(input);
        System.out.println(escaped);
    }
}

