package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.model.command.CommandType;

import java.util.List;

/**
 * A record that contains all commands read from the input JSON file.
 * @param commandList A list of commands for the simulation.
 */
public record CommandConfig(
        @JsonProperty("commands") List<CommandType> commandList
) {

    // Constructor for validating read values
    public CommandConfig {
        if (commandList == null) throw new IllegalArgumentException("CommandConfig wasn't give in input file!");
        if (commandList.isEmpty()) throw new IllegalArgumentException("CommandConfig is an empty list!");
    }
}
