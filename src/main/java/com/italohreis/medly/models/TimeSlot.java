package com.italohreis.medly.models;

import com.italohreis.medly.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_window_id", nullable = false)
    private AvailabilityWindow availabilityWindow;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus status;

    @OneToOne(mappedBy = "timeSlot")
    private Appointment appointment;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AvailabilityStatus.AVAILABLE;
        }
    }

}
