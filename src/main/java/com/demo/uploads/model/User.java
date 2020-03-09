package com.demo.uploads.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String email;

    private String password;

    @OneToMany(mappedBy = "owner")
    private List<SharedFile> myFiles;

    @OneToMany(mappedBy = "sharedWith")
    private List<SharedFile> sharedWithMe;
}
