package com.zest.zestexperimentorbackend.persists.entities.questions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@EqualsAndHashCode(callSuper = true,of = "exposureTime")
@Data
@JsonTypeName("CodeEvaluation")
public class CodeEvaluation extends BaseQuestion{
    protected long exposureTime;

    protected String codeText;
}
