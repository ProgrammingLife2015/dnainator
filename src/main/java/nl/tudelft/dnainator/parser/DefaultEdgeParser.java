package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceGraph;

import java.io.FileInputStream;
import java.util.Map;

public class DefaultEdgeParser implements EdgeParser {

    @Override
    public SequenceGraph parse(Map<String, Sequence> nodes, FileInputStream edgeIn) {
        return null;
    }

}
