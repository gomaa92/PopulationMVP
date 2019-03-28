package com.example.population;

public class BeanClass {
    int rank;
    String country;
    String population;
    String flag;


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "BeanClass{" +
                "rank=" + rank +
                ", country='" + country + '\'' +
                ", population='" + population + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
