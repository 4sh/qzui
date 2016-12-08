package qzui.job;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qzui.domain.HttpConfiguration;

public class HttpJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(HttpJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String url = jobDataMap.getString("url");
        String method = jobDataMap.getString("method");
        HttpRequest request = new HttpRequest(url, method);

        String body = "";
        if (!Strings.isNullOrEmpty(jobDataMap.getString("body"))) {
            body = jobDataMap.getString("body");
        }

        setContentType(jobDataMap, request);
        setCrendentials(jobDataMap, request);
        applyHttpConfiguration(jobDataMap, request);

        request.send(body);
        int code = request.code();
        String responseBody = request.body();

        logger.info("{} {} => {}\n{}", method, url, code, responseBody);
    }

    private void applyHttpConfiguration(JobDataMap jobDataMap, HttpRequest request) {
        HttpConfiguration httpConfiguration = (HttpConfiguration) jobDataMap.get("httpConfiguration");
        if (httpConfiguration.isTrustAllHosts()) {
            request.trustAllHosts();
        }
        if (httpConfiguration.isTrustAllCerts()) {
            request.trustAllCerts();
        }
        request.followRedirects(httpConfiguration.isFollowRedirect());
    }

    private void setCrendentials(JobDataMap jobDataMap, HttpRequest request) {
        String login = jobDataMap.getString("login");
        String pwd = jobDataMap.getString("pwd");
        if (!Strings.isNullOrEmpty(login) && !Strings.isNullOrEmpty(pwd)) {
            request.basic(login, pwd);
        }
    }

    private void setContentType(JobDataMap jobDataMap, HttpRequest request) {
        String contentType = jobDataMap.getString("contentType");
        if (!Strings.isNullOrEmpty(contentType)) {
            request.contentType(contentType);
        }
    }
}
