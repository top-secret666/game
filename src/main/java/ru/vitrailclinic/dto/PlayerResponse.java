package ru.vitrailclinic.dto;

import java.time.Instant;

import ru.vitrailclinic.model.PlayerRank;

public class PlayerResponse {
      private Long id;
    private String username;
    private String email;
    private int level;
    private Long experience;
    private PlayerRank rank;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getuUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }
    public PlayerRank getRank() { return rank; }
    public void setRank(PlayerRank rank) { this.rank = rank; }
}
