/**
 * 
 */
package LavaLampJenkins;

import org.joda.time.LocalTime;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

enum EventType {whenAllJobsOk, whenAnyJobFails, whenAnyJobUndefined}

class Action {
    private boolean on;
    private EventType event;

    Action(boolean on, EventType event) {
        this.on = on;
        this.event = event;
    }

    boolean isOn() {
        return on;
    }

    EventType getEvent() {
        return event;
    }
}

class Lamp {
    private String name;
    private String description;
    private String onCommand;
    private String offCommand;
    private List<String> jobNames = new ArrayList<String>();
    private List<Action> actions = new ArrayList<Action>();

    Lamp(String name, String description, String onCommand, String offCommand, Collection<String> jobNames, List<Action> actions) {
        this.name = name;
        this.description = description;
        this.onCommand = onCommand;
        this.offCommand = offCommand;
        this.jobNames = new ArrayList<String>(jobNames);
        this.actions = actions;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    String getOnCommand() {
        return onCommand;
    }

    String getOffCommand() {
        return offCommand;
    }

    List<String> getJobNames() {
        return jobNames;
    }

    List<Action> getActions() {
        return actions;
    }
}

public class LampConfig {
    private URL jenkinsUrl;
    private int pollTimeMsec;
    private LocalTime turnOnTime;
    private LocalTime turnOffTime;
    private boolean activeHolidays;
    private List<Lamp> lamps = new ArrayList<Lamp>();

    public LampConfig(URL jenkinsUrl, int pollTimeMsec, LocalTime turnOnTime, LocalTime turnOffTime,
                      boolean activeHolidays, List<Lamp> lamps) {
        this.jenkinsUrl = jenkinsUrl;
        this.pollTimeMsec = pollTimeMsec;
        this.turnOnTime = turnOnTime;
        this.turnOffTime = turnOffTime;
        this.activeHolidays = activeHolidays;
        this.lamps = lamps;
    }

    public URL getJenkinsUrl() {
        return jenkinsUrl;
    }

    public int getPollTimeMsec() {
        return pollTimeMsec;
    }

    public LocalTime getTurnOnTime() {
        return turnOnTime;
    }

    public LocalTime getTurnOffTime() {
        return turnOffTime;
    }

    public boolean isActiveHolidays() {
        return activeHolidays;
    }

    public List<Lamp> getLamps() {
        return lamps;
    }
}