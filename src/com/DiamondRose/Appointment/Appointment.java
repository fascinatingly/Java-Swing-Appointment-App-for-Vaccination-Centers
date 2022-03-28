//declared in own, separate file named Employee.java to separate classes

class Employee{

    private String name;

    private int age;

    private double height;

    public Employee(String fname, int age, double hg){

        this.name = fname;

        this.age = age;

        this.height = hg;

    }

    public void Print_Employee_Age(){

        System.out.println(this.age);


    }

}


//Declared in own, separate file named Admin.java

class Admin extends Employee{

    public Admin(String fname, int age, double hg){
        super(fname, age, hg);
    }

}


//Irrelevant to question but still a vital part of program:

//in own, separate file named Main.java

public class Main

{
    public static void main(String[] args) {
        System.out.println("Hello World");
}

}