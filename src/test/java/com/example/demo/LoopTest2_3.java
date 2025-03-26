package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest2_3 {

    @Test
    public void Qtwo() {
        
        int x = 5;
        
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= x; j++) {
                if (i == Math.ceil(x / 2d) || j == Math.ceil(x / 2d)) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
        }
            System.out.println("");
            
        
    }
    }
}

