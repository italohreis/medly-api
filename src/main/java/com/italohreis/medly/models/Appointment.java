package com.italohreis.medly.models;

import com.italohreis.medly.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "appointment")
@Entity
@SQLDelete(sql = "UPDATE appointment SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", unique = true)
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    @Column(nullable = false)
    private boolean deleted = false;

}
