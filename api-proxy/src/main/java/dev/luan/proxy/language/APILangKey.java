package dev.luan.proxy.language;

import dev.luan.common.language.KeyHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum APILangKey implements KeyHolder {

    COMMAND_LANGUAGE_NOT_FOUND("command.language.not_found", "%prefix% <red>The language you entered is not available."),
    COMMAND_LANGUAGE_SET("command.language.set", "%prefix% <gray>You have successfully set your language to <white>%language%<gray>."),
    ;

    String key;
    String englishDefault;

    @Override
    public @NotNull String key() {
        return key;
    }

    @Override
    public @NotNull String englishDefault() {
        return englishDefault;
    }
}