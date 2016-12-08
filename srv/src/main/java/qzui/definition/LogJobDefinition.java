package qzui.definition;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import qzui.domain.AbstractJobDefinition;
import qzui.domain.JobDescriptor;
import qzui.domain.LogJobDescriptor;
import qzui.job.LogJob;
import restx.factory.Component;

import java.util.List;
import java.util.Objects;

@Component
public class LogJobDefinition extends AbstractJobDefinition {

    @Override
    public boolean acceptJobClass(Class<? extends Job> jobClass) {
        return Objects.equals(jobClass.getName(), LogJob.class.getName());
    }

    @Override
    public JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        return setupDescriptorFromDetail(new LogJobDescriptor(), jobDetail, triggersOfJob);
    }
}
