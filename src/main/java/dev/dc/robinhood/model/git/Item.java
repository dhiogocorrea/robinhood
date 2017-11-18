package dev.dc.robinhood.model.git;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("sha")
    @Expose
    private String sha;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("git_url")
    @Expose
    private String gitUrl;
    @SerializedName("html_url")
    @Expose
    private String htmlUrl;
    @SerializedName("repository")
    @Expose
    private Repository repository;
    @SerializedName("score")
    @Expose
    private Double score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
