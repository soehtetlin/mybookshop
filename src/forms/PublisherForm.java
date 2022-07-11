package forms;

	import java.awt.BorderLayout;
	import java.awt.EventQueue;

	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;

	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Optional;
	import javax.swing.JTable;
	import java.awt.Color;
	import javax.swing.JTextField;
	import javax.swing.border.LineBorder;
	import javax.swing.table.DefaultTableColumnModel;
	import javax.swing.table.DefaultTableModel;
	import javax.swing.table.TableColumn;

	import entities.Publisher;
	import services.PublisherService;

	import javax.swing.JButton;
import javax.swing.ListSelectionModel;

	public class PublisherForm extends JPanel {

		private JPanel contentPane;
		private JTable tblPublisher;
		private JTextField txtSupId;
		private JTextField txtSupPhone;
		private JTextField txtSupAddress;
		private JTextField txtSupName;
		private JTextField txtSupMail;
		private JButton btnSave, btnUpdate, btnDelete, btnCancel;
		private Publisher publisher;
		private PublisherService pubService;
		private List<Publisher> originalPublisherList = new ArrayList<>();

	    private DefaultTableModel dtm = new DefaultTableModel();

		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						PublisherForm frame = new PublisherForm();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		/**
		 * Create the frame.
		 */
		public PublisherForm() {
			pubService = new PublisherService();
			initialize();
			setTableDesign();
			loadAllPublishers(Optional.empty());
		}
		
		private void initialize() {
			
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 802, 492);
			setBorder(new EmptyBorder(5, 5, 5, 5));
			//setContentPane(contentPane);
			setLayout(null);
			
			JPanel panel = new JPanel();
			panel.setBounds(0, 10, 786, 153);
			add(panel);
			panel.setLayout(null);
			
			JLabel lblPublisherId = new JLabel("Publisher Id");
			lblPublisherId.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblPublisherId.setEnabled(false);
			lblPublisherId.setBounds(93, 21, 92, 28);
			panel.add(lblPublisherId);
			
			JLabel lblPublisherPhNo = new JLabel("Phone No");
			lblPublisherPhNo.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblPublisherPhNo.setEnabled(false);
			lblPublisherPhNo.setBounds(93, 59, 113, 28);
			panel.add(lblPublisherPhNo);
			
			JLabel lblPublisherAds = new JLabel("Address");
			lblPublisherAds.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblPublisherAds.setEnabled(false);
			lblPublisherAds.setBounds(93, 97, 92, 28);
			panel.add(lblPublisherAds);
			
			JLabel lblPublisherName = new JLabel("Publisher Name");
			lblPublisherName.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblPublisherName.setEnabled(false);
			lblPublisherName.setBounds(429, 21, 107, 28);
			panel.add(lblPublisherName);
			
			JLabel lblPublisherMail = new JLabel("Mail");
			lblPublisherMail.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblPublisherMail.setEnabled(false);
			lblPublisherMail.setBounds(429, 59, 92, 28);
			panel.add(lblPublisherMail);
			
			txtSupId = new JTextField();
			txtSupId.setFont(new Font("Tahoma", Font.BOLD, 14));
			txtSupId.setBounds(217, 23, 125, 25);
			panel.add(txtSupId);
			txtSupId.setColumns(10);
			
			txtSupPhone = new JTextField();
			txtSupPhone.setFont(new Font("Tahoma", Font.BOLD, 14));
			txtSupPhone.setColumns(10);
			txtSupPhone.setBounds(217, 65, 125, 25);
			panel.add(txtSupPhone);
			
			txtSupAddress = new JTextField();
			txtSupAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
			txtSupAddress.setColumns(10);
			txtSupAddress.setBounds(217, 103, 125, 25);
			panel.add(txtSupAddress);
			
			txtSupName = new JTextField();
			txtSupName.setFont(new Font("Tahoma", Font.BOLD, 14));
			txtSupName.setColumns(10);
			txtSupName.setBounds(569, 21, 125, 25);
			panel.add(txtSupName);
			
			txtSupMail = new JTextField();
			txtSupMail.setFont(new Font("Tahoma", Font.BOLD, 14));
			txtSupMail.setColumns(10);
			txtSupMail.setBounds(569, 65, 125, 25);
			panel.add(txtSupMail);
			
			btnDelete = new JButton("Delete");
			btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnDelete.setBounds(580, 125, 91, 25);
			panel.add(btnDelete);
			
			btnUpdate = new JButton("Update");
			btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnUpdate.setBounds(472, 125, 91, 25);
			panel.add(btnUpdate);
			
			btnSave = new JButton("Save");
			btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnSave.setBounds(366, 125, 91, 25);
			panel.add(btnSave);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnCancel.setBounds(683, 125, 91, 25);
			panel.add(btnCancel);
			
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
			panel_1.setBounds(10, 173, 764, 270);
			add(panel_1);
			panel_1.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
		    scrollPane.setBounds(0, 0, 764, 270);
			panel_1.add(scrollPane);

			tblPublisher = new JTable();
			tblPublisher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblPublisher.setFont(new Font("Tahoma", Font.PLAIN, 13));
			tblPublisher.setBackground(new Color(255, 250, 240));
			tblPublisher.setForeground(Color.DARK_GRAY);
			tblPublisher.setBounds(150, 251, 555, -184);
			scrollPane.setViewportView(tblPublisher);
			
			buttonOnClick();
		}
		
		private void setTableDesign() {
		        dtm.addColumn("ID");
		        dtm.addColumn("Name");
		        dtm.addColumn("Phone");
		        dtm.addColumn("Email");
		        dtm.addColumn("Address");
		        
		        this.tblPublisher.setModel(new DefaultTableModel(
		        	new Object[][] {
		        	},
		        	new String[] {
		        		"ID", "Name", "Phone", "Address", "Mail"
		        	}
		        ));
		        
//		        DefaultTableColumnModel tcm=(DefaultTableColumnModel) tblPublisher.getColumnModel();
//		        TableColumn tc= tcm.getColumn(0);
//		        tc.setWidth(WIDTH);
		        
		  }
		
		private void clearForm() {
			txtSupName.setText("");
			txtSupPhone.setText("");
			txtSupAddress.setText("");
			txtSupMail.setText("");

		}
		
		private void buttonOnClick() {
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

	                if (publisher != null && publisher.getId() != null) {

	                    publisher.setName(txtSupName.getText());
	                    publisher.setContact_no(txtSupPhone.getText());
	                    publisher.setAddress(txtSupAddress.getText());
	                    publisher.setEmail(txtSupMail.getText());
	                    
	                    if (!publisher.getName().isBlank()) {

	                        pubService.updatePublisher(String.valueOf(publisher.getId()), publisher);
	                        clearForm();
	                        loadAllPublishers(Optional.empty());
	                        publisher = null;
	                    } else {
	                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
	                    }
	                } else {
	                    Publisher publisher = new Publisher();
	                    publisher.setName(txtSupName.getText());
	                    publisher.setName(txtSupName.getText());
	                    publisher.setContact_no(txtSupPhone.getText());
	                    publisher.setAddress(txtSupAddress.getText());
	                    publisher.setEmail(txtSupMail.getText());
	                    
	                    if (null != publisher.getName() && !publisher.getName().isBlank()) {

	                        pubService.savePublisher(publisher);
	                        clearForm();
	                        loadAllPublishers(Optional.empty());
	                    } else {
	                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
	                    }
	                }
	            }
			});
		}
		
		private void loadAllPublishers(Optional<List<Publisher>> optionalPublisher) {
			this.dtm = (DefaultTableModel) this.tblPublisher.getModel();
			this.dtm.getDataVector().removeAllElements();
			this.dtm.fireTableDataChanged();
			
			this.originalPublisherList = this.pubService.findAllPublishers();
			List<Publisher> pubList= optionalPublisher.orElseGet(() -> originalPublisherList);
			pubList.forEach(e -> {
			      Object[] row = new Object[7];
			      row[0] = e.getId();
			      row[1] = e.getName();
			      row[2] = e.getContact_no();
			      row[4] = e.getAddress();
			      row[5] = e.getEmail();
			      dtm.addRow(row);
			  });
			
			this.tblPublisher.setModel(dtm);
		}

	


}
