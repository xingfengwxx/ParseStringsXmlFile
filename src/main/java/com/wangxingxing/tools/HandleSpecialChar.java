package com.wangxingxing.tools;

import org.apache.commons.lang3.StringEscapeUtils;
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
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class HandleSpecialChar {

    public static final String DIR_PATH = "D:\\AndroidProject\\PM\\android\\language\\src\\main\\res\\";

    public static final String DIR_RES = "src/main/resources/handle_special_char";

    public static void main(String[] args) {
        File stringsDir = new File(DIR_PATH);
        traverseFolder(stringsDir);
//        handleAMP(stringsDir);

//        testHandleSpecialChar();

//        getUnescapedContentInStringsXML();

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
                        if (file.getName().startsWith("strings") && file.getName().contains("263")) {
                            // 执行转义处理
                            processStringsXML(file);
//                            processStringsXMLReplace(file);
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

                    String escapedContent;
                    if (content.contains("'")) {
                        // 只对含有撇号（'）的进行转义处理
                        escapedContent = StringEscapeUtils.escapeXml10(content);
                    } else {
                        escapedContent = content;
                    }

//                    System.out.println("escape content: " + escapedContent);

                    // 更新字符串内容
                    stringElement.setTextContent(escapedContent);
                }
            }

            // 将更新后的XML保存到文件
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

    public static void processStringsXMLReplace(File xmlFile) {
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
                    System.out.println("content: " + content);

                    // 转义处理
                    String replaceContent = content.replace("&amp;", "&");

                    System.out.println("escape content: " + replaceContent);

                    // 更新字符串内容
                    stringElement.setTextContent(replaceContent);
                }
            }

            // 将更新后的XML保存到文件
            // 如果需要保存到不同文件，可以创建一个新的Document并写入到新文件
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("替换【&amp;】处理完成: " + xmlFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testHandleSpecialChar() {
        String input = "Hello & World aaa \' aaa>";
        String escaped = StringEscapeUtils.escapeXml(input);
        System.out.println(escaped);
    }


    public static void getUnescapedContentInStringsXML() {
        String xmlFilePath = "D:\\IdeaProjects\\ParseStringsXmlFile\\ParseStringsXmlFile\\src\\main\\resources\\handle_special_char\\strings_230.xml"; // 指定要解析的 strings.xml 文件路径

        try {
            File inputFile = new File(xmlFilePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // 解析 XML 文件
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // 获取所有 <string> 标签
            NodeList stringNodes = doc.getElementsByTagName("string");

            for (int i = 0; i < stringNodes.getLength(); i++) {
                Element stringElement = (Element) stringNodes.item(i);

                // 检查 <string> 标签内是否存在 CDATA 部分
                NodeList cdataNodes = stringElement.getElementsByTagName("![CDATA[");

                if (cdataNodes.getLength() > 0) {
                    Node cdataNode = cdataNodes.item(0);
                    String cdataContent = cdataNode.getTextContent();

                    System.out.println("在 <string> 标签中发现 CDATA 部分:");
                    System.out.println("String Name: " + stringElement.getAttribute("name"));
                    System.out.println("CDATA Content: " + cdataContent);
                } else {
                    // 如果没有 CDATA 部分，则获取经过转义的文本内容
                    String stringContent = stringElement.getTextContent();
                    System.out.println("String Name: " + stringElement.getAttribute("name"));
                    System.out.println("String Content: " + stringContent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void handleAMP(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归遍历子文件夹
                        handleAMP(file);
                    } else {
                        // 处理文件
                        System.out.println("文件: " + file.getAbsolutePath());

                        // 在这里可以对文件进行操作，例如读取、写入或其他处理
                        // 例如，如果文件是strings.xml，可以在此处添加对其内容的转义处理
                        if (file.getName().startsWith("strings") && file.getName().contains("261")) {
                            // 执行转义处理
                            rewriteXML(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private static void rewriteXML(String xmlFilePath) {
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

                            // 获取字符串的内容
                            String content = stringElement.getTextContent();
                            System.out.println("content: " + content);

                            // 转义处理
                            String replaceContent = content.replace("&amp;", "&");

                            System.out.println("escape content: " + replaceContent);

                            // 更新字符串内容
                            stringElement.setTextContent(replaceContent);
                        }
                    }
                }
            }

            // 保存修改后的 XML 到文件，去除空行
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

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

            System.out.println("重新写入文件：" + xmlFilePath);
//            System.out.println("Key '" + keyToRemove + "' removed from " + xmlFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

