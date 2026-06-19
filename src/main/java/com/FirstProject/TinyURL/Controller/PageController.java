package com.FirstProject.TinyURL.Controller;

import com.FirstProject.TinyURL.dto.UrlStatsResponse;
import com.FirstProject.TinyURL.exception.AliasAlreadyExistsException;
import com.FirstProject.TinyURL.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UrlShortenerService urlShortenerService;

    @Value("${app.base-url}")
    private String baseUrl;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(
            @RequestParam("longUrl") String longUrl,
            @RequestParam(name = "customAlias", required = false) String customAlias,
            @RequestParam(name = "hoursToExpire", required = false) Integer hoursToExpire,
            Authentication authentication, Model model)
    {
        String email = authentication.getDeclaringClass().getName();

        model.addAttribute("originalUrl", longUrl);

    try{

        String shortCode = urlShortenerService.shortenUrl(longUrl, customAlias,  hoursToExpire, email);

//        String fullShortUrl = "http://localhost:8080/" + shortCode;
        String fullShortUrl = baseUrl + "/r/" + shortCode;

        model.addAttribute("shortUrlResult", fullShortUrl);
    }
    catch(AliasAlreadyExistsException e){
        model.addAttribute("AliasError", e.getMessage());
    }

        return "index";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model) {

        try{
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            model.addAttribute("urlStats", stats);
        }
        catch(Exception e){
            model.addAttribute("statsError", "Statistics not found for this short code : " + shortCode);
        }

        return "index";

    }

}
