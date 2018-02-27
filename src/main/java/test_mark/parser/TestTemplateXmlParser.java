package test_mark.parser;

import test_mark.test_template.TestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.text.SimpleDateFormat;

public class TestTemplateXmlParser {
    private JAXBContext jaxbContext;

    public TestTemplateXmlParser() {
    }

    public TestTemplate readTestTemplate(File testTemplateFile) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(TestTemplate.class);
        Unmarshaller jaxbUnmarshaller;
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (TestTemplate) jaxbUnmarshaller.unmarshal(testTemplateFile);
    }

    public void writeTestTemplate(TestTemplate testTemplate, String directoryPath) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(TestTemplate.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File testTemplateFile = new File(directoryPath, generateTestTemplateFilename(testTemplate));
        jaxbMarshaller.marshal(testTemplate, testTemplateFile);
    }

    private String generateTestTemplateFilename(TestTemplate testTemplate) {
        StringBuilder sb = new StringBuilder(testTemplate.getCourseName() + "_" + testTemplate.getExamName());
        for (int index = 0; index < sb.length(); index++) {
            char c = sb.charAt(index);
            if (Character.isUpperCase(c))
                sb.setCharAt(index, Character.toLowerCase(c));
        }

        return "test_template_" + sb.toString().replaceAll(" ", "_") + "_"
                + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(testTemplate.getCreationDate()) + ".xml";
    }
}
