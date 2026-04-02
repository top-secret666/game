 package ru.vitrailclinic.model;
    
    import jakarta.persistence.*;
import java.time.Instant;
    
    @Entity
    @Table(name = "players")
    public class PlayerEntity {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @Column(unique = true, nullable = false)

        private String username;
    
        @Column(unique = true)
        private String email;
    
        @Column(name = "password_hash", nullable = false)
        private String passwordHash;
    
        private int level;
    
        @Column ( nullable = false,columnDefinition = "int default 0")
        private long experience;
    
        @Column(nullable = false)                   
        @Enumerated(EnumType.STRING) 
        private PlayerRank rank;
    
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;
    
        @PrePersist
        public void onCreate() {
            
        this.createdAt = Instant.now();
    
         if (this.level == 0) {
            this.level = 1; 
        }
        
        if (this.rank == null) {
            this.rank = PlayerRank.APPRENTICE; 
        }
        }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }
    
    public PlayerRank getRank() { return rank; }
    public void setRank(PlayerRank rank) { this.rank = rank; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    }
      
    

