package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.dto.InvitationDTO;
import com.zest.zestexperimentorbackend.persists.entities.Invitation;
import com.zest.zestexperimentorbackend.services.InvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${server.allowedorigin}", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/invitations")
    List<Invitation> allInvitations() {
        return invitationService.getAll();
    }

    @PostMapping("/invitations")
    @ResponseStatus(HttpStatus.OK)
    String addInvitation(@RequestBody InvitationDTO invitation) {
        Invitation dbRecord = new Invitation(invitation.getSource(), invitation.getType());
        var savedInvitation = invitationService.saveOne(dbRecord);
        return savedInvitation.getId();
    }


    @DeleteMapping("/invitations/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteInvitation(@PathVariable String id) {
        invitationService.deleteById(id);
    }
}
