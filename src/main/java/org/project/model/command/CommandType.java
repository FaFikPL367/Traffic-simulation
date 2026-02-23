package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.project.Simulation;


/**
 * A common interface for all executable simulation commands.
 */
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
     * Executes the specific command logic on the provided simulation instance.
     * @param simulation The current simulation instance.
     */
    void execute(Simulation simulation);
}
