package com.zest.zestexperimentorbackend.persists.entities.questions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName("DemographicQuestion")
public class DemographicQuestion extends BaseQuestion{

}
