package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import properties.abs.ApplicationPropertiesBase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: leeping
 * @Date: 2019/12/23 13:51
 */
public class XmlReadUtils {

    public interface XmlType{
        String getKey();
        String getValue();
    }

    private static class Xml_KV_Type implements XmlType{
        private final String key;
        private final String value;

        private Xml_KV_Type(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public static List<XmlType> readXml(InputStream is,String nodeName){
        List<XmlType> list = new ArrayList<>();
        try {
            //1.创建DocumentBuilderFactory对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document d = builder.parse(is);
            NodeList nodeList = d.getElementsByTagName(nodeName);
            nodeParse(nodeList,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void nodeParse(NodeList servletList,  List<XmlType> list) {

        for (int i = 0; i <servletList.getLength() ; i++) {
            Node node = servletList.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j <childNodes.getLength() ; j++) {
               //层级
                if (childNodes.item(j).getNodeType()==Node.ELEMENT_NODE) {
                    //节点
                    list.add(new Xml_KV_Type(childNodes.item(j).getNodeName(),childNodes.item(j).getFirstChild().getNodeValue()));
                }
            }
        }

    }
}
