package it.valeriovaudi.familybudget.accountservice.web.endpoint;

public class ContextPathProvider {

    private final String contextPath;

    public ContextPathProvider(String contextPath) {
        this.contextPath = contextPath;
    }

    public String pathFor(String path) {
        return !contextPath.equals("/") ? contextPath + path : path;
    }
}
