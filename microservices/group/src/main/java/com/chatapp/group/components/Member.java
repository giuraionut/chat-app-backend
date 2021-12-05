package com.chatapp.group.components;

import com.chatapp.group.dto.MemberDto;
import com.chatapp.group.entity.GroupEntity;
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
@Table(name = "members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "group_id"})
        }
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity groupEntity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id", unique = true)
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        role.addMember(this);
        roles.add(role);
    }

    public MemberDto.Display toDisplay() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, MemberDto.Display.class);
    }
}
