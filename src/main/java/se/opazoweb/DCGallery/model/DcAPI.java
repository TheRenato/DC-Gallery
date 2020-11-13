//package se.opazoweb.DCGallery.model;
//
//import discord4j.core.DiscordClient;
//import discord4j.core.GatewayDiscordClient;
//import discord4j.core.event.domain.message.MessageCreateEvent;
//import discord4j.core.object.entity.Message;
//import discord4j.core.object.entity.channel.MessageChannel;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//
//public class DcAPI<client> {
//
//    @Value("${dc.client_id}")
//    private String dcClientId;
//
//    @Value("${dc.bot_token}")
//    private String dcBotToken;
//
//    final DiscordClient client = DiscordClient.create(dcBotToken);
//    final GatewayDiscordClient gateway = client.login().block();
//
//    gateway.on(MessageCreateEvent.class).subscribe(event -> {
//        final Message message = event.getMessage();
//        if ("!ping".equals(message.getContent())) {
//            final MessageChannel channel = message.getChannel().block();
//            channel.createMessage("Pong!").block();
//
//        }
//
//        if ("!gallery url".equals(message.getContent())) {
//            String urlBefore = "http://localhost:8080/gallerys/";
//            final String addUrlServer = message.getGuildId().get().asString();
//            final String addUrlChannel = "channelid=" + message.getChannelId().asString();
//            final MessageChannel channel = message.getChannel().block();
//            channel.createMessage(urlBefore + addUrlServer + "?" + addUrlChannel).block();
//        }
//    });
//
//    @Bean
//    public String getServerName(String id) {
//        return "";
//    }
//}
