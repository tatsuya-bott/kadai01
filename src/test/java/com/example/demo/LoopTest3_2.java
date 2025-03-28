package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest3_2 {

    @Test
    public void Qtwo() {
        
        int x = 3;
        int y = 5;
        int center = y / 2 + 1;

        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                int distance = Math.abs(center - j);
                if (i <= distance) {
                    System.out.print(" ");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println("");
        }
    }
}
