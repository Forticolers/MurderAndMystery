/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeanbourquj
 */
public class Lol {
    private String s;
    public Lol(String ss){
        this.s = ss;
       
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lol{s=").append(s);
        sb.append('}');
        return sb.toString();
    }
    
}
