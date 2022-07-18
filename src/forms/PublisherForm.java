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
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import entities.Publisher;
import services.PublisherService;

import javax.swing.JButton;

public class PublisherForm extends JPanel {

	private JPanel contentPane;
	private JTable tblPublisher;
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

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 802, 492);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		// setContentPane(contentPane);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 10, 786, 153);
		add(panel);
		panel.setLayout(null);

		JLabel lblPublisherPhNo = new JLabel("Phone No");
		lblPublisherPhNo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisherPhNo.setEnabled(false);
		lblPublisherPhNo.setBounds(429, 28, 113, 28);
		panel.add(lblPublisherPhNo);

		JLabel lblPublisherAds = new JLabel("Address");
		lblPublisherAds.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisherAds.setEnabled(false);
		lblPublisherAds.setBounds(93, 75, 92, 28);
		panel.add(lblPublisherAds);

		JLabel lblPublisherName = new JLabel("Publisher Name");
		lblPublisherName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisherName.setEnabled(false);
		lblPublisherName.setBounds(93, 28, 107, 28);
		panel.add(lblPublisherName);

		JLabel lblPublisherMail = new JLabel("Mail");
		lblPublisherMail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisherMail.setEnabled(false);
		lblPublisherMail.setBounds(429, 75, 92, 28);
		panel.add(lblPublisherMail);

		txtSupPhone = new JTextField();
		txtSupPhone.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSupPhone.setColumns(10);
		txtSupPhone.setBounds(569, 30, 125, 25);
		panel.add(txtSupPhone);

		txtSupAddress = new JTextField();
		txtSupAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSupAddress.setColumns(10);
		txtSupAddress.setBounds(217, 77, 125, 25);
		panel.add(txtSupAddress);

		txtSupName = new JTextField();
		txtSupName.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSupName.setColumns(10);
		txtSupName.setBounds(217, 30, 125, 25);
		panel.add(txtSupName);

		txtSupMail = new JTextField();
		txtSupMail.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSupMail.setColumns(10);
		txtSupMail.setBounds(569, 77, 125, 25);
		panel.add(txtSupMail);

		btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(429, 125, 91, 25);
		btnDelete.setVisible(false);
		panel.add(btnDelete);

		btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(283, 125, 91, 25);
		btnUpdate.setVisible(false);
		panel.add(btnUpdate);

		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.setBounds(283, 125, 91, 25);
		panel.add(btnSave);

		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(429, 125, 91, 25);
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
		tblPublisher.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tblPublisher.setBackground(new Color(255, 250, 240));
		tblPublisher.setForeground(Color.DARK_GRAY);
		tblPublisher.setBounds(150, 251, 555, -184);
		scrollPane.setViewportView(tblPublisher);

		this.tblPublisher.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {

			if (!tblPublisher.getSelectionModel().isSelectionEmpty()) {
				btnSave.setVisible(false);
				btnUpdate.setVisible(true);
				btnCancel.setVisible(false);
				btnDelete.setVisible(true);
				String id = tblPublisher.getValueAt(tblPublisher.getSelectedRow(), 0).toString();

				publisher = pubService.findById(id);

				txtSupName.setText(publisher.getName());
//					txtSupId.setText(publisher.getId());
				txtSupAddress.setText(publisher.getAddress());
				txtSupMail.setText(publisher.getEmail());
				txtSupPhone.setText(publisher.getContact_no());
			}
		});

		buttonOnClick();
	}

	private void setTableDesign() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Phone");
		dtm.addColumn("Address");
		dtm.addColumn("Mail");

		this.tblPublisher.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Name", "Phone", "Address", "Mail" }));

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

	private void buttonVisible() {
		btnSave.setVisible(true);
		btnCancel.setVisible(true);

		btnDelete.setVisible(false);
		btnUpdate.setVisible(false);
	}

	private void buttonOnClick() {

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Publisher publisher = new Publisher();
				publisher.setName(txtSupName.getText());
				publisher.setName(txtSupName.getText());
				publisher.setContact_no(txtSupPhone.getText());
				publisher.setAddress(txtSupAddress.getText());
				publisher.setEmail(txtSupMail.getText());

				if (!publisher.getName().isBlank() && !publisher.getContact_no().isBlank()
						&& !publisher.getEmail().isBlank() && !publisher.getAddress().isBlank()) {

					pubService.savePublisher(publisher);
					clearForm();
					loadAllPublishers(Optional.empty());
				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
			}
		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (publisher != null && publisher.getId() != null) {

					publisher.setName(txtSupName.getText());
					publisher.setContact_no(txtSupPhone.getText());
					publisher.setAddress(txtSupAddress.getText());
					publisher.setEmail(txtSupMail.getText());

					if (!publisher.getName().isBlank() && !publisher.getContact_no().isBlank()
							&& !publisher.getEmail().isBlank() && !publisher.getAddress().isBlank()) {

						pubService.updatePublisher(String.valueOf(publisher.getId()), publisher);
						clearForm();
						loadAllPublishers(Optional.empty());
						publisher = null;
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					}
				}
				buttonVisible();
			}

		});

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (publisher != null) {
					pubService.deletePublisher(String.valueOf(publisher.getId()));
					clearForm();
					buttonVisible();
					loadAllPublishers(Optional.empty());
					publisher = null;
				} else {
					JOptionPane.showMessageDialog(null, "Choose Publisher!");
				}

			}

		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearForm();
			}

		});

	}

	private void loadAllPublishers(Optional<List<Publisher>> optionalPublisher) {
		this.dtm = (DefaultTableModel) this.tblPublisher.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalPublisherList = this.pubService.findAllPublishers();
		List<Publisher> pubList = optionalPublisher.orElseGet(() -> originalPublisherList);
		pubList.forEach(e -> {
			Object[] row = new Object[7];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getAddress();
			row[4] = e.getEmail();
			dtm.addRow(row);
		});

		this.tblPublisher.setModel(dtm);
	}

}
