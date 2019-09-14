package com.titaniumproductionco.db.services.func;

import java.util.ArrayList;

/**
 * Handles all error messages
 *
 */
public class ErrorMes implements IErrorMessage {
    public static final ErrorMes ERROR_MESSAGE = new ErrorMes();
    private String[] messages;

    private ErrorMes() {
        ArrayList<String> list = new ArrayList<>();
        list.add(0, "Invalid Error");
        list.add(1, "Invalid Quantity");
        list.add(2, "Material doesn't exist");
        list.add(3, "Invalid Supervisor");
        list.add(4, "Date cannot be in the future");
        list.add(5, "Invalid Factory");
        list.add(6, "Log with same date, mine and ore already exists!");
        list.add(7, "Date must be after the discover date of the mine!");
        list.add(8, "Log with same date, mine, factory and ore already exists!");
        list.add(9, "Date cannot be null or in the future");
        list.add(10, "User already exists!");
        list.add(11, "Username cannot be empty!");
        list.add(12, "Password cannot be empty!");
        list.add(13, "Role cannot be empty!");
        list.add(14, "Invalid Role-specific input!");
        list.add(15, "Invalid Operator!");
        list.add(16, "Invalid Process Execution Log!");
        list.add(17, "Materials are invalid for this process!");
        list.add(18, "Process Execution is inconsistent with Chemical Equation!");
        list.add(19, "Invalid Machine!");
        list.add(20, "Invalid Cycles!");
        list.add(21, "Invalid Process!");
        list.add(22, "Invalid Order!");
        list.add(23, "Invalid Shipment Address!");
        list.add(24, "Cannot make updates to an order that is already shipped!");
        list.add(25, "Invalid Shipment Log");
        list.add(26, "Cannot delete an order that is already shipped!");
        list.add(27, "Cannot unship an order that is already received!");
        list.add(28, "Receive date cannot be before shipped date!");
        list.add(29, "The ship date cannot be before the date the order was placed!");
        list.add(30, "Duplicate shipment!");

        messages = new String[list.size()];
        list.toArray(messages);
    }

    @Override
    public String getErrorMessage(int error) {
        if (error < messages.length)
            return messages[error];
        return null;
    }
}
