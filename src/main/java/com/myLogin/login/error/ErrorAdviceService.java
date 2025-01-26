package com.myLogin.login.error;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/* I can`t redirect from ControllerAdvice to static source
    and I deside render page (with exceptions)in browser*/

@Service
public class ErrorAdviceService {
    private ResourceLoader resourceLoader;
    private String SourcePage = "classpath:static/error.html";
    private String page = null, start, end;

    public ErrorAdviceService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @PostConstruct
    public void initData() {
        final Resource resource = this.resourceLoader.getResource(SourcePage);
        try{
            page=Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        }catch(IOException ex){
            System.out.println("\nError(read file - "+SourcePage+") in ErrorAdviceService \n"+ex);
            return;
        }
        final String[] split = page.split("<div id=\"message\"></div>");
        if(split.length != 2) {
            page = null;
            return;
        }
        start = split[0];
        end = split[1];
    }
    
    public String getResponceErrorPageWithMessage(String message) {
        
        return page == null? "INTERNAL_SERVER_ERROR" : new StringBuilder("")
                .append(start)
                .append("<div id=\"message\">")
                .append(message)
                .append("</div>")
                .append(end).toString();
    }
}
