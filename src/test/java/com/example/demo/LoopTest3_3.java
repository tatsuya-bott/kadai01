package com.example.demo;

import org.junit.jupiter.api.Test;

public class LoopTest3_3 {

    @Test
    public void Qone() {
        
        int x = 5;
        int y = 3;

        for (int i = 1; i <= y; i++) {
            for (int j = 1; j <= x; j++) {
                if (i == j || i + j == x + 1) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
