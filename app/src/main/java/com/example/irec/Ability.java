package com.example.irec;

public class Ability {
    private int actionId;
    private String[] action={"продать", "сделать", "выполнить работы по", "сдать в аренду", "найти", "решить проблему с", "другое"};
    private String subjects;
    private String[] works;
    private String[] others;

    public Ability(int actionId, String subjects, String[] works, String[] others){
        this.actionId = actionId;
        this.subjects = subjects;
        this.works = works;
        this.others = others;
    }

    public String getAbilityString(){
        String ability = "";
        if (actionId!=2){

                ability = "Я могу "+action[actionId]+" "+subjects;

        } else if (actionId==2){
            for (int i = 0; i< works.length; i++){
                ability = "Я могу "+action[actionId]+" "+works[i];
            }
        }else
        {
            for (int i = 0; i< others.length; i++){
                ability = "Я могу "+others[i];
            }
        }
        return ability;
    }
    //"Я могу сделать торт, пирожное, пирог, запеканку, мороженое"
}
