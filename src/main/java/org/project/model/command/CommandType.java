package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.project.Simulation;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddVehicle.class, name = "addVehicle"),
        @JsonSubTypes.Type(value = Step.class, name = "step"),
        @JsonSubTypes.Type(value = AutoSimulation.class, name = "autoSimulation")
})

public interface CommandType {
    /**
     * Method that execute read command
     */
    void execute(Simulation simulation);
}
