package fun.onysakura.algorithm.utils.core.basic;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
public class XmlUtils {

    /**
     * xml 转换为 map
     */
    public static Map<String, Object> xmlToMap(String xml) throws Exception {
        Document document = newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
        return parseElement(document.getDocumentElement());
    }

    /**
     * map 转换为 xml
     *
     * @param data map
     */
    public static String mapToXml(Map<String, ?> data) throws Exception {
        return mapToXml(data, "xml", false);
    }

    /**
     * map 转换为 xml
     *
     * @param data         map
     * @param rootNodeName 根节点名称
     * @param indent       是否缩进
     */
    public static String mapToXml(Map<String, ?> data, String rootNodeName, boolean indent) throws Exception {
        Document document = newDocument();
        Element root = document.createElement(rootNodeName);
        document.appendChild(root);
        buildElement(document, root, data);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, String.valueOf(indent));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        writer.close();
        return output;
    }

    private static void buildElement(Document document, Element root, Map<String, ?> map) throws Exception {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            Element element = document.createElement(key);
            if (value == null) {
                element.appendChild(document.createTextNode(""));
            } else if (value instanceof Map) {
                Map<String, Object> valueMap = new HashMap<>();
                Map<?, ?> rawMap = (Map) value;
                for (Object o : rawMap.keySet()) {
                    valueMap.put(String.valueOf(o), rawMap.get(o));
                }
                buildElement(document, element, valueMap);
            } else {
                element.appendChild(document.createTextNode(String.valueOf(value)));
            }
            root.appendChild(element);
        }
    }

    private static Map<String, Object> parseElement(Element element) {
        HashMap<String, Object> map = new HashMap<>();
        String key = element.getNodeName();
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            // 获得结点的类型
            short nodeType = node.getNodeType();
            switch (nodeType) {
                case Node.ELEMENT_NODE:
                    // 是元素，继续递归
                    Map<String, Object> value = parseElement((Element) node);
                    if (map.containsKey(key)) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> oldValue = (HashMap<String, Object>) map.get(key);
                        oldValue.putAll(value);
                    } else {
                        map.put(key, value);
                    }
                    break;
                case Node.CDATA_SECTION_NODE:
                case Node.TEXT_NODE:
                    map.put(key, node.getNodeValue());
                    break;
                case Node.COMMENT_NODE:
                default:
                    System.out.println("others: " + node.toString());
                    break;
            }
        }
        if (children.getLength() == 0) {
            map.put(key, "NULL");
        }
        return map;
    }

    /**
     * 生成DOM
     */
    private static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    /**
     * DOM文档生成器
     */
    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory.newDocumentBuilder();
    }
}
