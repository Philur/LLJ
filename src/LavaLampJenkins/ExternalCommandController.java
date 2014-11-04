package LavaLampJenkins;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Controls lamps with an external OS command.
 */
public class ExternalCommandController implements LampController {
    private final static Logger logger = Logger.getLogger(ExternalCommandController.class);
    private String name;
    private String commandStrOn;
    private String commandStrOff;

    public ExternalCommandController(String name, String commandStrOn, String commandStrOff) {
        this.name = name;
        this.commandStrOn = commandStrOn;
        this.commandStrOff = commandStrOff;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void turnLamp(boolean on) {
        String cmdLine = on ? commandStrOn : commandStrOff;
        try {
            logger.debug("name=" + name + ",cmdLine=" + cmdLine);
            Process p = Runtime.getRuntime().exec(cmdLine);
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                logger.debug(name + ":Failed to execute command", e);
            }
        } catch (IOException e) {
            logger.debug(name + ":Failed to execute command", e);
        }
    }
}