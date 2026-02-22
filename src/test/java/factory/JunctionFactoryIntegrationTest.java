package factory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.project.factory.JunctionFactory;
import org.project.model.Junction;
import org.project.model.config.JunctionConfig;
import tools.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class with test for creating different junction valid or wrong
 */
public class JunctionFactoryIntegrationTest {

    // To parse JSON
    private final ObjectMapper mapper = new ObjectMapper();

    // Helper method to read example input file
    private JunctionConfig loadJunctionConfig(String inputFileName) {
        // Read JSON file from resources
        InputStream inputStream = getClass().getResourceAsStream("/junctions/" + inputFileName);
        assertNotNull(inputStream);
        return mapper.readValue(inputStream, JunctionConfig.class);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "valid_4_wave.json",
            "valid_t_junction.json",
            "valid_one_way.json",
            "valid_multiple_lanes.json"
    })
    void giveValidJunction_whenReadingInputFile_thenReturnJunction(String inputFileName) throws FileNotFoundException {
        // Given
        JunctionConfig junctionConfig = loadJunctionConfig(inputFileName);

        // When
        Junction junction = JunctionFactory.createJunction(junctionConfig);

        // Then
        assertNotNull(junction);
        assertNotNull(junction.allLanes());
    }
}
