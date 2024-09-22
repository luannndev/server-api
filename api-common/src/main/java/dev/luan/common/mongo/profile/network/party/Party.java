package dev.luan.common.mongo.profile.network.party;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Party {

    @Id
    UUID partyUniqueId;

    UUID leaderUniqueId;
    List<UUID> memberUniqueIds;

    public void addMember(UUID memberUniqueId) {
        if (!this.memberUniqueIds.contains(memberUniqueId)) {
            this.memberUniqueIds.add(memberUniqueId);
        }
    }

    public void removeMember(UUID memberUniqueId) {
        this.memberUniqueIds.remove(memberUniqueId);
    }

    public boolean isMember(UUID memberUniqueId) {
        return this.memberUniqueIds.contains(memberUniqueId);
    }

    public boolean isLeader(UUID leaderUniqueId) {
        return this.leaderUniqueId.equals(leaderUniqueId);
    }

    public void disband() {
        this.memberUniqueIds.clear();
        this.leaderUniqueId = null;
        this.partyUniqueId = null;
    }
}