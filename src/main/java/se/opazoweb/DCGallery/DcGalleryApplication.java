package se.opazoweb.DCGallery;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;

@SpringBootApplication
public class DcGalleryApplication {

	public static void main(String[] args) {


		SpringApplication.run(DcGalleryApplication.class, args);

//		myFun(dcBotToken);
	}

	public static void myFun(String dcBotToken) {
		final DiscordClient client = DiscordClient.create(dcBotToken);
		final GatewayDiscordClient gateway = client.login().block();

		gateway.on(MessageCreateEvent.class).subscribe(event -> {
			final Message message = event.getMessage();

			if ("!ping".equals(message.getContent())) {
				final MessageChannel channel = message.getChannel().block();
				channel.createMessage("Pong!").block();

			}

			if ("!gallery url".equals(message.getContent())) {
				String urlBefore = "http://localhost:8080/gallerys/";
				final String addUrlServer = message.getGuildId().get().asString();
				final String addUrlChannel = "channelid=" + message.getChannelId().asString();
				final MessageChannel channel = message.getChannel().block();
				channel.createMessage(urlBefore + addUrlServer + "?" + addUrlChannel).block();
			}

			if (!message.getAttachments().isEmpty()) {
				System.out.println(message.getAttachments().toArray()[0]);
				System.out.println(message.getAttachments().getClass());

				Attachment attachment = (Attachment) message.getAttachments().toArray()[0];

				System.out.println("File name: " + attachment.getFilename());
				System.out.println("File url: " + attachment.getUrl());
			}

			String channelID = message.getChannelId().asString();
			System.out.println("Guild Name: " + gateway.getGuildById(message.getGuildId().get()).block().getName());
			System.out.println("Guild ID: " + message.getGuildId().get().asString());
			System.out.println("Channel Name: " + gateway.getGuilds().blockFirst().getChannelById(Snowflake.of(channelID)).block().getName());
			System.out.println("Channel ID: " + channelID);


			System.out.println("Author Name: " + message.getAuthor().get().getUsername());
			System.out.println("Author ID: " + message.getAuthor().get().getId().asString());
		});

		gateway.onDisconnect().block();
	}

}
