package com.example.predictor_mundial.dto;

public class TeamStats {

    private Integer ranking;
    private Integer elo;

    public TeamStats(Integer ranking, Integer elo) {
        this.ranking = ranking;
        this.elo = elo;
    }

    public Integer getRanking() {
        return ranking;
    }

    public Integer getElo() {
        return elo;
    }
}