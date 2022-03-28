# Java-Swing-Appointment-App-for-Vaccination-Centers

OOP Application for appointment scheduling and management client-and-admin using Java and Swing
This project was built in Intellij IDEA using Java, Maven, and Swing.



Content:
#### I- Summary
#### II- Features
#### III- Acknoledgements
#### IV- Class and Use-Case Diagrams
#### V- Demo Output
#### V- Acknowledgements



It is basically a COVID-19 vaccination-management-themed application through which:
1- a patient can login/signup, select a vaccination center, select a vaccine in-stock, schedule an appointment, manage profile, etc.
2- an admin can login, restock vaccines in a vaccination center, update appointment enum status, manage users, add/remove/edit vaccine or vaccination center details, and more.

Features include:

Multithreading using Thread objects

Runnable objects for lambda expressions used for temporary scope methods for several events.
![image](https://user-images.githubusercontent.com/102264544/160498190-acf3e3da-7cf5-4942-9971-f5f80e46c990.png)
![image](https://user-images.githubusercontent.com/102264544/160502309-a79944e5-61af-4e80-a069-6524e32dfc4f.png)

Parsing identifier, gender, state, and date of birth using identification card. (Inspired by the Malaysian MyKad, any MyKad can be parsed for these details)
Dynamic appointment handling greying out unavailable (taken) appointments.
Inventory management.
Efficient implementation and careful adherence of the OOP paradigm.
GUI with varying functionality and design based on admin or user login.
UUID generation.
Successful integration with opensource calendar component LGoodDatePicker.
Extensive use of Swing components, including JTabbedPane, JFileChooser, Icon, and JTable.
Comprehensive exception handling.



# Acknowledgements

Thanks to LGoodDatePicker for the excellent date and time Swing Components.
https://github.com/LGoodDatePicker/LGoodDatePicker


# Design

## Class Diagram

![image](https://user-images.githubusercontent.com/102264544/160496655-369f4380-ba34-40cc-98c5-4741128af342.png)

## Use-case Diagram

![image](https://user-images.githubusercontent.com/102264544/160496772-d33fda7e-336f-4762-9a13-2d877e16863a.png)


# IV- Demo Output

There are two types of accounts, a personnel account and a person account

1- All appointments tab from personnel account
![image](https://user-images.githubusercontent.com/102264544/160500217-3bce6864-058a-4ab2-9d2e-14f36720bcfa.png)
2- 
![image](https://user-images.githubusercontent.com/102264544/160500264-009f0d93-864a-4a9a-ab4e-8100ad05d125.png)
3- 
![image](https://user-images.githubusercontent.com/102264544/160500342-1c0af9d9-eefe-4f5d-961c-9ef021129a7d.png)
4- 
![image](https://user-images.githubusercontent.com/102264544/160500446-634abcae-d1c0-42f2-afaf-0150ffd8f8c3.png)
5- 
![image](https://user-images.githubusercontent.com/102264544/160500418-2df8486c-2a36-4580-8ba5-e8d4753df882.png)
6- Dialog for person to edit booked appointments
![image](https://user-images.githubusercontent.com/102264544/160500524-86140e6b-278f-463c-80cc-5cba75a7204a.png)
7- Appointment booking tab for person
![image](https://user-images.githubusercontent.com/102264544/160500576-766ab9f9-2569-4bf8-b0d5-6b932153888e.png)
Appointment booking form for person
![image](https://user-images.githubusercontent.com/102264544/160500592-24c1b474-f4e0-422b-b141-c9a677ab988a.png)
8- Settings for person
![image](https://user-images.githubusercontent.com/102264544/160501136-9351c886-dd3e-4165-b04d-0edce1f73e9e.png)
9- File browser for person
![image](https://user-images.githubusercontent.com/102264544/160501072-b8e94fce-69ac-493f-969c-2c1ae54f7061.png)
10- home tab for personnel account
![image](https://user-images.githubusercontent.com/102264544/160501264-6455623e-f954-4fd9-8353-f9e7a3993e28.png)
11- People tab and edit person dialog box for personnel account
![image](https://user-images.githubusercontent.com/102264544/160501477-036ddb26-dd70-4eb5-9e3b-98f393bf7569.png)
12- Vaccine tab for personnel
![image](https://user-images.githubusercontent.com/102264544/160501514-8964b0c5-b73b-4863-9538-92c050f89262.png)
13- Vaccine creator dialog for personnel
![image](https://user-images.githubusercontent.com/102264544/160501562-81e637de-1495-4c15-afe7-b19b1bb3a646.png)
14- Deletion of vaccines and all associated data for personnel
![image](https://user-images.githubusercontent.com/102264544/160501626-ef285042-9fbc-4544-9c57-3bae2688ce9f.png)
15- Center tab for personnel
![image](https://user-images.githubusercontent.com/102264544/160501675-10c5a662-68d7-4b1e-bdc2-68f4c14fa823.png)
16- Registration dialog for personnel
![image](https://user-images.githubusercontent.com/102264544/160501709-7fd0fe19-020c-4c19-84c3-b66989b1f0ea.png)
17- Center editing for personnel
![image](https://user-images.githubusercontent.com/102264544/160501763-196f66cb-5333-47b9-9050-2e369d6e657d.png)
18- Vaccine center inventory dialog for personnel
![image](https://user-images.githubusercontent.com/102264544/160501801-17b288bc-47ee-4ead-9f4e-21a1f650a464.png)
19- Center deletion prompt for personnel
![image](https://user-images.githubusercontent.com/102264544/160501834-97c5b6be-1c5e-4ebf-bd7b-9142210cce4a.png)
20- Stock tab for personnel
![image](https://user-images.githubusercontent.com/102264544/160501849-e77b7a3b-2f32-4fa9-a79b-fd16a81fdba6.png)
21- Stock update for personnel
![image](https://user-images.githubusercontent.com/102264544/160501977-13ff4059-8ac4-4351-8207-b33770832d56.png)
![image](https://user-images.githubusercontent.com/102264544/160501987-75acce6d-b0a5-4483-bead-ea0489125ff8.png)
![image](https://user-images.githubusercontent.com/102264544/160501998-5054c7cd-55cb-4f5f-bd13-9f1f8a283baa.png)














