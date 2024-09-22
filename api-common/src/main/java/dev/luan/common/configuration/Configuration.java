package dev.luan.common.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Configuration {

    protected YamlConfiguration configuration;

    public abstract void load(File file);

    public abstract void save(File file);

}