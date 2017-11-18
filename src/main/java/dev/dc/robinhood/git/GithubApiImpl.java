package dev.dc.robinhood.git;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import dev.dc.robinhood.model.git.GitCodeSearch;
import dev.dc.robinhood.model.git.GitRepo;
import dev.dc.robinhood.model.git.Item;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GithubApiImpl {

    static Gson gson;
    OkHttpClient client;

    final String GIT_API = "https://api.github.com";

    public GithubApiImpl() {
        gson = new Gson();
        client = new OkHttpClient();
    }

    public GitRepo[] getRepositories(int since) throws IOException {
        String url = GIT_API + "/repositories";

        if (since > 0) {
            url += "?since=" + since;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        
        if (response.isSuccessful()) {
            GitRepo[] repos = gson.fromJson(response.body().string(), GitRepo[].class);
            response.body().close();
            return repos;
        } else {
            return null;
        }
    }
    
    public GitCodeSearch search(GitRepo repo, String keyword) throws IOException, InterruptedException {
        String repoUrl = repo.getFullName();
        
        String url = GIT_API + "/search/code?q=" + keyword + " repo:" + repoUrl;
        
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        
        if (response.isSuccessful()) {
            GitCodeSearch res = gson.fromJson(response.body().string(), GitCodeSearch.class);
            response.body().close();
            return res;
        } else {
           System.out.println("Reached request limit, sleeping for 1 min...");
           response.body().close();
           Thread.sleep(60000);
           return search(repo, keyword);
        }
    }
    
    public static int getMaxId(GitRepo[] repos) {
        int max = -1;
        
        for (GitRepo repo : repos) {
            if (max < repo.getId()) {
                max = repo.getId();
            }
        }
        return max;
    }
    
    public static List<String> extractContent(GitCodeSearch gitCodeSearch, boolean saveJson) {
        if (gitCodeSearch != null) {
            int num = gitCodeSearch.getTotalCount();
            
            if (num > 0) {
                System.out.print("Found " + num + " files! Extracting code...");
                
                List<Item> items = gitCodeSearch.getItems();
                
                if (saveJson) {
                    return items.parallelStream().map(item -> gson.toJson(item))
                            .collect(Collectors.toList());
                }
                return items.parallelStream().map(item -> {
                    String htmlUrl = item.getHtmlUrl();
                    
                    try {
                        Document doc = Jsoup.connect(htmlUrl).get();
                        Elements elems = doc.getElementsByTag("table");
                        
                        if (elems.size() > 0) {
                            return elems.get(0).html();
                        }
                        
                        return null;
                    } catch (IOException ex) {
                        Logger.getLogger(GithubApiImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    return null;
                }).filter(content -> content != null).collect(Collectors.toList());
            }
        }
        return new ArrayList();
    }
}
