package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.entities.Invitation;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.InvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/invitations")
    List<Invitation> allInvitations(){
        return invitationService.getAll();
    }

    @PostMapping("/invitations")
    @ResponseStatus(HttpStatus.OK)
    void addInvitation(@RequestBody List<Invitation> invitationList){
        invitationService.save(invitationList);
    }


    @DeleteMapping("/invitations/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteInvitation(@PathVariable String id){
        invitationService.deleteById(id);
    }
}
