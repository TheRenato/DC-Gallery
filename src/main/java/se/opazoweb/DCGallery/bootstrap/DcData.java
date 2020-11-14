package se.opazoweb.DCGallery.bootstrap;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import se.opazoweb.DCGallery.model.DcChannel;
import se.opazoweb.DCGallery.model.DcImage;
import se.opazoweb.DCGallery.model.DcServer;
import se.opazoweb.DCGallery.repositories.DcChannelRepo;
import se.opazoweb.DCGallery.repositories.DcImageRepo;
import se.opazoweb.DCGallery.repositories.DcServerRepo;

import java.util.Set;

@Component
public class DcData implements CommandLineRunner {


    @Value("${dc.bot_token}")
    private String dcBotToken;

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
            handleDcMessage(message, gateway);
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

        if ("!ping".equals(message.getContent())) {
            Iterable<Guild> guilds = gateway.getGuilds().toIterable();

            for (Guild guild : guilds) {
                System.out.println(guild.getName());
            }

            channel.createMessage("Pong!").block();
        }

        if ("!gallery build".equals(message.getContent())) {
            addChannelIfNotAdded(serverId, channelName, channelID);

            Flux<Message> messageBefore = message.getChannel().block().getMessagesBefore(message.getId());

            Iterable<Message> itr = messageBefore.toIterable();

            for (Message msg : itr) {
                handleMessageWithAttachments(msg, channelID);
            }
        }

        if ("!gallery url".equals(message.getContent())) {
            String urlBefore = "http://localhost/gallerys/";
            final String addUrlServer = message.getGuildId().get().asString();
            final String addUrlChannel = "channelid=" + message.getChannelId().asString();

            channel.createMessage(urlBefore + addUrlServer + "?" + addUrlChannel).block();
        }

//
//            System.out.println("Author Name: " + message.getAuthor().get().getUsername());
//            System.out.println("Author ID: " + message.getAuthor().get().getId().asString());

    }

    private void addServerIfNotAdded(String serverId, String serverName) {
        if(!dcServerRepo.existsById(serverId)) {
            DcServer dcServer = new DcServer(serverName, serverId);
            dcServerRepo.save(dcServer);

            System.out.println("Server saved");
            System.out.println("count " + dcServerRepo.count());
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

            System.out.println("channel Saved");
            System.out.println("count " + dcChannelRepo.count());
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

                    System.out.println("image Saved");
                    System.out.println("count " + dcImageRepo.count());
                }
            }

        }
    }

    public Set<DcImage> getChannelsImages(String channelId) {
        return dcChannelRepo.findById(channelId).get().getDcImages();
    }
}
