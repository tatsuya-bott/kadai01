package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest02 {

    @Test
    public void Qtwo() {
        
        //最初の"*"を出力
        String line = "* * * * *";
        System.out.println(line);
        
        for (int i = 1; i < 6; i++) {
            
            //ループごとに" *"が減っていく
            line = line.substring(0, line.length() - i * 2);
            System.out.print(line);
            line = "* * * * *";
            
            //改行
            System.out.println("");
        }
    }
}
