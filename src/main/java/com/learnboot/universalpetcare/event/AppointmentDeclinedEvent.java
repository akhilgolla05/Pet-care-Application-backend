package com.learnboot.universalpetcare.event;

import com.learnboot.universalpetcare.models.Appointment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class AppointmentDeclinedEvent extends ApplicationEvent {

    private Appointment appointment;

    public AppointmentDeclinedEvent(Appointment appointment) {
        super(appointment);
        this.appointment = appointment;
    }
}
