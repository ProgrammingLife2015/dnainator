package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceGraph;

import java.io.FileInputStream;
import java.util.Map;

public interface EdgeParser {

    /**
     * Connects the nodes given according to the edge
     * file.
     *
     * @param nodes  The nodes which should be connected.
     * @param edgeIn The file input stream describing the edges.
     * @return a complete SequenceGraph instance.
     */
    SequenceGraph parse(Map<String, Sequence> nodes, FileInputStream edgeIn);

}
