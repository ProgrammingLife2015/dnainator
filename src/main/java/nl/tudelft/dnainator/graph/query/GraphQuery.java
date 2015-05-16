package nl.tudelft.dnainator.graph.query;

public interface GraphQuery {

	void compile(GraphQueryDescription qd);

	void compile(IDsFilter iDsFilter);

	void compile(SourcesFilter sourcesFilter);

	void compile(PredicateFilter predicateFilter);

	void compile(RankStart rankStart);

	void compile(RankEnd rankEnd);

}
