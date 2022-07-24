package forms;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class TableCell extends JPanel {

	/**
	 * Create the panel.
	 */
	public TableCell() {

		ImageIcon imgedit = new ImageIcon(new ImageIcon(this.getClass().getResource("/edit.png")).getImage());
		ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/delete.png")).getImage());

		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		JButton btnedit = new JButton("");
		btnedit.setHorizontalAlignment(SwingConstants.LEADING);
		btnedit.setIcon(imgedit);
		btnedit.setContentAreaFilled(false);
		btnedit.setBorder(new EmptyBorder(5, 5, 5, 5));
		btnedit.setBackground(Color.red);

		JButton btndelete = new JButton("");
		btndelete.setIcon(img);
		btndelete.setContentAreaFilled(false);
		btndelete.setBorder(new EmptyBorder(5, 5, 5, 5));
		btndelete.setBackground(Color.red);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE).addGap(10)
						.addComponent(btndelete, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGroup(
						gl_panel.createParallelGroup(Alignment.LEADING).addComponent(btnedit).addComponent(btndelete))
						.addContainerGap(62, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}
}
