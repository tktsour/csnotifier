package com.tktsour.csnotifier.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Announcement {
    @Id
    Long id;
    String title;
    @Column(columnDefinition = "TEXT")
    String content;
    String lecturer;
    LocalDateTime dateTime;
}
