package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest03 {

    @Test
    public void Qthree() {
        
        //最初の"*"を出力
        String line = "* * * * *";
        System.out.println(line);
        
        for (int i = 1; i <= 3; i++) {
            System.out.println("*       *");
        }
        System.out.print(line);
    }
}
