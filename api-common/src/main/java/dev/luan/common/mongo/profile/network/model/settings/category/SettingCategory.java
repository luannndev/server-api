package dev.luan.common.mongo.profile.network.model.settings.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum SettingCategory {

    GENERAL("settings.category.general.name", "settings.category.general.description", null, true),
    CHAT("settings.category.chat.name", "settings.category.chat.description", null, true),

    TTT("settings.category.ttt.name", "settings.category.ttt.description", "TTT", false),
    ;

    String nameLangKey;
    String descriptionLangKey;
    String gameMode;
    boolean isGlobal;

    public boolean isGlobal() {
        return isGlobal && gameMode == null;
    }

    public boolean isGameMode() {
        return gameMode != null && !isGlobal;
    }

}