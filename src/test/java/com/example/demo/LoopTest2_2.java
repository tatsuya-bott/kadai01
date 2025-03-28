package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest2_2 {

    @Test
    public void Qtwo() {
        
        int x = 5;
        
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= x; j++) {
                if (i % 2 != 0 && j % 2 != 0) {
                    System.out.print("*");
                } else if (i % 2 == 0 && j % 2 == 0) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
        
    }
}
