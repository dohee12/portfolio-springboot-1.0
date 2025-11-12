package com.example.portfoliospring1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConteroller {

    private Boolean isPrime(int n) {
        // 과제1. 에라토스테네스의 체 방식으로 구현

        return true;
    }

    private void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    @GetMapping("/check-prime-number")
    public String checkPrimeNumber(@RequestParam Integer number) {

        if (isPrime(number)) { // 소수 체크 로직
            return number + "는 소수가 맞습니다.";
        }

        return number + "는 소수가 아닙니다.";
    }

    // O(n^2)
    @GetMapping("/bubble-sort")
    public String bubbleSort() {
        int[] array = { 5,3,8,4,2,9,7,1 };

        System.out.println("최초 함수");
        printArray(array);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

        System.out.println("");
        System.out.println("최종 정렬 함수");
        printArray(array);

        return array.toString();
    }

    // O(N*logN)
    @GetMapping("/merge-sort")
    public String mergeSort() {
        int[] array = { 5,3,8,4,2,9,7,1 };

        System.out.println("최초 함수");
        printArray(array);

        // 과제 2. 머지 소트 로직

        System.out.println("");
        System.out.println("최종 정렬 함수");
        printArray(array);

        return array.toString();
    }

    // O(N*logN)
    @GetMapping("/quick-sort")
    public String quickSort() {
        int[] array = { 5,3,8,4,2,9,7,1 };

        System.out.println("최초 함수");
        printArray(array);

        // 옵션 퀵 소트 로직

        System.out.println("");
        System.out.println("최종 정렬 함수");
        printArray(array);

        return array.toString();
    }



}