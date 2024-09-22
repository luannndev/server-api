package dev.luan.common.mongo.profile.network.model.settings;

import dev.luan.common.mongo.profile.network.model.settings.category.SettingCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum Setting {

    //1: off, 0: on
    AUTO_ACCEPT_FRIEND_REQUESTS("settings.auto_accept_friend_requests.name", "settings.auto_accept_friend_requests.description", SettingCategory.GENERAL, null, 1, List.of(0, 1), true),
    ;

    public static final Setting[] VALUES = values();

    String nameKey;
    String descriptionKey;
    SettingCategory category;
    String gameMode;
    int defaultValue;
    List<Integer> possibleValues;
    boolean isGlobal;

    public boolean isGlobal() {
        return isGlobal && gameMode == null;
    }

    public boolean isGameMode() {
        return gameMode != null && !isGlobal;
    }

    public boolean isPossibleValue(int value) {
        return possibleValues.contains(value);
    }

    public boolean isDefaultValue(int value) {
        return defaultValue == value;
    }

}