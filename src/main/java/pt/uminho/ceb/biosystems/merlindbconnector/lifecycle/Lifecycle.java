package pt.uminho.ceb.biosystems.merlindbconnector.lifecycle;

import org.optflux.core.utils.iowizard.ReadersList2;
import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.ceb.biosystems.merlindbconnector.MerlinDBOptfluxReader;

public class Lifecycle extends PluginLifecycle{
	
	public void start(){
		
		System.out.println("Starting Life Cycle");
		ReadersList2.getReadersList().addReader(new MerlinDBOptfluxReader());
		System.out.println("Terminating Life Cycle");
	}
}
