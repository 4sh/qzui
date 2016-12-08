package qzui.domain;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

public interface JobDefinition {

    boolean acceptJobClass(Class<? extends Job> jobClass);

    JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob);
}
