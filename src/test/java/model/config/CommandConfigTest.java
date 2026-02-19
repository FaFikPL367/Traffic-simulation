package model.config;

import org.junit.jupiter.api.Test;
import org.project.model.command.CommandType;
import org.project.model.config.CommandConfig;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with tests to - CommandConfig
 */
public class CommandConfigTest {

    @Test
    void giveNullCommandList_whenParsingCommandConfig_thenThrowException() {
        // Given
        List<CommandType> commandList = null;

        // When & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CommandConfig(commandList)
        );

        // Check exception message
        assertEquals("CommandConfig wasn't give in input file!", exception.getMessage());
    }

    @Test
    void giveEmptyCommandList_whenParsingCommandConfig_thenThrowException() {
        // Given
        List<CommandType> commandList = Collections.emptyList();

        // When & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CommandConfig(commandList)
        );

        // Check exception message
        assertEquals("CommandConfig is an empty list!", exception.getMessage());
    }
}
