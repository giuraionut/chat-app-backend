package com.chatapp.group.components;

import com.chatapp.group.dto.RoleDto;
import com.chatapp.group.entity.GroupEntity;
import com.chatapp.group.permissions.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "group_id"})
        }
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    @ElementCollection
    @Column(name = "user_id", nullable = false)
    @CollectionTable(name = "user_role_mapping")
    @ToString.Exclude
    private List<UUID> usersId = new ArrayList<>();

    @ElementCollection
    @Column(name = "permission", nullable = false)
    @CollectionTable(name = "role_permission_mapping", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"permission", "role_id"})
    })
    private List<String> permissions = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "group_id", nullable = false, unique = true)
    @ToString.Exclude
    private GroupEntity groupEntity;

    public void addUsers(UUID userId) {
        this.usersId.add(userId);
    }

    public void addPermissions(Permission permission) {
        this.permissions.add(permission.name());
    }

    public RoleDto toRoleDto() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, RoleDto.class);
    }
}
