package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Member;
import com.chatapp.group.components.Role;
import com.chatapp.group.components.RoleType;
import com.chatapp.group.dto.GroupDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;
    private String avatar;
    private UUID ownerId;

    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Member> members = new ArrayList<>();

    public void deleteRole(Role role) {
        roles.remove(role);
    }

    public void deleteAllRoles() {
        roles = new ArrayList<>();
    }

    public List<Role> getGeneratedRoles() {
        return this.roles.stream().filter(Role::isGenerated).toList();
    }

    public GroupDto.Display toDisplay() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, GroupDto.Display.class);
    }

    public GroupDto.Update toUpdate() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, GroupDto.Update.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupEntity groupEntity = (GroupEntity) o;
        return id != null && Objects.equals(id, groupEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addCategory(Category category) {
        category.setGroupEntity(this);
        this.categories.add(category);
    }

    public void addMember(Member member) {
        member.setGroupEntity(this);
        this.members.add(member);
    }

    public void addRole(Role role) {
        role.setType(RoleType.USER_CREATED);
        role.setGroupEntity(this);
        this.roles.add(role);
    }

    public void generateRole(Role role) {
        role.setType(RoleType.GENERATED);
        role.setGroupEntity(this);
        this.roles.add(role);
    }
}
