package dev.luan.common.mongo.profile.network;

import dev.luan.common.CommonAPI;
import dev.luan.common.mongo.profile.ProfileManager;
import dev.luan.common.mongo.profile.language.Language;
import dev.luan.common.mongo.profile.network.model.NetworkProfile;
import dev.luan.common.mongo.profile.network.model.rank.type.RankType;
import dev.luan.common.mongo.profile.network.repository.NetworkProfileRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NetworkProfileManager extends ProfileManager<NetworkProfile, NetworkProfileRepository, UUID> {

    public NetworkProfileManager() {
        super(CommonAPI.instance().networkProfileRepository());
    }

    @Override
    public NetworkProfile onCreate(UUID uuid) {
        NetworkProfile networkProfile = new NetworkProfile();
        networkProfile.setUserUniqueId(uuid);
        networkProfile.setFirstJoin(System.currentTimeMillis());
        networkProfile.setLastSeen(System.currentTimeMillis());
        networkProfile.setCoins(0);
        networkProfile.setLevel(1);
        networkProfile.setExperience(0);
        networkProfile.setParty(null);
        networkProfile.setSettings(new HashMap<>());
        networkProfile.setFriends(new ArrayList<>());
        networkProfile.setIgnoredPlayers(new ArrayList<>());
        networkProfile.setStatus("<green>Player on MGN");
        networkProfile.setLanguage(Language.ENGLISH);
        networkProfile.setRankType(RankType.NORMAL);
        return networkProfile;
    }

    @Override
    public NetworkProfile onLoad(NetworkProfile entity) {
        return entity;
    }

    @Override
    public NetworkProfile onSave(NetworkProfile entity) {
        entity.setLastSeen(System.currentTimeMillis());
        entity.disbandParty();
        return entity;
    }

    @Override
    public NetworkProfile onUnload(NetworkProfile entity) {
        return entity;
    }
}