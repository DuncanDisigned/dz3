package main.java;


import data.AnimalData;

public abstract class Animal {
    private String name;
    private int age;
    private float weight;
    private String color;
    private long id;
    private AnimalData animalType;

    public AnimalData getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalData animalType) {
        this.animalType = animalType;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public void say() {
        System.out.println("Я говорю");
    }

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    @Override
    public String toString() {
        return String.format("Привет! меня зовут %s, мне %d %s, я вешу - %.2f кг, мой цвет - %s",
                name, age, getCase(), weight, color);
    }

    private String getCase() {
        int remainder = age % 10;

        if (age >= 11 && age <= 14) {
            return "лет";
        }
        if (remainder == 1) {
            return "год";
        }
        if (remainder >= 5) {
            return "лет";
        }
        if (remainder >= 2) {
            return "года";
        }
        return "лет";
    }

}
