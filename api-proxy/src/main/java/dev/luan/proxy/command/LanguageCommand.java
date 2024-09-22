package dev.luan.proxy.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import dev.luan.common.mongo.profile.language.Language;
import dev.luan.common.mongo.profile.network.model.NetworkProfile;
import dev.luan.proxy.ProxyAPI;
import dev.luan.proxy.language.APILangKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class LanguageCommand {

    public static BrigadierCommand createCommand(ProxyAPI plugin) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("language")
                .then(BrigadierCommand.requiredArgumentBuilder("language", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (Language language : Language.values()) {
                                builder.suggest(language.name().toLowerCase());
                                builder.suggest(language.getKey());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            if (!(context.getSource() instanceof Player player)) {
                                context.getSource().sendMessage(Component.text("You must be a player to execute this command!"));
                                return 0;
                            }
                            NetworkProfile profile = plugin.getCommonAPI().networkProfileManager().findById(player.getUniqueId());
                            String argumentProvided = context.getArgument("language", String.class);
                            Language language = Language.getLanguage(argumentProvided);
                            if (language == null) {
                                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(APILangKey.COMMAND_LANGUAGE_NOT_FOUND.translate(profile, s -> s.replaceAll("%prefix%", ProxyAPI.PREFIX_CONTENT).replaceAll("%language%", argumentProvided))));
                                return 0;
                            }
                            profile.setLanguage(language);
                            plugin.getCommonAPI().networkProfileManager().save(profile.getUserUniqueId(), profile);
                            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(APILangKey.COMMAND_LANGUAGE_SET.translate(profile, s -> s.replaceAll("%prefix%", ProxyAPI.PREFIX_CONTENT).replaceAll("%language%", language.name()))));
                            return Command.SINGLE_SUCCESS;
                        })
                ).build();
        return new BrigadierCommand(helloNode);
    }

}