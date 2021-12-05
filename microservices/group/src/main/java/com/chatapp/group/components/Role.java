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

    private String type;

    @ElementCollection
    @Column(name = "permission", nullable = false)
    @CollectionTable(name = "role_permission_mapping", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"permission", "role_id"})
    })
    private List<String> permissions = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private GroupEntity groupEntity;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "roles_members_mapping",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "members_id"))
    @ToString.Exclude
    private List<Member> members = new ArrayList<>();

    public boolean isGenerated() {
        return this.type.equals(RoleType.GENERATED.name());
    }

    public void setType(RoleType type) {
        this.type = type.name();
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void deleteMember(Member member) {
        this.members.remove(member);
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
