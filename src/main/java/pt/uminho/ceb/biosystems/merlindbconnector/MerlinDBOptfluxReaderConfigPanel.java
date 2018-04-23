package pt.uminho.ceb.biosystems.merlindbconnector;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.optflux.core.utils.iowizard.readers.configurationPanels.AbstractWizardConfigurationPanel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.sysbio.common.database.connector.datatypes.DatabaseSchemas;
import pt.uminho.sysbio.common.database.connector.datatypes.Enumerators.DatabaseType;
import pt.uminho.sysbio.merlin.utilities.ConfFileReader;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MerlinDBOptfluxReaderConfigPanel extends AbstractWizardConfigurationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	public static String databasetype;
	private JComboBox<String> projectNameBox;
	public static JComboBox<DatabaseType> dataBaseTypeBox;
	public JButton searchDirectoryButton;
	private JFileChooser chooseDirectory;
	private JTextField directoryTextField;
	private MerlinDBOptfluxReader reader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MerlinDBOptfluxReaderConfigPanel window = new MerlinDBOptfluxReaderConfigPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MerlinDBOptfluxReaderConfigPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 36, 121, 231, 46 };
		gridBagLayout.rowHeights = new int[] {28, 28, 28, 28};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0, 0.0};
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0, 0.0};
		this.setLayout(gridBagLayout);

		JLabel lblDataBaseType = new JLabel("Database Type");
		lblDataBaseType.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblDataBaseType = new GridBagConstraints();
		gbc_lblDataBaseType.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDataBaseType.insets = new Insets(0, 0, 5, 5);
		gbc_lblDataBaseType.gridx = 1;
		gbc_lblDataBaseType.gridy = 0;
		this.add(lblDataBaseType, gbc_lblDataBaseType);

		dataBaseTypeBox = new JComboBox<DatabaseType>();
		dataBaseTypeBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				reader.resetContainer();
			}
		});
		dataBaseTypeBox.setModel(new DefaultComboBoxModel<DatabaseType>(new DatabaseType[] { DatabaseType.MYSQL, DatabaseType.H2 }));
		dataBaseTypeBox.setEditable(false);
		GridBagConstraints gbc_dataBaseTypeBox = new GridBagConstraints();
		gbc_dataBaseTypeBox.anchor = GridBagConstraints.NORTH;
		gbc_dataBaseTypeBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataBaseTypeBox.insets = new Insets(0, 0, 5, 5);
		gbc_dataBaseTypeBox.gridx = 2;
		gbc_dataBaseTypeBox.gridy = 0;
		this.add(dataBaseTypeBox, gbc_dataBaseTypeBox);
		dataBaseTypeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dataBaseTypeBox.getSelectedItem() == DatabaseType.MYSQL) {
					directoryTextField.setEnabled(true);
					searchDirectoryButton.setEnabled(true);
				}
				if (dataBaseTypeBox.getSelectedItem() == DatabaseType.H2) {
					directoryTextField.setEnabled(true);
					searchDirectoryButton.setEnabled(true);				
				}
			}
		});

		JLabel lblDirectory = new JLabel("Directory");
		lblDirectory.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblDirectory = new GridBagConstraints();
		gbc_lblDirectory.anchor = GridBagConstraints.WEST;
		gbc_lblDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblDirectory.gridx = 1;
		gbc_lblDirectory.gridy = 1;
		add(lblDirectory, gbc_lblDirectory);

		chooseDirectory = new JFileChooser();
		chooseDirectory.setCurrentDirectory(new java.io.File("."));
		chooseDirectory.setDialogTitle("Select merlin directory");
		chooseDirectory.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		searchDirectoryButton = new JButton("");
		searchDirectoryButton.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				reader.resetContainer();
			}
		});
		searchDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int v = chooseDirectory.showOpenDialog(null);
				if (v == JFileChooser.APPROVE_OPTION) { //if the user selects merlin.bat
					directoryChecker();													    
				};
			}
		});
		
		directoryTextField = new JTextField();
		directoryTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				reader.resetContainer();
			}
		});
		
		GridBagConstraints gbc_directoryTextField = new GridBagConstraints();
		gbc_directoryTextField.insets = new Insets(0, 0, 5, 5);
		gbc_directoryTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_directoryTextField.gridx = 2;
		gbc_directoryTextField.gridy = 1;
		add(directoryTextField, gbc_directoryTextField);
		directoryTextField.setColumns(10);
		searchDirectoryButton.setIcon(new ImageIcon(
				MerlinDBOptfluxReaderConfigPanel.class.getResource("/es/uvigo/ei/aibench/workbench/inputgui/images/fileopen.png")));
		GridBagConstraints gbc_searchDirectoryButton = new GridBagConstraints();
		gbc_searchDirectoryButton.insets = new Insets(0, 0, 5, 0);
		gbc_searchDirectoryButton.gridx = 3;
		gbc_searchDirectoryButton.gridy = 1;
		add(searchDirectoryButton, gbc_searchDirectoryButton);
		
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.NORTH;
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 2;
		gbc_btnConnect.gridy = 2;
		this.add(btnConnect, gbc_btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dataBaseTypeBox.getSelectedItem().equals(DatabaseType.MYSQL)) {
					 updateComboBox();
				}
				if (dataBaseTypeBox.getSelectedItem().equals(DatabaseType.H2)) {					 
					 updateComboBox();
				}
			}
		});

		JLabel lblProjectName = new JLabel("Database Name");
		lblProjectName.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblProjectName = new GridBagConstraints();
		gbc_lblProjectName.anchor = GridBagConstraints.WEST;
		gbc_lblProjectName.insets = new Insets(0, 0, 0, 5);
		gbc_lblProjectName.gridx = 1;
		gbc_lblProjectName.gridy = 3;
		this.add(lblProjectName, gbc_lblProjectName);

		projectNameBox = new JComboBox<String>();
		projectNameBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				reader.resetContainer();
			}
		});
		projectNameBox.setModel(new DefaultComboBoxModel<String>(new String[] {}));
		projectNameBox.setEditable(false);
		GridBagConstraints gbc_projectNameBox = new GridBagConstraints();
		gbc_projectNameBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_projectNameBox.insets = new Insets(0, 0, 0, 5);
		gbc_projectNameBox.gridx = 2;
		gbc_projectNameBox.gridy = 3;
		this.add(projectNameBox, gbc_projectNameBox);
		
	}	
	
		public void directoryChecker(){
			if (chooseDirectory.getSelectedFile().isFile()){
				File directoryAdress = chooseDirectory.getSelectedFile().getParentFile();
				File[] listOfFiles = directoryAdress.listFiles();
				boolean hasRunSH = false;
				boolean h2Database = false;
			    for (int i = 0; i < listOfFiles.length; i++) {
			    	if (listOfFiles[i].getName().equals("run.sh")){				    	
			    		hasRunSH = true;
			    	}else if(listOfFiles[i].getName().equals("h2Database.mv.db")){
			    		h2Database = true;
			    	}					      
			    }					
			    if (hasRunSH && h2Database){
			    	String directoryAdressName = directoryAdress.getAbsolutePath();
			    	directoryTextField.setText(directoryAdressName);
			    }else{
			    	Workbench.getInstance().error("Wrong File Selected");}	
			    
			} else if (chooseDirectory.getSelectedFile().isDirectory()){ //if the user selects merlin directory
				boolean hasRunSH = false;
				boolean h2Database = false;				
				File directoryAdress = chooseDirectory.getSelectedFile();
				File[] listOfFiles = directoryAdress.listFiles();
			    for (int i = 0; i < listOfFiles.length; i++) {
			    	if (listOfFiles[i].getName().equals("run.sh")){				    	
			    		hasRunSH = true;
			    	}else if(listOfFiles[i].getName().equals("h2Database.mv.db")){
			    		h2Database = true;
			    	}					      
			    }
			    if (hasRunSH && h2Database && chooseDirectory.getSelectedFile().isDirectory()){
			    	String directoryAdressName = directoryAdress.getAbsolutePath();
			    	directoryTextField.setText(directoryAdressName);
			    } else {			    
				    File directoryAdress2 = chooseDirectory.getSelectedFile().getParentFile();
					File[] listOfFiles2 = directoryAdress2.listFiles();
				    for (int i = 0; i < listOfFiles2.length; i++) {
				    	if (listOfFiles2[i].getName().equals("run.sh")){				    	
				    		hasRunSH = true;
				    	}else if(listOfFiles2[i].getName().equals("h2Database.mv.db")){
				    		h2Database = true;
				    	}					      
				    }
				    if(hasRunSH && h2Database && chooseDirectory.getSelectedFile().isDirectory()){
					    	String directoryAdressName = directoryAdress2.getAbsolutePath();
					    	directoryTextField.setText(directoryAdressName);
				    }else{
				    	Workbench.getInstance().error("Wrong Directory Selected");
				    }
			    }
			}			
		}
	
	public Map<String, Object> getConfigurationMap() {		
		String username = null, password = null, host = null, port = null;
		
		DatabaseType dataBaseType = (DatabaseType) dataBaseTypeBox.getSelectedItem();
		Map<String, String> credentials = loadDatabaseCredentials(dataBaseType, directoryTextField.getText()+"/conf");
		String path = directoryTextField.getText();
		if (dataBaseType.equals(DatabaseType.MYSQL)) {
			try {
				credentials = ConfFileReader.loadConf(path+"/conf/database_settings.conf");
				username = credentials.get("username");
				password = credentials.get("password");
				host = credentials.get("host");
				port = credentials.get("port");
			} catch (IOException e) {
				e.printStackTrace();
		Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+dataBaseType.toString()+"_settings.conf");
		}
		}
		else {
			try {
				credentials = ConfFileReader.loadConf(path+"/conf/database_settings.conf");
				username = credentials.get("username");
				password = credentials.get("password");
		} catch (IOException e) {
			e.printStackTrace();
			Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+dataBaseType.toString()+"_settings.conf");
		}
		}
		
		Map<String, Object> mapa1 = new HashMap<String, Object>();
		mapa1.put(MerlinDBOptfluxReader.NAME, username);
		mapa1.put(MerlinDBOptfluxReader.PASSWORD, password);
		mapa1.put(MerlinDBOptfluxReader.HOST, host);
		mapa1.put(MerlinDBOptfluxReader.PORT, port);
		mapa1.put(MerlinDBOptfluxReader.MODELNAME, "Teste1");
		mapa1.put(MerlinDBOptfluxReader.BIOMASSNAME, null);
		mapa1.put(MerlinDBOptfluxReader.ORGANISMNAME, "Organimo1");
		mapa1.put(MerlinDBOptfluxReader.DATABASE, projectNameBox.getSelectedItem());
		mapa1.put(MerlinDBOptfluxReader.DATABASETYPE, dataBaseTypeBox.getSelectedItem().toString());
		mapa1.put(MerlinDBOptfluxReader.PATH, directoryTextField.getText());
		return mapa1;
	}
	
	public String userNameField(){
		return null;
	}

	public Object getTheMap(String arg0) {
		return getConfigurationMap().get(arg0);
	}

	@Override
	public boolean validateConfigurations() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasSpecificSize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getXdimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYdimension() {
		// TODO Auto-generated method stub
		return 0;
	}
	  	  
	private void updateComboBox() {
	  
	  try {
		  setComboBox();
	  } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e1) {	  
		  Workbench.getInstance().error("No connection! Cause: " +e1.getMessage());
		  e1.printStackTrace();
	  }
}
	  	  
	private void setComboBox() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
				
		DefaultComboBoxModel<String> sch = new DefaultComboBoxModel<>();	
		
		DatabaseType dataBaseType = (DatabaseType) dataBaseTypeBox.getSelectedItem();				
		String username = null, password = null, host = null, port = null;
		Map<String, String> credentials = loadDatabaseCredentials(dataBaseType, directoryTextField.getText()+"/conf");
		String path = directoryTextField.getText();
		if (dataBaseType.equals(DatabaseType.MYSQL)) {
			try {
				credentials = ConfFileReader.loadConf(path+"/conf/database_settings.conf");
				username = credentials.get("username");
				password = credentials.get("password");
				host = credentials.get("host");
				port = credentials.get("port");
			} catch (IOException e) {
				e.printStackTrace();
		Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+dataBaseType.toString()+"_settings.conf");
		}
		}
		else {
			try {
				credentials = ConfFileReader.loadConf(path+"/conf/database_settings.conf");
				System.out.println(credentials);
				username = credentials.get("h2_username");
				password = credentials.get("h2_password");
		} catch (IOException e) {
			e.printStackTrace();
			Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+dataBaseType.toString()+"_settings.conf");
		}
		}
			
	DatabaseSchemas mSchemas = new DatabaseSchemas(username, password, host, port, dataBaseType);
		if(mSchemas.isConnected()) {
		  List<String> schemas = mSchemas.getSchemas();
		  
		  //get list of databases already used by projects, and remove those from schemas
		  
		  if(schemas.isEmpty()){
			  Workbench.getInstance().warn("No database available for merlin.");
		  }
		  sch = new DefaultComboBoxModel<>(schemas.toArray(new String[schemas.size()]));
		}		  
	  projectNameBox.setModel(sch);
	  projectNameBox.updateUI();
	}
	
	public boolean checkTable(String schema, String table, String path, String user, String password) throws SQLException {		
			Connection connection=(Connection) DriverManager.getConnection("jdbc:h2:"+path+"/h2Database/"+schema+";MODE=MySQL;DATABASE_TO_UPPER=FALSE;AUTO_SERVER=TRUE",user,password);
			Statement statement = null;
			ResultSet rs = null;
			boolean result = false;
			try {				
				statement = connection.createStatement();	
				statement.execute( "show tables");
				rs=statement.getResultSet();	
				while(rs.next())
					if(rs.getString(1).equalsIgnoreCase(table))
						result = true;				
				connection.close();
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();
			}
			return result;
	}
	
	public static Map<String,String> loadDatabaseCredentials(DatabaseType databaseType, String path){
		Map<String, String> credentials = null;
		if (databaseType.equals(DatabaseType.MYSQL)) {
			try {
				credentials = ConfFileReader.loadConf(path+"/database_settings.conf");
			} catch (IOException e) {
				e.printStackTrace();
				Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+databaseType.toString()+"_settings.cfg");
			}
		}
		else {
			try {
				credentials = ConfFileReader.loadConf(path+"/database_settings.conf");
			} catch (IOException e) {
				e.printStackTrace();
				Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+databaseType.toString()+"_settings.cfg");
			}
		}
		
		if(!((credentials.containsKey("username") && credentials.containsKey("password")) || (databaseType.equals(DatabaseType.MYSQL) && credentials.containsKey("host") && credentials.containsKey("port"))))
			Workbench.getInstance().error("Error! Check your database configuration file in merlin directory at /utilities/"+databaseType.toString()+"_settings.cfg");
		return credentials;
		}
	
	public void setReader(MerlinDBOptfluxReader reader){
		this.reader = reader;		
	}
}