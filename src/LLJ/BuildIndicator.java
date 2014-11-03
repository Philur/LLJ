package LLJ;

import org.apache.log4j.Logger;
import org.joda.time.LocalTime;

import java.util.*;

/**
 * Controls build indicator lamps:
 * Red=one or more builds has failed
 * Green=All build are ok
 * <p/>
 * The class reads its configuration from a Configuration object.
 */
public class BuildIndicator {
    private static final Logger logger = Logger.getLogger(BuildIndicator.class);
    private LampConfig config;
    private SwedishHolidayExplorer holiday = new SwedishHolidayExplorer();
    private List<LampController> lampControllers;
    private JobStatusFetcher jobStatusFetcher;

    public BuildIndicator(LampConfig config, JobStatusFetcher jobStatusFetcher, List<LampController> lampControllers) {
        this.config = config;
        this.jobStatusFetcher = jobStatusFetcher;
        this.lampControllers = lampControllers;
        logger.info("Started CAG jenkinslamps, jenkinsUrl=" + config.getJenkinsUrl());
    }

    public void check() {
        Map<String, JobStatus> jobStatusMap = jobStatusFetcher.getJobStatus(findAllJobsNames(config));

        for (Lamp lamp : config.getLamps()) {
            LampController controller = findLampController(lampControllers, lamp);
            assert controller != null : "Felkonfat av programmeraren";
            LocalTime now = LocalTime.now();
            String s = holiday.getHoliday(Calendar.getInstance());
            if (s != null || now.isBefore(config.getTurnOnTime()) || now.isAfter(config.getTurnOffTime())) {
                // Holiday or outside office time
                controller.turnLamp(false);
            } else {
                for (Action action : lamp.getActions()) {
                    switch (action.getEvent()) {
                        case whenAllJobsOk:
                            if (allJobsOk(lamp.getJobNames(), jobStatusMap)) {
                                controller.turnLamp(action.isOn());
                            }
                            break;
                        case whenAnyJobFails:
                            if (anyJobFails(lamp.getJobNames(), jobStatusMap)) {
                                controller.turnLamp(action.isOn());
                            }
                            break;
                        case whenAnyJobUndefined:
                            if (anyJobUndefined(lamp.getJobNames(), jobStatusMap)) {
                                controller.turnLamp(action.isOn());
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unknown EventType: " + action.getEvent());
                    }
                }
            }
        }
    }

    private boolean allJobsOk(List<String> jobNames, Map<String, JobStatus> jobStatusMap) {
        for (String jobName : jobNames) {
            JobStatus s = jobStatusMap.get(jobName);
            assert s != null;
            if (s != JobStatus.ok) {
                return false;
            }
        }
        return true;
    }

    private boolean anyJobFails(List<String> jobNames, Map<String, JobStatus> jobStatusMap) {
        for (String jobName : jobNames) {
            JobStatus s = jobStatusMap.get(jobName);
            assert s != null;
            if (s == JobStatus.error || s == JobStatus.failed) {
                return true;
            }
        }
        return false;
    }

    private boolean anyJobUndefined(List<String> jobNames, Map<String, JobStatus> jobStatusMap) {
        for (String jobName : jobNames) {
            JobStatus s = jobStatusMap.get(jobName);
            assert s != null;
            if (s == JobStatus.unknown) {
                return true;
            }
        }
        return false;
    }

    private Set<String> findAllJobsNames(LampConfig config) {
        Set<String> result = new HashSet<String>();
        for (Lamp lamp : config.getLamps()) {
            result.addAll(lamp.getJobNames());
        }
        return result;
    }

    private LampController findLampController(List<LampController> controllers, Lamp lamp) {
        for (LampController c : controllers) {
            if (c.getName().equals(lamp.getName())) {
                return c;
            }
        }
        return null;
    }
}
