# Java-Swing-Appointment-App-for-Vaccination-Centers

OOP Application for appointment scheduling and management client-and-admin using Java and Swing


This project was built in Intellij IDEA using Java, Maven, and Swing.

It is basically a COVID-19 vaccination-management-themed application through which:
1- a patient can login/signup, select a vaccination center, select a vaccine in-stock, schedule an appointment, manage profile, etc.
2- an admin can login, restock vaccines in a vaccination center, update appointment enum status, manage users, add/remove/edit vaccine or vaccination center details, and more.

Features include:

Multithreading using Thread objects

Runnable objects for lambda expressions used for temporary scope methods for several events.
![image](https://user-images.githubusercontent.com/102264544/160498190-acf3e3da-7cf5-4942-9971-f5f80e46c990.png)
![image](https://user-images.githubusercontent.com/102264544/160498201-884e6559-0277-4d13-9dad-c6ddc9651e3f.png)

Parsing identifier, gender, state, and date of birth using identification card. (Inspired by the Malaysian MyKad, any MyKad can be parsed for these details)
Dynamic appointment handling greying out unavailable (taken) appointments.
Inventory management.
Efficient implementation and careful adherence of the OOP paradigm.
GUI with varying functionality and design based on admin or user login.
UUID generation.
Successful integration with opensource calendar component LGoodDatePicker.
Extensive use of Swing components, including JTabbedPane, JFileChooser, Icon, and JTable.
Comprehensive exception handling.



## Class Diagram

![image](https://user-images.githubusercontent.com/102264544/160496655-369f4380-ba34-40cc-98c5-4741128af342.png)

## Use-case Diagram

![image](https://user-images.githubusercontent.com/102264544/160496772-d33fda7e-336f-4762-9a13-2d877e16863a.png)


# Acknowledgements

Thanks to LGoodDatePicker for the excellent date and time Swing Components.
https://github.com/LGoodDatePicker/LGoodDatePicker


