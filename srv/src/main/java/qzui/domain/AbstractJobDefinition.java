package qzui.domain;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJobDefinition implements JobDefinition {

    protected <T extends JobDescriptor> T setupDescriptorFromDetail(T jobDescriptor, JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {

        jobDescriptor.setGroup(jobDetail.getKey().getGroup())
                .setName(jobDetail.getKey().getName())
                .setData(jobDetail.getJobDataMap().getWrappedMap());

        List<TriggerDescriptor> triggerDescriptors = triggersOfJob.stream()
                .map(TriggerDescriptor::buildDescriptor)
                .collect(Collectors.toList());

        jobDescriptor.setTriggerDescriptors(triggerDescriptors);

        return jobDescriptor;
    }
}
