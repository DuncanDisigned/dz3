package main.java;




import data.AnimalData;
import data.CommandData;
import exceptions.AnimalNotSupported;
import factory.AnimalFactory;
import tables.AnimalTable;
import utils.Validators;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static void displayAnimalList(AnimalTable animalTable) throws SQLException, AnimalNotSupported {
        List<Animal> animalList = animalTable.selectAll();
        if (animalList.isEmpty()) {
            System.out.println("Список животных пуст");
        } else {
            animalList.forEach(animal -> System.out.println(animal.toString()));
        }
    }
    public static void main(String[] args) throws AnimalNotSupported, SQLException {
        AnimalTable animalTable = new AnimalTable();

        Scanner scanner = new Scanner(System.in);
        CommandData[] commandsData = CommandData.values();
        String[] commandsConsole = new String[CommandData.values().length];
        for (int i = 0; i < commandsData.length; i++) {
            commandsConsole[i] = CommandData.values()[i].name().toLowerCase();
        }

        String[] animalCommandsDataConsole = new String[AnimalData.values().length];
        for (int j = 0; j < animalCommandsDataConsole.length; j++) {
            animalCommandsDataConsole[j] = AnimalData.values()[j].name().toLowerCase();
        }
        ArrayList<Animal> animalList = new ArrayList<>();


        while (true) {
            System.out.printf("Введите одну из команд: %s%n", String.join("/", commandsConsole));
            String commandInput = scanner.nextLine().toUpperCase().trim();

            boolean isCommandCorrect = false;
            for (CommandData commandData : commandsData) {
                if (commandData.name().equals(commandInput)) {
                    isCommandCorrect = true;
                    break;
                }
            }

            if (!isCommandCorrect) {
                System.out.printf("Команды %s не существует%n", String.join("/", commandInput));
                continue;
            }
            CommandData commandData = CommandData.valueOf(commandInput);

            switch (commandData) {
                case ADD:
                    boolean isValidAnimal = false;
                    while (!isValidAnimal) {
                        System.out.printf("Введите тип животного: %s%n", String.join("/",
                                animalCommandsDataConsole));
                        String animalType = scanner.nextLine().toUpperCase().trim();
                        AnimalData animalData = null;
                        try {
                            animalData = AnimalData.valueOf(animalType);
                            isValidAnimal = true;
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Animal not supported");
                            continue;
                        }
                        if (isValidAnimal) {
                            if (animalData != null) {
                                Animal animal = new AnimalFactory().create(animalData);
                                animal.setAnimalType(animalData);
                                String animalName = Validators.validateStringInput(scanner, "Введите имя животного:",
                                        "Неверный формат ввода. Пожалуйста, введите строку");
                                animal.setName(animalName);

                                int animalAge;
                                do {
                                    animalAge = Validators.validateIntegerInput(scanner, "Введите возраст животного",
                                            "Неверный формат возраста. Введите положительное целое число.");
                                    if (animalAge < 0) {
                                        System.out.println("Возраст не может быть отрицательным");
                                    }
                                } while (animalAge < 0);
                                animal.setAge(animalAge);

                                float animalWeight;
                                do {
                                    animalWeight = Validators.validateFloatInput(scanner, "Введите вес животного",
                                            "Неверный формат веса");
                                    if (animalWeight < 0) {
                                        System.out.println("Вес не может быть отрицательным");
                                    }
                                } while (animalWeight < 0);
                                animal.setWeight(animalWeight);

                                String colorInput = Validators.validateStringInput(scanner, "Введите цвет животного",
                                        "Неверный формат ввода. Пожалуйста, введите строку");
                                animal.setColor(colorInput);

                                animalTable.insert(animal);
                                
                                System.out.println(animal);
                                animal.say();
                                
                            }
                        }
                    }
                    break;
                case LIST:
                    displayAnimalList(animalTable);
                    break;
                case DELETE:
                    displayAnimalList(animalTable);
                    System.out.println("Введите ID животного, которое нужно удалить");
                    String deleteAnimal = scanner.nextLine().trim();
                    int delete = Integer.parseInt(deleteAnimal);
                    animalTable.delete(delete);
                    System.out.println("Животное успешно удалено");
                    break;
                case UPDATE:
                    displayAnimalList(animalTable);

                    System.out.println("Введите идентификатор животного для обновления");
                    int idToUpdate = Integer.parseInt(scanner.nextLine().trim());

                    Animal animalToUpdate = animalTable.selectById(idToUpdate);
                    if (animalToUpdate != null) {
                        String newName = Validators.validateStringInput(scanner, "Пожалуйста, введите новое имя животного",
                                "Неверный формат ввода. Пожалуйста, введите строку");

                        String newAge = String.valueOf(Validators.validateIntegerInput(scanner, "Введите возраст животного",
                                "Неверный формат возраста. Введите положительное целое число."));

                        String newWeight = String.valueOf(Validators.validateFloatInput(scanner, "Введите вес животного",
                                "Неверный формат веса"));

                        String newColor = Validators.validateStringInput(scanner, "Пожалуйста, введите новый цвет животного",
                                "Неверный формат ввода. Пожалуйста, введите строку");

                        animalToUpdate.setName(newName);
                        animalToUpdate.setAge(Integer.parseInt(newAge));
                        animalToUpdate.setWeight(Float.parseFloat(newWeight));
                        animalToUpdate.setColor(newColor);

                        animalTable.update(animalToUpdate);
                    }
                    break;
                case EXIT:
                    System.exit(0);
            }
        }
    }
}
