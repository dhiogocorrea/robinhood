package dev.dc.robinhood;

import dev.dc.robinhood.git.GithubApiImpl;
import dev.dc.robinhood.model.git.GitCodeSearch;
import dev.dc.robinhood.model.git.GitRepo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class App {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        String resultPath = "C:\\Users\\Dhiogo\\git_codes_aws\\";
        
        GithubApiImpl git = new GithubApiImpl();
        
        GitRepo[] repos = null;
        int since = 963;
        //91654
        do {
            System.out.println("We are at repo num: " + since);
            
            repos = git.getRepositories(since);
            
            if (repos != null) {
                since = GithubApiImpl.getMaxId(repos);
                
                for (GitRepo repo : repos) {
                    System.out.println(" ------->" + repo.getId());
                    GitCodeSearch gitCodeSearch = git.search(repo, "aws");
                    List<String> codes = GithubApiImpl
                            .extractContent(gitCodeSearch, true);
                    
                    if (codes.size() > 0) {
                    for (int i = 0; i < codes.size(); i++) {
                        String code = codes.get(i);
                        String filename = repo.getFullName().replace("/", "_")
                                + "_" + repo.getId() + "_" + i + ".json";
                        
                        Files.write(Paths.get(resultPath + filename), 
                                code.getBytes(), StandardOpenOption.CREATE_NEW);
                    }
                    System.out.println("Saved " + codes.size());
                    }
                }
            }
        } while (repos != null);
    }
}
