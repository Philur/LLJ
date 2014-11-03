package LLJ;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LampConfigReader {
    /**
     * Read a LampConfig object from an XML file
     *
     * @param configIs Configuration file
     * @return The MyData object
     */
    public static LampConfig read(InputStream configIs) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(configIs);
            Element root = doc.getRootElement();

            List<Lamp> lamps = parseLamps(root);

            return new LampConfig(
                    new URL(root.getChild("jenkinsUrl").getText()),
                    Integer.parseInt(root.getChild("pollTimeMsec").getText()),
                    LocalTime.parse(root.getChild("turnOn").getText()),
                    LocalTime.parse(root.getChild("turnOff").getText()),
                    root.getChild("activeHolidays").getText().equals("true"),
                    lamps);
        } catch (JDOMException e) {
            throw new IOException("Malformed XML file", e);
        }
    }

    private static List<Lamp> parseLamps(Element lampsElement) {
        Element lampCommandElement = lampsElement.getChild("lamps");
        List<Element> list = lampCommandElement.getChildren();
        List<Lamp> result = new ArrayList<Lamp>();
        for (Element cmdElement : list) {
            result.add(new Lamp(
                    cmdElement.getChild("name").getText(),
                    cmdElement.getChild("description").getText(),
                    cmdElement.getChild("onCommand").getText(),
                    cmdElement.getChild("offCommand").getText(),
                    parseJobNames(cmdElement.getChild("jobs")),
                    parseActions(cmdElement.getChild("actions"))
            ));
        }
        return result;
    }

    private static List<Action> parseActions(Element actionsElement) {
        List<Action> result = new ArrayList<Action>();
        List<Element> list = actionsElement.getChildren();
        for (Element e : list) {
            result.add(new Action(
                    e.getText().equals("on"),
                    EventType.valueOf(e.getName())
            )
            );
        }
        return result;
    }

    private static List<String> parseJobNames(Element jobsElement) {
        List<String> result = new ArrayList<String>();
        List<Element> list = jobsElement.getChildren();
        for (Element jobElement : list) {
            result.add(jobElement.getText());
        }
        return result;
    }
}