package com.sports.equipment.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility class for generating IDs and timestamps.
 */
public class IDGenerator {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String generateUserId() {
        return "USR_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateEquipmentId() {
        return "EQP_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateRequestId() {
        return "REQ_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateTransactionId() {
        return "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateMaintenanceId() {
        return "MNT_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(dateFormatter);
    }

    public static String getCurrentDateTime() {
        return java.time.LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getDateAfterDays(int days) {
        return LocalDate.now().plusDays(days).format(dateFormatter);
    }

    public static int getDaysBetween(String date1, String date2) {
        LocalDate d1 = LocalDate.parse(date1, dateFormatter);
        LocalDate d2 = LocalDate.parse(date2, dateFormatter);
        return (int) java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
    }
}
