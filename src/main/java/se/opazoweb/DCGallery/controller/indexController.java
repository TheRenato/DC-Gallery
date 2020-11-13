package se.opazoweb.DCGallery.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class indexController {


    private String title = " DC - Image Gallery";

    @Value("${dc.client_id}")
    private String dcClientId;

    @Value("${dc.base_url}")
    private String dcBaseUrl;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("message", title);
        return "index";
    }

    @GetMapping("/servers")
    public String mainWithParam(
            @RequestParam(name = "server", required = false, defaultValue = "")
            String server, Model model) {

        String link =
                dcBaseUrl + dcClientId + "&scope=bot";

        model.addAttribute("server", server);
        model.addAttribute("message", server + title);
        model.addAttribute("link", link);

        return "index";
    }

    @GetMapping("/gallerys/{serverID}")
    public String galleryWithParam(
            @PathVariable("serverID") String serverId,
            @RequestParam(name = "channelid",
                    required = false,
                    defaultValue = "") String channelId,
            Model model
    ) {

        String link =
                "https://discord.com/oauth2/authorize?client_id="
                        + dcClientId + "&scope=bot";

        model.addAttribute("channelid", channelId);
        model.addAttribute("message", title);
        model.addAttribute("h1title", "Gallery of " + channelId);
        model.addAttribute("link", link);

        return "gallery";
    }

    private List<String> galleryList(String channelId) {
        List<String> galleryList = new ArrayList<>();

//        Channel channel = gateway.getChannelById(Snowflake.of(channelId)).block();

        return galleryList;
    }
}
