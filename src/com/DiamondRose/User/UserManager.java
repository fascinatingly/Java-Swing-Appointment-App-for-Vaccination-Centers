package com.DiamondRose.User;

import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

final public class UserManager{

    private static final UUID ADMIN_UUID = UUID.fromString("4a25b70b-679c-4880-9c09-f0b6b00cf50b");

    public static void validateUserInput(String firstName, String lastName, String username, String password, String email, LocalDate dob){
        if(firstName.isBlank()){
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if(!firstName.chars().allMatch(Character::isAlphabetic)){
            throw new IllegalArgumentException("First must contain only alphabetic characters");
        }

        if(lastName.isBlank()){
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        if(!lastName.chars().allMatch(Character::isAlphabetic)){
            throw new IllegalArgumentException("Last name must contain only alphabetic characters");
        }

        if(username.length() < 4){
            throw new IllegalArgumentException("Username must be at least 4 characters in length");
        }
        if(!username.chars().allMatch(Character::isLetterOrDigit)){
            throw new IllegalArgumentException("Username must contain only alphanumeric characters");
        }

        if(!Pattern.compile("^(.+)@(.+)$").matcher(email).matches()){
            throw new IllegalArgumentException("E-mail address is invalid");
        }

        if(password.length() <= 3){
            throw new IllegalArgumentException("Password must be at least 4 characters in length");
        }

        if(password.contains("\t") || password.contains("\n")){
            throw new IllegalArgumentException("Password contains unsupported characters");
        }

        if(LocalDate.now().minusYears(dob.getYear()).getYear() <= 12){
            throw new IllegalArgumentException("You must be at least 12 years or older to sign up.");
        }
    }

    private final String filePath;
    private final Map<UUID, User> userMap = new HashMap<>();

    public User getById(UUID id){
        return this.userMap.get(id);
    }

    public UserManager(String filePath){
        this.filePath = filePath;
        this.loadFromFile();
        if(this.getById(ADMIN_UUID) == null){
            this.register(new Personnel(ADMIN_UUID, "admin", "admin@mojang.edu", "Admin", "Doe", LocalDate.now(), Gender.MALE, "admin", Address.NULL));
        }
    }

    private void loadFromFile(){
        FileReader fileReader;
        try{
            fileReader = new FileReader(this.filePath);
        }catch(FileNotFoundException e){
            return;
        }

        Scanner scanner = new Scanner(fileReader);
        while(scanner.hasNextLine()) {
            String[] personString = scanner.nextLine().split("\t", 2);
            User user = switch(personString[0]){
                case "citizen" -> Citizen.fromString(personString[1]);
                case "noncitizen" -> NonCitizen.fromString(personString[1]);
                case "personnel" -> Personnel.fromString(personString[1]);
                default -> throw new IllegalArgumentException("Undefined type of user: " + personString[0]);
            };
            this.userMap.put(user.id, user);
        }

    }

    public void saveToFile() {
        FileWriter fileWriter;
        try{
            fileWriter = new FileWriter(this.filePath);
        }catch(IOException e){
            throw new RuntimeException();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        //write every element into file
        for(User user : this.userMap.values()) {
            String type;
            if(user instanceof Citizen){
                type = "citizen";
            }else if(user instanceof NonCitizen){
                type = "noncitizen";
            }else if(user instanceof Personnel){
                type = "personnel";
            }else{
                throw new IllegalArgumentException("Undefined type of user: " + user.getClass().getName());
            }
            printWriter.write(type +  "\t" + user.toString() + "\n");
        }
        printWriter.close();
    }

    public Collection<User> getAll(){
        return this.userMap.values();
    }

    public User getByUsername(String value){
        for(User user : this.getAll()){
            if(user.username.equalsIgnoreCase(value)){
                return user;
            }
        }
        return null;
    }

    public User getByEmail(String value){
        for(User user : this.getAll()){
            if(user.email.equalsIgnoreCase(value)){
                return user;
            }
        }
        return null;
    }

    public User getByUsernameOrEmail(String value){
        User user = this.getByUsername(value);
        if(user == null){
            user = this.getByEmail(value);
        }
        return user;
    }

    public User getByFullName(String firstName, String lastName) {
        for(User user : this.getAll()){
            if(user.firstName.equalsIgnoreCase(firstName) && user.lastName.equalsIgnoreCase(lastName))
            return user;
        }
        return null;
    }

    public void register(User newUser){
        for(User user : this.getAll()){
            if (user.username.equalsIgnoreCase(newUser.username)){
                throw new IllegalArgumentException("A user with this username already exists");
            }

            if(user.email.equalsIgnoreCase(newUser.email)){
                throw new IllegalArgumentException("A user with this email address already exists");
            }
        }

        this.userMap.put(newUser.id, newUser);
        this.saveToFile();
    }
}