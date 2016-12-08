package qzui.definition;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import qzui.domain.AbstractJobDefinition;
import qzui.domain.HttpJobDescriptor;
import qzui.domain.JobDescriptor;
import qzui.job.HttpJob;
import restx.factory.Component;

import java.util.List;
import java.util.Objects;

@Component
public class HttpJobDefinition extends AbstractJobDefinition {

    @Override
    public boolean acceptJobClass(Class<? extends Job> jobClass) {
        return Objects.equals(jobClass.getName(), HttpJob.class.getName());
    }

    @Override
    public JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {

        HttpJobDescriptor jobDescriptor = setupDescriptorFromDetail(new HttpJobDescriptor(), jobDetail, triggersOfJob);

        return jobDescriptor
                .setUrl((String) jobDescriptor.getData().remove("url"))
                .setMethod((String) jobDescriptor.getData().remove("method"))
                .setBody((String) jobDescriptor.getData().remove("body"));
    }
}
