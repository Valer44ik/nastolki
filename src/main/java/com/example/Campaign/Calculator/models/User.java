package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @OneToMany(mappedBy = "user")
    private Set<Campaign> campaigns = new HashSet<>();

    private String nickname, email, login, password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(Set<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public User(String nickname, String password, String login, String email) {
        this.nickname = nickname;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    public void removeCampaign(Campaign campaign) {
        campaigns.remove(campaign);
        campaign.setUser(null);
    }
}
