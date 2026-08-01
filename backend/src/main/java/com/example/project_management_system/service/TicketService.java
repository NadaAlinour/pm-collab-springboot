package com.example.project_management_system.service;

import com.example.project_management_system.Mapper;
import com.example.project_management_system.dto.*;
import com.example.project_management_system.entity.*;
import com.example.project_management_system.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepo;
    private final ProjectRepository projectRepo;
    private final StatusRepository statusRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    private final Mapper mapper;


    public TicketService(Mapper mapper, TicketRepository ticketRepo, ProjectRepository projectRepo, StatusRepository statusRepo, CategoryRepository categoryRepo, UserRepository userRepo)
    {
        this.mapper = mapper;
        this.ticketRepo = ticketRepo;
        this.projectRepo = projectRepo;
        this.statusRepo = statusRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    // have one method for ticket by id, and another for tickets with params (filters etc)
    public TicketResponseDTO getTicketById(Long id) {
        Ticket t = ticketRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return mapper.toTicketResponse(t);
    }

    // fetch all tickets (for now, later on filter and get by id)
    // huh no i wanna get tickets by tenant id or all tickets or with filters lol
    public List<TicketResponseDTO> getTickets(String title, Long status, Priority priority, Long category) {

        List <Ticket> tickets = ticketRepo.searchTickets(title.toLowerCase(), status, priority, category);
        // map tickets to ticket response DTOs
        return tickets.stream()
                .map(mapper::toTicketResponse)
                .toList();
    }

    public List<TicketResponseDTO> getAllTickets(Long projectId) {
        List <Ticket> tickets = ticketRepo.findAllByProjectId(projectId);
        return tickets.stream()
                .map(mapper::toTicketResponse)
                .toList();
    }


    // create ticket
    public TicketResponseDTO saveTicket(TicketRequestDTO dto) {
        // map dto to ticket entity
        Ticket reqTicket = new Ticket();

        // project
        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        reqTicket.setProject(project);

        // status from status id
        if (dto.getStatusId() != null) {
            Status status = statusRepo.findById(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));

            reqTicket.setStatus(status);
        }


        // category from category id
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            reqTicket.setCategory(category);
        }


        // created by
        Users user = userRepo.findById(dto.getCreatedBy())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        reqTicket.setCreatedBy(user);

        // assigned to
        Users assignee = null;
        if(dto.getAssigneeId() != null) {
            assignee = userRepo.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException(("User not found")));
            reqTicket.setAssignedTo(assignee);

        }


        reqTicket.setTitle((dto.getTitle()));
        reqTicket.setDescription(dto.getDescription());
        reqTicket.setDueDate(dto.getDueDate());

        // priority
        reqTicket.setPriority(dto.getPriority());

        Ticket ticket = ticketRepo.saveAndFlush(reqTicket);

        return mapper.toTicketResponse(ticket);


    }
}
