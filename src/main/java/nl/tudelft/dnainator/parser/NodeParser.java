package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import java.io.FileInputStream;
import java.util.Map;

public abstract class NodeParser {

    protected SequenceFactory sf;

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
     * @throws NumberFormatException
     * @throws InvalidHeaderFormatException
     */
    public abstract Map<String, Sequence> parse(FileInputStream fIn)
            throws NumberFormatException, InvalidHeaderFormatException;
}
