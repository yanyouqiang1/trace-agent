package org;

import java.util.Random;

public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Person person = new Person();
            Random random = new Random();
            person.setAge(random.nextInt(105));
            person.saySomething();

            Thread.sleep(1000);
        }
    }


    static class Person {
        String name;
        int age;


        public void setAge(int age) {
            this.age = age;
        }


        public int getAge() {
            return this.age;
        }

        public void saySomething() {
            if (age < 10) {
                System.out.println("i'm " + getAge() + ",so young");
            } else if (age < 50) {
                System.out.println("i'm " + getAge() + ",so hard");
            } else if (age < 100) {
                System.out.println("i'm " + getAge() + ",need sleep");
            }
        }
    }

}
