package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * An abstract implementation for parsing node file input streams.
 */
public abstract class NodeParser {

    protected SequenceFactory sf;

    /**
     * Creates the parser with the provided SequenceFactory.
     *
     * @param sf The SequenceFactory to be used.
     */
    public NodeParser(SequenceFactory sf) {
        this.sf = sf;
    }

    /**
     * Tries to parse the given input stream to a Map in which the tuples denote the
     * header and data of the nodes.
     * The caller is responsible for closing the input stream.
     *
     * @param fIn The FileInputStream to be used for parsing.
     * @return A Map of String, Sequence tuples containing all nodes.
     * @throws NumberFormatException        Thrown when the input contains a NaN where it should
     *                                      not.
     * @throws InvalidHeaderFormatException Thrown when a FASTA header section is invalid.
     * @throws IOException Thrown when reading the FileInputStream fails.
     */
    public abstract Map<String, Sequence> parse(FileInputStream fIn) throws NumberFormatException,
            InvalidHeaderFormatException, IOException;
}
