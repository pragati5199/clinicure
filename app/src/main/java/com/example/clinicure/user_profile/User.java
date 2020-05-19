package com.example.clinicure.user_profile;

public class User {

    public  String past_med,current_ill,blood_grp,drug_his,allergy,insurance;
    int weight;
    public User(){

    }
    public User(String past_med, String current_ill, String blood_grp, String drug_his, String allergy, int weight, String insurance){
        this.past_med = past_med;
        this.current_ill = current_ill;
        this.blood_grp = blood_grp;
        this.drug_his = drug_his;
        this.allergy = allergy;
        this.weight = weight;
        this.insurance = insurance;
    }

    public String getPast_med() {
        return past_med;
    }

    public void setPast_med(String past_med) {
        this.past_med = past_med;
    }

    public String getCurrent_ill() {
        return current_ill;
    }

    public void setCurrent_ill(String current_ill) {
        this.current_ill = current_ill;
    }

    public String getBlood_grp() {
        return blood_grp;
    }

    public void setBlood_grp(String blood_grp) {
        this.blood_grp = blood_grp;
    }

    public String getDrug_his() {
        return drug_his;
    }

    public void setDrug_his(String drug_his) { this.drug_his = drug_his; }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) { this.allergy = allergy; }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) { this.weight = weight; }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) { this.insurance = insurance; }

}