package qzui.domain;

import org.quartz.Job;

public abstract class QuartzJob implements Job {

    public abstract <T extends JobDescriptor> T buildDescriptor();
}
