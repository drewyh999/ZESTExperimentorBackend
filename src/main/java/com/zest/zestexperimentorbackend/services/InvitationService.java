package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Invitation;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InvitationService extends BaseCrudService<Invitation>{
    public InvitationService(@Qualifier("invitationRepository") BaseRepository<Invitation> repository) {
        super(repository);
    }
}
