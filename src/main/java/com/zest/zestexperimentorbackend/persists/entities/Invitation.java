package com.zest.zestexperimentorbackend.persists.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Invitations")
@Data
public class Invitation {
    @Id
    private String id;

    private String source;

    public Invitation(String source) {
        this.source = source;
    }
}
