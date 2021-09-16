package com.tktsour.csnotifier.repository;

import com.tktsour.csnotifier.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    @Query(value = "SELECT MAX(id) FROM Announcement")
    Long getMaxId();
}
