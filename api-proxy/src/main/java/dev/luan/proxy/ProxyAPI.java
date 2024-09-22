package dev.luan.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.luan.common.CommonAPI;
import dev.luan.proxy.command.LanguageCommand;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.logging.Logger;

@Plugin(
        id = "api-proxy",
        name = "API Proxy",
        version = "1.0",
        url = "",
        description = "Proxy API for the network",
        authors = {"luannndev"}
)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ProxyAPI {

    public static final String PREFIX_CONTENT = "<dark_gray>| <gradient:#fff800:#ff5900:#fff800><bold>sᴇʀᴠᴇʀ</bold></gradient> <dark_gray>→</dark_gray><white>";
    public static final Component PREFIX = MiniMessage.miniMessage().deserialize(PREFIX_CONTENT);

    ProxyServer proxyServer;
    Logger logger;
    CommonAPI commonAPI;

    @Getter
    static ProxyAPI instance;

    @Inject
    public ProxyAPI(ProxyServer proxyServer, Logger logger) {
        instance = this;
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.commonAPI = CommonAPI.instance();

        this.logger.info("API Proxy has been enabled!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        //Register Commands and Events
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder("language")
                .plugin(this)
                .aliases("lang", "sprache", "idioma", "lingua")
                .build();
        commandManager.register(meta, LanguageCommand.createCommand(this));
    }

}