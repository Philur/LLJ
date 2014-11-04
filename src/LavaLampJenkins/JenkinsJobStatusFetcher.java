package LavaLampJenkins;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JenkinsJobStatusFetcher implements JobStatusFetcher {
    private static final Logger logger = Logger.getLogger(JenkinsJobStatusFetcher.class);
    private URL url;

    public JenkinsJobStatusFetcher(URL url) {
        this.url = url;
    }

    @Override
    public Map<String, JobStatus> getJobStatus(Collection<String> jobNames) {
        Map<String, JobStatus> result = new HashMap<String, JobStatus>();
        Document dom;
        try {
            dom = new SAXReader().read(url);
        } catch (DocumentException e) {
            logger.error("Failed to parse api url (" + url + "):" + e.getMessage());
            for (String s : jobNames) {
                result.put(s, JobStatus.unknown);
            }
            return result;
        }
        for (Element jobElement : (List<Element>) dom.getRootElement().elements("job")) {
            String jobName = jobElement.elementText("name");
            if (jobNames.contains(jobName)) {
                String ballColor = jobElement.elementText("color");
                logger.debug("jobName=" + jobName + ",ballColor=" + ballColor);
                JobStatus jobStatus;
                if (ballColor.startsWith("blue") || ballColor.startsWith("green")) {
                    jobStatus = JobStatus.ok;
                } else if (ballColor.startsWith("yellow")) {
                    jobStatus = JobStatus.failed;
                } else if (ballColor.startsWith("red")) {
                    jobStatus = JobStatus.error;
                } else {
                    jobStatus = JobStatus.unknown;
                }
                result.put(jobName, jobStatus);
            }
        }
        return result;
    }
}
