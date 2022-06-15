package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> graph;
	private List<Business> vertices;
	private Map<Business, Double> medie;
	private List<Business> best;
	
	public Model() {
		this.dao =  new YelpDao();
	}
	
	public List<String> getAllCities() {
		List<String> cities = this.dao.getAllCities();
		Collections.sort(cities);
		return cities;
	}

	public String creaGrafo(String c, Integer a) {
		this.graph = new SimpleDirectedWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertices = this.dao.getBusinesses(c, a);
		Collections.sort(this.vertices);
		Graphs.addAllVertices(this.graph, this.vertices);
		
		this.medie = new HashMap<>();
		for(Business b: this.vertices) {
			double media = this.dao.getMedia(b, a);
			this.medie.put(b, media);
		}
		
		for(Business b1: this.vertices) {
			for(Business b2: this.vertices) {
				if(!b1.equals(b2)) {
					DefaultWeightedEdge edge = this.graph.getEdge(b1, b2);
					
					if(edge == null && this.graph.getEdge(b2, b1) == null) {
						double m1 = this.medie.get(b1);
						double m2 = this.medie.get(b2);
						double weight;
						if(m1 > m2) {
							weight = m1 - m2;
							edge = this.graph.addEdge(b2, b1);
							this.graph.setEdgeWeight(edge, weight);
						} else if(m1 < m2) {
							weight = m2 - m1;
							edge = this.graph.addEdge(b1, b2);
							this.graph.setEdgeWeight(edge, weight);
						} 
					}
				}
			}
		}
		
		return "GRAFO CREATO\n#Vertici: "+this.graph.vertexSet().size()+"\n#Archi: "+this.graph.edgeSet().size();
	}

	public String localeMigliore() {
		Business best = null;
		double sum = 0.0;
		
		for(Business b: this.vertices) {
			double in = 0;
			for(DefaultWeightedEdge e: this.graph.incomingEdgesOf(b)) {
				in += this.graph.getEdgeWeight(e);
			}
			double out = 0;
			for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(b)) {
				out += this.graph.getEdgeWeight(e);
			}
			
			double k = in - out;
			
			if(k >= sum) {
				best = b;
				sum = k;
			}
		}
		
		return "LOCALE MIGLIORE: "+best.getBusinessName();
	}

	public Business localeMigliore2() {
		Business best = null;
		double sum = 0.0;
		
		for(Business b: this.vertices) {
			double in = 0;
			for(DefaultWeightedEdge e: this.graph.incomingEdgesOf(b)) {
				in += this.graph.getEdgeWeight(e);
			}
			double out = 0;
			for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(b)) {
				out += this.graph.getEdgeWeight(e);
			}
			
			double k = in - out;
			
			if(k >= sum) {
				best = b;
				sum = k;
			}
		}
		
		return best;
	}
	
	public List<Business> calcolaPercorso(Business b, double x) {
		this.best = new ArrayList<Business>();
		List<Business> parziale = new ArrayList<Business>();
		parziale.add(b);
		Business end = this.localeMigliore2();
		this.ricorsiva(parziale, end, x, 0);
		return this.best;
	}
	
	private void ricorsiva(List<Business> parziale, Business end, double x, double weight) {
		if(parziale.get(parziale.size()-1).equals(end)) {
			if(this.best.isEmpty()) {
				this.best = new ArrayList<Business>(parziale);
			} else if(parziale.size() < this.best.size()) {
				this.best = new ArrayList<Business>(parziale);
			}
		} else {
			List<Business> near = Graphs.neighborListOf(this.graph, parziale.get(parziale.size()-1));
			for(Business b: near) {
				if(!parziale.contains(b)) {
					DefaultWeightedEdge edge = this.graph.getEdge(parziale.get(parziale.size()-1), b);
					
					if(edge != null && this.graph.getEdgeWeight(edge) >= weight+x) {
						parziale.add(b);
						this.ricorsiva(parziale, end, x, this.graph.getEdgeWeight(edge));
						parziale.remove(b);
					}
				}
			}
		}
	}

	public Graph<Business, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public List<Business> getBusinesses() {
		return this.vertices;
	}
	
}
