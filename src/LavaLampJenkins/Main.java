package LavaLampJenkins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Java client to set static of lamps depending on Jenkins build status.
 * <p/>
 * Command line parameters: <configFileName> Path to configuration file. If omitted, the file "jenkinslamps.properties"
 * is assumed in cwd.
 */
public class Main {
    public static void main(String[] args) {
        String fileName;
        if (args.length > 0) {
            fileName = args[0];
        } else {
            fileName = "jenkinslamps.properties";
        }
        try {
            LampConfig config = LampConfigReader.read(new FileInputStream(new File(fileName)));

            List<LampController> lampControllers = new ArrayList<LampController>();
            for (Lamp lamp : config.getLamps()) {
                lampControllers.add(new ExternalCommandController(lamp.getName(), lamp.getOnCommand(), lamp.getOffCommand()));
            }
            BuildIndicator controller = new BuildIndicator(
                    config,
                    new JenkinsJobStatusFetcher(config.getJenkinsUrl()),
                    lampControllers);
            while (true) {
                controller.check();
                try {
                    Thread.sleep(config.getPollTimeMsec());
                } catch (InterruptedException ignore) {
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read config file " + fileName + ":" + e.getMessage());
        }
    }
}