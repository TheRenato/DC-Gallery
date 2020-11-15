package se.opazoweb.DCGallery.bootstrap;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import se.opazoweb.DCGallery.model.DcChannel;
import se.opazoweb.DCGallery.model.DcImage;
import se.opazoweb.DCGallery.model.DcServer;
import se.opazoweb.DCGallery.repositories.DcChannelRepo;
import se.opazoweb.DCGallery.repositories.DcImageRepo;
import se.opazoweb.DCGallery.repositories.DcServerRepo;

import java.util.Iterator;

@Component
public class DcData implements CommandLineRunner {


    @Value("${dc.bot_token}")
    private String dcBotToken;

    @Value("${gallery.baseurl}")
    private String galleryBaseUrl;

    @Value("${gallery.build_cmd}")
    private String galleryBuildCmd;

    @Value("${gallery.url_cmd}")
    private String galleryUrlCmd;

    @Value("${gallery.remove_cmd}")
    private String galleryRemoveCmd;

    @Autowired
    private final DcChannelRepo dcChannelRepo;
    @Autowired
    private final DcImageRepo dcImageRepo;

    @Autowired
    private final DcServerRepo dcServerRepo;

    public DcData(DcChannelRepo dcChannelRepo, DcImageRepo dcImageRepo, DcServerRepo dcServerRepo) {
        this.dcChannelRepo = dcChannelRepo;
        this.dcImageRepo = dcImageRepo;
        this.dcServerRepo = dcServerRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        final DiscordClient client = DiscordClient.create(this.dcBotToken);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            if (!message.getAuthor().get().isBot()) {
                handleDcMessage(message, gateway);
            }
        });

        gateway.onDisconnect().block();
    }

    public void handleDcMessage(Message message, GatewayDiscordClient gateway) {
        String serverName = gateway.getGuildById(message.getGuildId().get()).block().getName();
        String serverId = message.getGuildId().get().asString();

        final MessageChannel channel = message.getChannel().block();
        String channelID = message.getChannelId().asString();
        String channelName = gateway.getGuilds().blockFirst().getChannelById(Snowflake.of(channelID)).block().getName();

        addServerIfNotAdded(serverId, serverName);

        if (galleryBuildCmd.equals(message.getContent())) {
            addChannelIfNotAdded(serverId, channelName, channelID);

            buildGallery(message, channelID);
            channel.createMessage("Your gallery will now be created").block();
        }

        if (dcChannelRepo.existsById(channelID)) {
//            if (galleryRemoveCmd.equals(message.getContent())) {
//               removeChannelAndImages(channelID);
//            }

            if (galleryUrlCmd.equals(message.getContent())) {
                final String addUrlChannel = "channelid=" + channelID;

                channel.createMessage(galleryBaseUrl + serverId + "?" + addUrlChannel).block();
            }
            handleMessageWithAttachments(message, channelID);
        }

    }

    private void addServerIfNotAdded(String serverId, String serverName) {
        if(!dcServerRepo.existsById(serverId)) {
            DcServer dcServer = new DcServer(serverName, serverId);
            dcServerRepo.save(dcServer);
        }
    }

    private void addChannelIfNotAdded(String serverId, String channelName, String channelID) {
        if(!dcChannelRepo.existsById(channelID)) {

            DcChannel dcChannel = new DcChannel(
                    channelID,
                    channelName,
                    dcServerRepo.findById(serverId).get()
                    );
            dcChannelRepo.save(dcChannel);

        }
    }

    private void buildGallery(Message message, String channelID) {
        Flux<Message> messageBefore = message.getChannel().block().getMessagesBefore(message.getId());
        Iterable<Message> itr = messageBefore.toIterable();

        for (Message msg : itr) {
            handleMessageWithAttachments(msg, channelID);
        }
    }

    private void handleMessageWithAttachments(Message message, String channelID) {

        if (!message.getAttachments().isEmpty()) {

            String time = String.valueOf(message.getTimestamp());
            DcChannel theChannel = dcChannelRepo.findById(channelID).get();

            for (Object o : message.getAttachments().toArray()) {
                Attachment attachment = (Attachment) o;

                String imageId = attachment.getId().asString();

                if (!dcImageRepo.existsById(imageId)) {
                    String filename = attachment.getFilename();
                    String url = attachment.getUrl();

                    DcImage dcImage = new DcImage(imageId, theChannel, filename, url, time);
                    dcImageRepo.save(dcImage);

                }
            }

        }
    }

    private void removeChannelAndImages(String channelID) {
        Iterator<DcImage> imgItr = dcChannelRepo.findById(channelID).get().getDcImages().iterator();

        while (imgItr.hasNext()) {
            dcImageRepo.deleteById(imgItr.next().getImageId());
        }

        dcChannelRepo.deleteById(channelID);
    }

}
