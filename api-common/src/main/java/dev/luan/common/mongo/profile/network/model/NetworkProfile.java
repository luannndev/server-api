package dev.luan.common.mongo.profile.network.model;

import dev.luan.common.mongo.profile.language.Language;
import dev.luan.common.mongo.profile.language.structure.LanguageHolder;
import dev.luan.common.mongo.profile.network.model.rank.type.RankType;
import dev.luan.common.mongo.profile.network.model.settings.Setting;
import dev.luan.common.mongo.profile.network.party.Party;
import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NetworkProfile implements LanguageHolder {

    @Id
    UUID userUniqueId;

    RankType rankType;

    String lastKnownName;

    long lastSeen;
    long firstJoin;

    float coins;
    float bees; //This will be used for shop purchases, etc.
    int level;
    int experience;

    Map<Setting, Integer> settings;

    List<UUID> ignoredPlayers;
    List<UUID> friends;

    @Transient
    Party party;

    Language language;

    /**
     * @apiNote Status of the player, can be used for custom purposes. MiniMessage is needed to parse the status.
     * @see net.kyori.adventure.text.minimessage.MiniMessage
     */
    String status;

    public boolean isFriendsWith(UUID friendUniqueId) {
        return this.friends.contains(friendUniqueId);
    }

    public boolean isIgnoring(UUID ignoredUniqueId) {
        return this.ignoredPlayers.contains(ignoredUniqueId);
    }

    public void addFriend(UUID friendUniqueId) {
        if (!this.isFriendsWith(friendUniqueId)) {
            this.friends.add(friendUniqueId);
        }
    }

    public void removeFriend(UUID friendUniqueId) {
        this.friends.remove(friendUniqueId);
    }

    public void ignorePlayer(UUID ignoredUniqueId) {
        if (!this.isIgnoring(ignoredUniqueId)) {
            this.ignoredPlayers.add(ignoredUniqueId);
        }
    }

    public void unignorePlayer(UUID ignoredUniqueId) {
        this.ignoredPlayers.remove(ignoredUniqueId);
    }

    public void addCoins(float coins) {
        this.coins += coins;
    }

    public void removeCoins(float coins) {
        this.coins -= coins;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public void removeExperience(int experience) {
        this.experience -= experience;
    }

    public void addLevel(int level) {
        this.level += level;
    }

    public void removeLevel(int level) {
        this.level -= level;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    public boolean isInParty() {
        return party != null;
    }

    public void disbandParty() {
        if (party != null) {
            party.disband();
            party = null;
        }
    }
}