package pt.uminho.ceb.biosystems.merlindbconnector;

import java.util.Map;

import org.optflux.core.utils.iowizard.readers.AbstractOptFluxReader;
import org.optflux.core.utils.iowizard.readers.WarningsException;
import org.optflux.core.utils.iowizard.readers.configurationPanels.AbstractWizardConfigurationPanel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.interfaces.IContainerBuilder;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.MerlinDBReader;
import pt.uminho.sysbio.common.database.connector.databaseAPI.ProjectAPI;
import pt.uminho.sysbio.common.database.connector.datatypes.Connection;
import pt.uminho.sysbio.common.database.connector.datatypes.DatabaseAccess;
import pt.uminho.sysbio.common.database.connector.datatypes.H2DatabaseAccess;
import pt.uminho.sysbio.common.database.connector.datatypes.MySQLDatabaseAccess;

public class MerlinDBOptfluxReader extends AbstractOptFluxReader {
	
	public static final String NAME = "Nome";
	public static final String PASSWORD = "Password";
	public static final String HOST = "Host";
	public static final String PORT = "port";
	public static final String DATABASE = "database";
	public static final String MODELNAME = "modelName";
	public static final String ISCOMPARTMENTALISEDMODEL = "isCompartmentalisedModel";
	public static final String ORGANISMNAME = "organismName";
	public static final String BIOMASSNAME = "biomassName";
	public static final String DATABASETYPE = "H2";
	public static final String PATH = "path";
	private MerlinDBOptfluxReaderConfigPanel configpanel;
	
	
	public MerlinDBOptfluxReader() {
		super();
		configpanel = new MerlinDBOptfluxReaderConfigPanel();
		configpanel.setReader(this);
	}
	
	@Override
	public String getReaderName() {
		// TODO Auto-generated method stub
		return "Merlin Database";
	}

	@Override
	public boolean needsDrainsIdentification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createContainer() throws Exception, WarningsException {
		Map<String, Object> mapa = configpanel.getConfigurationMap();
		DatabaseAccess databaseConnector;
		if (mapa.get(DATABASETYPE)=="MYSQL"){
			databaseConnector=new MySQLDatabaseAccess((String)mapa.get(NAME), (String)mapa.get(PASSWORD), (String)mapa.get(HOST), mapa.get(PORT).toString(), (String)mapa.get(DATABASE));
		}
		else{
			databaseConnector=new H2DatabaseAccess((String)mapa.get(NAME), (String)mapa.get(PASSWORD), (String)mapa.get(DATABASE), (String)mapa.get(PATH));
		}
		Connection conn = new Connection(databaseConnector);
		boolean isCompartmentalised = ProjectAPI.isCompartmentalisedModel(conn);
		conn.closeConnection();
		IContainerBuilder a = new MerlinDBReader(databaseConnector,(String) mapa.get(MODELNAME),isCompartmentalised, (String)mapa.get(ORGANISMNAME),(String) mapa.get(BIOMASSNAME));
		Container container = new Container(a);
		if (container.getReactions().size()>0 && container.getMetabolites().size()>0){
			this.container = container;
		}else{
			Workbench.getInstance().error("Error! The selected container is empty");
		}
	}

	@Override
	public boolean needsSelectionFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsConfiguration() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean needsVerification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AbstractWizardConfigurationPanel getConfigurationPanel() throws Exception {
		// TODO Auto-generated method stub
		return configpanel;
	}
	
	@Override
	public void resetReader() {
		super.resetReader();
	}

}
