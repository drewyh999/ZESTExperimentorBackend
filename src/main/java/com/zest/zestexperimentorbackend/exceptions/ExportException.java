package com.zest.zestexperimentorbackend.exceptions;

import com.zest.zestexperimentorbackend.persists.entities.Testee;

import java.io.IOException;

public class ExportException extends IOException {
    public ExportException(Testee testee){
        super("Exception happen while exporting" + testee.toString());
    }
}
