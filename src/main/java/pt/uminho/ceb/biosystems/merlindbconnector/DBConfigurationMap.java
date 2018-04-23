package pt.uminho.ceb.biosystems.merlindbconnector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.optflux.core.datatypes.project.Project;

import es.uvigo.ei.aibench.core.operation.annotation.Operation;

@Operation(description="My optflux")

public class DBConfigurationMap {
	static String nome, password, host, database, modelName,organismName,biomassName, ficheiro;
	static int port;	
	static boolean isCompartmentalisedModel;
	Project p;
	
	
	public static HashMap<String, Object> readTheFile(String file) throws IOException {	
		BufferedReader br = new BufferedReader(new FileReader(file));		
		HashMap<String, Object> hmap = new HashMap<String, Object>();			
		while(br.ready()){
			String line = br.readLine();
			String [] tokens = line.split(":");			
			hmap.put(tokens[0],tokens[1]);
		}
		br.close();
		return hmap;
	}
	
		
// Esta main não é usada	
	public static void main(String[] args) throws IOException {
		HashMap<String, Object> mapa = readTheFile("C:/Users/Utilizador/Desktop/optflux/experiencia.txt");
	}
}
