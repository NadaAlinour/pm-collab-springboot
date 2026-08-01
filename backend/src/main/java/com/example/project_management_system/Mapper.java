package com.example.project_management_system;

import com.example.project_management_system.dto.*;
import com.example.project_management_system.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
/*
    public Ticket toTicket(TicketRequestDTO dto) {

    }

*/

    public TicketResponseDTO toTicketResponse(Ticket t) {

        // map ticket to ticket response dto
        StatusResponseDTO status = null;
        if (t.getStatus() != null)
            status = new StatusResponseDTO(t.getStatus().getId(), t.getStatus().getValue(), t.getStatus().getName());


        CategoryResponseDTO category = null;
        if (t.getCategory() != null)
            category = new CategoryResponseDTO(t.getCategory().getId(), t.getCategory().getName());

        UserResponseDTO createdBy = new UserResponseDTO(
                t.getCreatedBy().getId(), t.getCreatedBy().getFirstname(), t.getCreatedBy().getLastname(), t.getCreatedBy().getEmail(), t.getCreatedBy().getTenant().getId(), t.getCreatedBy().getRole().getId()
        );

        UserResponseDTO assignee = null;
        if (t.getAssignedTo() != null)
            assignee = new UserResponseDTO(t.getAssignedTo().getId(), t.getAssignedTo().getFirstname(),
                    t.getAssignedTo().getLastname(), t.getAssignedTo().getEmail(), t.getAssignedTo().getTenant().getId(), t.getAssignedTo().getRole().getId());


        return new TicketResponseDTO(t.getId(), t.getProject().getId(),
                t.getTitle(), t.getDescription(), status, category, t.getPriority(), t.getDueDate(), createdBy, assignee);
    }

}
