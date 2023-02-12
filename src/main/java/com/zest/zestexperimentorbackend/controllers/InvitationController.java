package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.dto.InvitationDTO;
import com.zest.zestexperimentorbackend.persists.entities.Invitation;
import com.zest.zestexperimentorbackend.services.InvitationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${server.allowedorigin}", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class InvitationController {
    private final InvitationService invitationService;

    private static final Log log = LogFactory.getLog(InvitationController.class);

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/invitations")
    List<Invitation> allInvitations() {
        log.info("Requested for all invitations");
        return invitationService.getAll();
    }

    @PostMapping("/invitations")
    @ResponseStatus(HttpStatus.OK)
    String addInvitation(@RequestBody InvitationDTO invitation) {
        Invitation dbRecord = new Invitation(invitation.getSource(), invitation.getType());
        var savedInvitation = invitationService.saveOne(dbRecord);
        log.info("New invitation generated: " + savedInvitation);
        return savedInvitation.getId();
    }


    @DeleteMapping("/invitations/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteInvitation(@PathVariable String id) {
        invitationService.deleteById(id);
        log.info("Deleted invitation: " + id);
    }
}
