package com.learnboot.universalpetcare.event;

import com.learnboot.universalpetcare.models.Appointment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class AppointmentBookedEvent extends ApplicationEvent {

    private Appointment appointment;

    public AppointmentBookedEvent(Appointment appointment) {
        super(appointment);
        this.appointment = appointment;

    }
}
