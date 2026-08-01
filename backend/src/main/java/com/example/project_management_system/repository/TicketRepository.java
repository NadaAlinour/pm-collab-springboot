package com.example.project_management_system.repository;

import com.example.project_management_system.entity.Priority;
import com.example.project_management_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public List<Ticket> findAllByProjectId(Long id);

    @Query("select t from Ticket t where (t.title like concat(:title, '%')) and (:status is null or t.status.id = :status) and (:priority is null or t.priority = :priority) and (:category is null or t.category.id = :category) ")
    public List<Ticket> searchTickets(@Param("title") String title, @Param("status") Long status,
                                      @Param("priority") Priority priority, @Param("category") Long category);
}