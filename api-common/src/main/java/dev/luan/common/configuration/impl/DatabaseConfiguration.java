package dev.luan.common.configuration.impl;

import dev.luan.common.configuration.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatabaseConfiguration extends Configuration {

    String mongoUri;
    String mongoDatabase;

    String redisUri;
    String redisPassword;

    @Override
    public void load(File file) {
        if (!file.exists()) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                file.createNewFile();
                this.configuration = YamlConfiguration.loadConfiguration(file);
                this.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.mongoUri = this.configuration.getString("mongodb.uri", "mongodb://localhost:27017");
        this.mongoDatabase = this.configuration.getString("mongodb.database", "api");
        this.redisUri = this.configuration.getString("redis.uri", "redis://localhost:6379");
        this.redisPassword = this.configuration.getString("redis.password", "");
    }

    @Override
    public void save(File file) {
        this.configuration.set("mongodb.uri", this.mongoUri);
        this.configuration.setComments("mongodb.uri", List.of(
                "The MongoDB URI to connect to the database.",
                "Example: mongodb://localhost:27017",
                " ",
                "This is required to connect to the database.",
                "If not set, the plugin will not work."
        ));
        this.configuration.set("mongodb.database", this.mongoDatabase);
        this.configuration.setComments("mongodb.database", List.of(
                "The MongoDB database name the client should connect to.",
                "Example: api",
                " ",
                "This is required to connect to the mongodb server.",
                "If not set, the plugin will not work."
        ));
        this.configuration.set("redis.uri", this.redisUri);
        this.configuration.setComments("redis.uri", List.of(
                "The Redis URI to connect to the database.",
                "Example: redis://localhost:6379",
                " ",
                "This is required to connect to the database.",
                "If not set, the plugin will not work."
        ));
        this.configuration.set("redis.password", this.redisPassword);
        this.configuration.setComments("redis.password", List.of(
                "The Redis password to connect to the database.",
                "Example: password",
                " ",
                "This is not required if you didn't set a password."
        ));
        try {
            this.configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}