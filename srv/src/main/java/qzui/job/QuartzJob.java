package qzui.job;

import org.quartz.Job;
import qzui.domain.JobDescriptor;

public abstract class QuartzJob implements Job {

    public abstract <T extends JobDescriptor> T buildDescriptor();
}
