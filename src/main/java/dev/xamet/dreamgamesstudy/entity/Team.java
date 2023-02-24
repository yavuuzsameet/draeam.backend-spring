package dev.xamet.dreamgamesstudy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue()
    private int id;

    @Column(unique = true)
    private String name;

    @ElementCollection
    private List<Integer> users;

}
