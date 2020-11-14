package se.opazoweb.DCGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.opazoweb.DCGallery.model.DcChannel;
import se.opazoweb.DCGallery.model.DcImage;
import se.opazoweb.DCGallery.repositories.DcChannelRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class indexController {


    private String title = " DC - Image Gallery";

    @Autowired
    private DcChannelRepo dcChannelRepo;

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

        DcChannel dcChannel;
        Set<DcImage> dcImages;

        if (dcChannelRepo.existsById(channelId)) {
            dcChannel = dcChannelRepo.findById(channelId).get();
            System.out.println(dcChannel.getDcImages().isEmpty());

            dcImages = dcChannel.getDcImages();
            if (!dcImages.isEmpty()) {
                model.addAttribute("images", dcImages);
            }
            model.addAttribute("h1title", "Gallery of "
                    + dcChannel.getChannelName() + " @ "
                    + dcChannel.getDcServer().getServerName()
            );

        } else {
            model.addAttribute("h1title", "Gallery of ");
            model.addAttribute("message", "Run !gallery build in your server or/and add the bot to your server first.");
        }

        model.addAttribute("channelid", "Channel ID " + channelId);
        model.addAttribute("title", title);


        return "gallery";
    }
}
