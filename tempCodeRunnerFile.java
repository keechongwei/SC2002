public static void makeDailyAppointments(List<Doctor> staffs){
        // hour determines starting hour
        int hour = 9;
        for(int i = 0; i < numberofSlots; i++){
            String dateString = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
            String timeString = "";
            if (hour < 10){
                timeString = "0" + String.valueOf(hour) + ":00";
            }
            else{
                timeString = String.valueOf(hour) + ":00";
            }
            for (String doctorID : staffs) {
            AppointmentSlot slot = new AppointmentSlot(dateString, timeString, "AVAILABLE", doctorID, " ");
            
            // Add the appointment slot to the array and append to CSV
            appointmentSlotArray.add(slot); 
            appendAppointmentToCSV(slot);
        }
            hour++;
        }
    }