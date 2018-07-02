package com.doctor.finder;


import com.doctor.finder.model.Specialty;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class XMLReaderDOM {

    public static void main(String[] args) {
        String filePath = "specialties.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("Specialties");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Specialty> empList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                empList.add(getEmployee(nodeList.item(i)));
            }
            //lets print Employee list information
            for (Specialty emp : empList) {
                System.out.println(emp.toString());
            }
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }


    private static Specialty getEmployee(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Specialty emp = new Specialty();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setActor(getTagValue("actor", element));
            emp.setActors(getTagValue("actors", element));
            emp.setName(getTagValue("name", element));
            emp.setDescription(getTagValue("description", element));
            emp.setUid(getTagValue("uid", element));
        }

        return emp;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node =  nodeList.item(0);
        return node.getNodeValue();
    }

}