package LavaLampJenkinsTest;

import org.joda.time.LocalTime;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import LavaLampJenkins.LampConfig;
import LavaLampJenkins.LampConfigReader;

/*
 * Created by u017121
 */
public class LampConfigReaderTest {
    @Test
    public void testRead() throws Exception {
        LampConfig c = LampConfigReader.read(getClass().getResourceAsStream("/config.xml"));
        assertThat(c.getJenkinsUrl(), is(new URL("http://ec2-54-171-84-56.eu-west-1.compute.amazonaws.com/api/xml/")));
        assertThat(c.getPollTimeMsec(), is(10000));
        assertThat(c.getTurnOnTime(), is(LocalTime.parse("08:00:00")));
        assertThat(c.getTurnOffTime(), is(LocalTime.parse("17:00:00")));
        assertThat(c.isActiveHolidays(), is(true));

        List<Lamp> lamps = c.getLamps();
        assertThat(lamps.size(), is(2));
        {
            Lamp lamp = lamps.get(0);
            assertThat(lamp.getName(), is("green"));
            assertThat(lamp.getDescription(), is("Turn on green lamp when job TestBankAccount is ok"));
            assertThat(lamp.getOnCommand(), is("tdtool --on 1"));
            assertThat(lamp.getOffCommand(), is("tdtool --off 1"));
            assertThat(lamp.getJobNames().size(), is(1));
            assertThat(lamp.getJobNames().get(0), is("TestBankAccount"));

            List<Action> actions = lamp.getActions();
            assertThat(actions.size(), is(3));
            assertThat(actions.get(0).getEvent(), is(EventType.whenAllJobsOk));
            assertThat(actions.get(0).isOn(), is(true));
            assertThat(actions.get(1).getEvent(), is(EventType.whenAnyJobFails));
            assertThat(actions.get(1).isOn(), is(false));
            assertThat(actions.get(2).getEvent(), is(EventType.whenAnyJobUndefined));
            assertThat(actions.get(2).isOn(), is(true));
        }
        {
            Lamp lamp = lamps.get(1);
            assertThat(lamp.getName(), is("red"));
            assertThat(lamp.getDescription(), is("Turn on red lamp when job TestBankAccount has failed"));
            assertThat(lamp.getOnCommand(), is("tdtool --on 2"));
            assertThat(lamp.getOffCommand(), is("tdtool --off 2"));
            assertThat(lamp.getJobNames().size(), is(1));
            assertThat(lamp.getJobNames().get(0), is("TestBankAccount"));
            //assertThat(lamp.getJobNames().get(1), is("unit-test2"));

            List<Action> actions = lamp.getActions();
            assertThat(actions.size(), is(3));
            assertThat(actions.get(0).getEvent(), is(EventType.whenAllJobsOk));
            assertThat(actions.get(0).isOn(), is(false));
            assertThat(actions.get(1).getEvent(), is(EventType.whenAnyJobUndefined));
            assertThat(actions.get(1).isOn(), is(true));
            assertThat(actions.get(2).getEvent(), is(EventType.whenAnyJobFails));
            assertThat(actions.get(2).isOn(), is(true));
        }
    }
	@Test
    public void testReadHeader() throws Exception {
        LampConfig c = LampConfigReader.read(getClass().getResourceAsStream("/config.xml"));
        assertThat(c.getJenkinsUrl(), is(new URL("http://ec2-54-171-84-56.eu-west-1.compute.amazonaws.com/api/xml/")));
        assertThat(c.getPollTimeMsec(), is(10000));
        assertThat(c.getTurnOnTime(), is(LocalTime.parse("08:00:00")));
        assertThat(c.getTurnOffTime(), is(LocalTime.parse("17:00:00")));
        assertThat(c.isActiveHolidays(), is(true));
    }
    @Test
    public void testReadNoOflamps() throws Exception {
        LampConfig c = LampConfigReader.read(getClass().getResourceAsStream("/config.xml"));

        List<Lamp> lamps = c.getLamps();
        assertThat(lamps.size(), is(2));
    }
    @Test
    public void testReadLamps() throws Exception {
        LampConfig c = LampConfigReader.read(getClass().getResourceAsStream("/config.xml"));

        List<Lamp> lamps = c.getLamps();
        assertThat(lamps.size(), is(2));
        {
            Lamp lamp = lamps.get(0);
            assertThat(lamp.getName(), is("green"));
            assertThat(lamp.getDescription(), is("Turn on green lamp when job TestBankAccount is ok"));
            assertThat(lamp.getOnCommand(), is("tdtool --on 1"));
            assertThat(lamp.getOffCommand(), is("tdtool --off 1"));
            assertThat(lamp.getJobNames().size(), is(1));
            assertThat(lamp.getJobNames().get(0), is("TestBankAccount"));

            List<Action> actions = lamp.getActions();
            assertThat(actions.size(), is(3));
            assertThat(actions.get(0).getEvent(), is(EventType.whenAllJobsOk));
            assertThat(actions.get(0).isOn(), is(true));
            assertThat(actions.get(1).getEvent(), is(EventType.whenAnyJobFails));
            assertThat(actions.get(1).isOn(), is(false));
            assertThat(actions.get(2).getEvent(), is(EventType.whenAnyJobUndefined));
            assertThat(actions.get(2).isOn(), is(true));
        }
        {
            Lamp lamp = lamps.get(1);
            assertThat(lamp.getName(), is("red"));
            assertThat(lamp.getDescription(), is("Turn on red lamp when job TestBankAccount has failed"));
            assertThat(lamp.getOnCommand(), is("tdtool --on 2"));
            assertThat(lamp.getOffCommand(), is("tdtool --off 2"));
            assertThat(lamp.getJobNames().size(), is(1));
            assertThat(lamp.getJobNames().get(0), is("TestBankAccount"));
            //assertThat(lamp.getJobNames().get(1), is("unit-test2"));

            List<Action> actions = lamp.getActions();
            assertThat(actions.size(), is(3));
            assertThat(actions.get(0).getEvent(), is(EventType.whenAllJobsOk));
            assertThat(actions.get(0).isOn(), is(false));
            assertThat(actions.get(1).getEvent(), is(EventType.whenAnyJobUndefined));
            assertThat(actions.get(1).isOn(), is(true));
            assertThat(actions.get(2).getEvent(), is(EventType.whenAnyJobFails));
            assertThat(actions.get(2).isOn(), is(true));
        }
    }
    
}
