package qzui.domain;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import qzui.job.HttpJob;

public class HttpJobDescriptor extends JobDescriptor {

    private String url;
    private String method = "POST";
    private String body;
    private String contentType;
    private String login;
    private String pwdHash;
    private HttpConfiguration httpConfiguration;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLogin() {
        return login;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public HttpJobDescriptor setBody(final String body) {
        this.body = body;
        return this;
    }

    public HttpJobDescriptor setMethod(final String method) {
        this.method = method;
        return this;
    }

    public HttpJobDescriptor setUrl(final String url) {
        this.url = url;
        return this;
    }

    public HttpJobDescriptor setContentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpJobDescriptor setPwdHash(final String pwdHash) {
        this.pwdHash = pwdHash;
        return this;
    }

    public HttpJobDescriptor setLogin(final String login) {
        this.login = login;
        return this;
    }

    public HttpConfiguration getHttpConfiguration() {
        return httpConfiguration;
    }

    public HttpJobDescriptor setHttpConfiguration(final HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
        return this;
    }

    @Override
    public JobDetail buildJobDetail() {

        JobDataMap dataMap = new JobDataMap(getData());

        dataMap.put("url", url);
        dataMap.put("method", method);
        dataMap.put("body", body);
        dataMap.put("contentType", contentType);
        dataMap.put("login", login);
        dataMap.put("pwd", pwdHash);

        return JobBuilder.newJob(HttpJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(dataMap)
                .build();
    }

    @Override
    public String toString() {
        return "HttpJobDescriptor{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

}