package LavaLampJenkinsTest;

import org.junit.Test;

import org.joda.time.LocalTime;
import org.junit.Test;

import java.net.URL;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BuildIndicatorTest {
    @Test
    public void testTwoJobsWhenAllJobsOk() throws Exception {
        runTest(
                true,
                Arrays.asList("Lava Lamp app for Jenkins", "TestBankAccount"),
                new TreeMap<String, JobStatus>() {{
                    put("Lava Lamp app for Jenkins", JobStatus.ok);
                    put("TestBankAccount", JobStatus.ok);
                }},
                Arrays.asList(new Action(true, EventType.whenAllJobsOk))
        );
    }

    @Test
    public void testTwoJobsOkWhenAnyJobFails() throws Exception {
        runTest(
                false,
                Arrays.asList("Lava Lamp app for Jenkins", "TestBankAccount"),
                new TreeMap<String, JobStatus>() {{
                    put("Lava Lamp app for Jenkins", JobStatus.ok);
                    put("TestBankAccount", JobStatus.ok);
                }},
                Arrays.asList(new Action(true, EventType.whenAnyJobFails))
        );
    }

    @Test
    public void testTwoJobsOkFailedWhenAnyJobFails() throws Exception {
        runTest(
                true,
                Arrays.asList("Lava Lamp app for Jenkins", "TestBankAccount"),
                new TreeMap<String, JobStatus>() {{
                    put("Lava Lamp app for Jenkins", JobStatus.ok);
                    put("TestBankAccount", JobStatus.failed);
                }},
                Arrays.asList(new Action(true, EventType.whenAnyJobFails))
        );
    }

    @Test
    public void testTwoJobsOkWhenAnyJobUndefined() throws Exception {
        runTest(
                false,
                Arrays.asList("Lava Lamp app for Jenkins", "TestBankAccount"),
                new TreeMap<String, JobStatus>() {{
                    put("Lava Lamp app for Jenkins", JobStatus.ok);
                    put("TestBankAccount", JobStatus.ok);
                }},
                Arrays.asList(new Action(true, EventType.whenAnyJobUndefined))
        );
    }

    @Test
    public void testTwoJobsUnknownOkWhenAnyJobUndefined() throws Exception {
        runTest(
                true,
                Arrays.asList("Lava Lamp app for Jenkins", "TestBankAccount"),
                new TreeMap<String, JobStatus>() {{
                    put("Lava Lamp app for Jenkins", JobStatus.unknown);
                    put("TestBankAccount", JobStatus.ok);
                }},
                Arrays.asList(new Action(true, EventType.whenAnyJobUndefined))
        );
    }

    private void runTest(boolean lampState,
                         Collection<String> jobNames, final Map<String, JobStatus> jobStatusMap,
                         List<Action> actions) throws Exception {
        LampConfig config = new LampConfig(
                new URL("http://nothing.net"),
                0,
                LocalTime.now().minusMinutes(1),
                LocalTime.now().plusMinutes(1),
                true,
                Arrays.asList(new Lamp(
                        "green",
                        "desc",
                        "cmd-on",
                        "cmd-off",
                        jobNames,
                        actions)));
        JobStatusFetcher fetcherStub = new JobStatusFetcher() {
            @Override
            public Map<String, JobStatus> getJobStatus(Collection<String> jobNames) {
                return jobStatusMap;
            }
        };
        CommandControllerStub controller0 = new CommandControllerStub(config.getLamps().get(0).getName());
        BuildIndicator b = new BuildIndicator(config, fetcherStub, Arrays.asList((LampController) controller0));
        assertThat(controller0.getState(), is(false));
        b.check();
        assertThat(controller0.getState(), is(lampState));
    }
}
