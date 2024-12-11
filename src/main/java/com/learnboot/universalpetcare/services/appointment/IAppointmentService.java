package com.learnboot.universalpetcare.services.appointment;

import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.request.AppointmentUpdateRequest;
import com.learnboot.universalpetcare.request.BookAppointmentRequest;

import java.util.List;

public interface IAppointmentService {

    //Appointment createAppointment(AppointmentRequest appointment, Long sender, Long receiver);

    Appointment createAppointment(BookAppointmentRequest request,
                                  Long senderId, Long recipientId);

    List<Appointment> getAllAppoints();
    //Appointment updateAppointment(Long appointmentId, AppointmentUpdateRequest appointment);

    Appointment updateAppointment(Long appointmentId, AppointmentUpdateRequest appointment);

    void deleteAppointment(Long appointmentId);
    Appointment getAppointmentById(Long appointmentId);
    Appointment getAppointmentByAppointmentNo(String appointmentNo);

    Appointment cancelAppointment(Long appointmentId);

    Appointment approveAppointment(Long appointmentId);

    Appointment declineAppointment(Long appointmentId);
}
