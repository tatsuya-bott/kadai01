package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest {
    
    @Test
    public void Qone() {
        
        //最初の"*"を出力
        String line = "*";
        System.out.println(line);
        
        for (int i = 1; i < 5; i++) {
            
            //ループごとに" *"が増えていく
            line += " *";
            System.out.println(line);
        }
    }
}
