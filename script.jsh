// Get Session ID

session.getId();

// ### GRAPHVIZ ###

// Get Pre-loaded Graph

var graph = session.getGraph("Customer 360");

// Filter by "transfer" Edges

var graph2 = graph.filter(
  new EdgeFilter("edge.label()='transfer'"), "graph2");

// Run PageRank

analyst.pagerank(graph2);

// Check the result

graph2.queryPgql(
  " SELECT a.account_no, a.pagerank " +
  " MATCH (a) " +
  " ORDER BY a.pagerank DESC "
).print();

// Run Strongly Connected Component

analyst.sccKosaraju(graph2);

// Check the result

graph2.queryPgql(
  " SELECT a.scc_kosaraju AS component_id, COUNT(a.account_no) AS count " +
  " FROM MATCH (a) " +
  " GROUP BY a.scc_kosaraju " +
  " ORDER BY a.scc_kosaraju "
).print();

// ### GRAPHVIZ ###



var graph3 = graph.filter(
  new EdgeFilter("edge.label()='purchased'"), "graph3");

//

var cs = graph3.<Integer>createChangeSet();
var rs = graph3.queryPgql("SELECT id(a), id(x) MATCH (a)-[]->(x)");
for (var r : rs) {
   var e = cs.addEdge(
     r.getInteger(2),r.getInteger(1)).setLabel("purchased_by");
}

graph3 = cs.build();
graph3.queryPgql(
  " SELECT ID(r), x.name, LABEL(r), a.account_no" +
  "  MATCH (x)-[r:purchased_by]->(a)" +
  " LIMIT 5"
).print();

var vertex = graph3.getVertex(201);
analyst.personalizedPagerank(graph3, vertex);

graph3.queryPgql(
"  SELECT ID(x), x.name, x.pagerank " +
"  MATCH (x) " +
"  WHERE x.type = 'merchant' " +
"    AND NOT EXISTS ( " +
"     SELECT * " +
"     MATCH (x)-[:purchased_by]->(a) " +
"     WHERE ID(a) = 201 " +
"    ) " +
"  ORDER BY x.pagerank DESC"
).print();
