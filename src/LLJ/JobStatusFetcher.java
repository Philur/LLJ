package LLJ;

import java.util.Collection;
import java.util.Map;

public interface JobStatusFetcher {
    Map<String, JobStatus> getJobStatus(Collection<String> jobNames);
}