package com.yu7.order;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sid;
        String name;
        String result = " ";
        while (true) {
            //sid
            sid = scanner.nextInt();
            if (sid == -1) {
                break;
            }
            name = scanner.next();

            result = result + "\n" + new Student(sid, name).toString();
        }
        System.out.println(result);
        System.out.println("学生总人数"+Student.getCount());
    }
}
class Student {
    private int sid;
    private String sName;
    private static int count = 0;

    public Student(int sid, String sName) {
        this.sid = sid;
        this.sName = sName;
        count++;
    }
    public static int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return  sid +" "+ sName ;
    }
}
