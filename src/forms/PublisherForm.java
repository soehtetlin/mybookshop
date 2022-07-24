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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import entities.Publisher;
import services.PublisherService;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PublisherForm extends JPanel {

	private JPanel contentPane, panel;
	private JTable tblPublisher;
	private JTextField txtSupPhone;
	private JTextField txtSupAddress;
	private JTextField txtSupName;
	private JTextField txtSupMail;
	private JButton btnSave, btnUpdate, btnDelete, btnCancel;
	private JLabel lblPublisherPhNo, lblPublisherAds, lblPublisherName, lblPublisherMail;
	private Publisher publisher;
	private PublisherService pubService;
	private List<Publisher> originalPublisherList = new ArrayList<>();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private DefaultTableModel dtm = new DefaultTableModel();

	public PublisherForm() {
		pubService = new PublisherService();
		initialize();
		setTableDesign();
		loadAllPublishers(Optional.empty());
	}

	private void initialize() {

		setBounds(100, 100, 802, 492);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		panel = new JPanel();

		lblPublisherPhNo = new JLabel("Phone No");
		cLayout.setLabel(lblPublisherPhNo);

		lblPublisherAds = new JLabel("Address");
		cLayout.setLabel(lblPublisherAds);

		lblPublisherName = new JLabel("Publisher Name");
		cLayout.setLabel(lblPublisherName);

		lblPublisherMail = new JLabel("Mail");
		cLayout.setLabel(lblPublisherMail);

		txtSupPhone = new JTextField();
		cLayout.setTextField(txtSupPhone);

		txtSupAddress = new JTextField();
		cLayout.setTextField(txtSupAddress);

		txtSupName = new JTextField();
		cLayout.setTextField(txtSupName);

		txtSupMail = new JTextField();
		cLayout.setTextField(txtSupMail);

		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);
		btnDelete.setVisible(false);

		btnUpdate = new JButton("Update");
		cLayout.setButton(btnUpdate);
		btnUpdate.setVisible(false);

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);

		JPanel panel_1 = new JPanel();

		JScrollPane scrollPane = new JScrollPane();

		tblPublisher = new JTable();

		scrollPane.setViewportView(tblPublisher);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE))
						.addGap(0)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(5)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).addGap(10)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addGap(34)));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE));
		panel_1.setLayout(gl_panel_1);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(211)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(191, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup().addGap(39)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblPublisherAds, GroupLayout.PREFERRED_SIZE, 92,
												GroupLayout.PREFERRED_SIZE)
										.addGap(32)
										.addComponent(txtSupAddress, GroupLayout.PREFERRED_SIZE, 125,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
										.addComponent(lblPublisherMail, GroupLayout.PREFERRED_SIZE, 92,
												GroupLayout.PREFERRED_SIZE)
										.addGap(48).addComponent(txtSupMail, GroupLayout.PREFERRED_SIZE, 125,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblPublisherName).addGap(17)
										.addComponent(txtSupName, GroupLayout.PREFERRED_SIZE, 125,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
										.addComponent(lblPublisherPhNo, GroupLayout.PREFERRED_SIZE, 113,
												GroupLayout.PREFERRED_SIZE)
										.addGap(27).addComponent(txtSupPhone, GroupLayout.PREFERRED_SIZE, 125,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(98)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(28)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPublisherPhNo, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtSupPhone,
								GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblPublisherName, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtSupName,
								GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(19)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPublisherMail, GroupLayout.PREFERRED_SIZE, 28,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtSupMail,
												GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup().addGap(18)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPublisherAds, GroupLayout.PREFERRED_SIZE, 28,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtSupAddress,
												GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))))
				.addGap(18)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		this.tblPublisher.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {

			if (!tblPublisher.getSelectionModel().isSelectionEmpty()) {
				btnSave.setVisible(false);
				btnUpdate.setVisible(true);
				btnDelete.setVisible(true);

				String id = tblPublisher.getValueAt(tblPublisher.getSelectedRow(), 0).toString();

				publisher = pubService.findById(id);

				txtSupName.setText(publisher.getName());
				txtSupAddress.setText(publisher.getAddress());
				txtSupMail.setText(publisher.getEmail());
				txtSupPhone.setText(publisher.getContact_no());
			}
		});

		buttonOnClick();
	}

	private void setTableDesign() {

		tblPublisher.setBounds(150, 251, 555, -184);
		cLayout.setTable(tblPublisher);

		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Phone");
		dtm.addColumn("Address");
		dtm.addColumn("Mail");

		this.tblPublisher.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Name", "Phone", "Address", "Mail" }));
	}

	private void clearForm() {
		txtSupName.setText("");
		txtSupPhone.setText("");
		txtSupAddress.setText("");
		txtSupMail.setText("");
		loadAllPublishers(Optional.empty());
	}

	private void buttonVisible() {
		btnSave.setVisible(true);
		btnUpdate.setVisible(false);
		btnCancel.setVisible(true);
		btnDelete.setVisible(false);
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
//					loadAllPublishers(Optional.empty());
					publisher = null;
				} else {
					JOptionPane.showMessageDialog(null, "Choose Publisher!");
				}

				buttonVisible();
			}

		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearForm();
				buttonVisible();
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
