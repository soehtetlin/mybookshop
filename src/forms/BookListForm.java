package forms;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entities.Author;
import entities.Book;
import entities.Category;
import services.AuthorService;
import services.BookService;
import services.CategoryService;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import forms.CreateLayoutProperties;
import forms.JpanelLoader;
import forms.BookListForm2.ButtonRenderer;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class BookListForm extends JPanel {

	private JTable table;
	private JLabel lbladd;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAdd;
	private JComboBox<String> cboCategory, cboAuthors;
	private DefaultTableModel dtm = new DefaultTableModel();
	private BookService bookService;
	private List<Book> originalBookList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private BookForm bookForm = new BookForm();
	private Book book = new Book();
	private JLabel showPhoto = new JLabel();
	private AuthorService authorService = new AuthorService();
	private CategoryService categoryService = new CategoryService();
	private List<Category> categoryList;
	private List<Author> authorList;
	

	public BookListForm() {
		initialize();
		setTableDesign();
		loadAllBooks(Optional.empty());
		loadAuthorForComboBox();
		loadCategoryForComboBox();
		buttonOnClick();
	}

	private void initialize() {

		this.bookService = new BookService();

		panel = new JPanel();
		
		table = new JTable();

		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);

		cboCategory = new JComboBox();
		cLayout.setComboBox(cboCategory);
//		loadCategoryForComboBox();

		cboAuthors = new JComboBox();
		cLayout.setComboBox(cboAuthors);
//		loadAuthorForComboBox();

		btnAdd = new JButton("Add New Book");
		cLayout.setButton(btnAdd);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 725, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, Short.MAX_VALUE).addContainerGap()));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(20)
					.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(cboCategory, 0, 100, Short.MAX_VALUE)
					.addGap(29)
					.addComponent(cboAuthors, 0, 100, Short.MAX_VALUE)
					.addGap(243)
					.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboAuthors, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	private void setTableDesign() {
		
		table.setSelectionBackground(new Color(191,148,228));
		table.setShowVerticalLines(false);
		table.setFocusable(false);
		table.setBounds(12, 254, 404, -216);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
		table.getTableHeader().setPreferredSize(new Dimension(80, 35));
		table.setRowHeight(65);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
	
		
		scrollPane.setViewportView(table);
		
		dtm.addColumn("Book Cover");
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Price");
		dtm.addColumn("Quantity");
		dtm.addColumn("Shelf No");
		dtm.addColumn("Remark");
		dtm.addColumn("Action");

		
		table.setModel(dtm);
		
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new ImagerRender());
		table.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(4).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(5).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(6).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
		
		//scroll 

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooksforList();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[7];
			row[0] = e.getPhoto();
			row[1] = e.getId();
			row[2] = e.getName();
			row[3] = e.getPrice();
			row[4] = e.getStockamount();
			row[5] = e.getShelf_number();
			row[6] = e.getRemark();
			
			
			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}

	private void buttonOnClick() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel, bookForm);
			}
		});
	}

	private class ImagerRender extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object photo, boolean arg2, boolean arg3, int arg4,
			int arg5) {
		
		System.out.println("Show store file address :" + photo.toString());

		ImageIcon imageIcon = null;
		
			imageIcon = new ImageIcon(new ImageIcon(photo.toString()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		return new JLabel(imageIcon);
	}

	}
	
	
	private void loadCategoryForComboBox() {
		cboCategory.addItem("All Category");
		System.out.println("Cate count " + categoryService.findAllCategories().size());
		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadAuthorForComboBox() {
		cboAuthors.addItem("All Author");
		System.out.println("author count " + authorService.findAllAuthors().size());

		this.authorList = this.authorService.findAllAuthors();
		this.authorList.forEach(a -> cboAuthors.addItem(a.getName()));
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
				int row, int column) {
			
			ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/edit.png")).getImage());
			setIcon(img);
			setContentAreaFilled(false);
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setBackground(Color.red);
			return this;
		}

	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton btn;
		private String lbl;
		private Boolean clicked;

		public ButtonEditor(JTextField textField) {
			super(textField);
			btn = new JButton();

			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String id = table.getValueAt(table.getSelectedRow(), 1).toString();
					book = bookService.findById(id);
					//book.setId(id);
					jloader.jPanelLoader(panel, new BookForm(book));

				}

			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row,
				int column) {
			// TODO Auto-generated method stub

			// set text to button , set clicked to true
			lbl = (obj == null) ? "" : obj.toString();
			System.out.println("lbl " + lbl);
			btn.setText(lbl);

			clicked = true;
			return btn;
//			return super.getTableCellEditorComponent(table, obj, isSelected, row, column);
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub

			if (clicked) {
				JOptionPane.showMessageDialog(btn, lbl + "Clicked");
			}

			// set it to false now that its click
			clicked = false;
			return new String(lbl);
		}

		@Override
		public boolean stopCellEditing() {
			// TODO Auto-generated method stub
			// set clicked to false first
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			// TODO Auto-generated method stub
			super.fireEditingStopped();
		}

	}

	
	
	
	
//	class EditDeleteButton extends JPanel implements TableCellRenderer {
//        public Component getTableCellRendererComponent(
//                            final JTable table, Object value,
//                            boolean isSelected, boolean hasFocus,
//                            int row, int column) {
//                //this.add( new JTextField( value.toString()  ) );
//        	JButton hello = new JButton();
//        	hello.addActionListener(new ActionListener() {
//
//    			@Override
//    			public void actionPerformed(ActionEvent e) {
//    				// TODO Auto-generated method stub
//    				JOptionPane.showMessageDialog(null, "Hello");
//    			}
//
//    		});
//        	
//        	JButton test = new JButton();
//        	test.setBounds(new Rectangle(600, 0, 0, 0));
////    		test.setMargin(new Insets(2, 1, 2, 1));
////    		test.setIconTextGap(1);
//    		test.setBorderPainted(false);
//    		test.setForeground(SystemColor.textHighlightText);
//    		test.setHorizontalAlignment(SwingConstants.CENTER);
//    		test.setBackground(new Color(153, 51, 204));
//    		test.setFont(new Font("Tahoma", Font.BOLD, 14));
//    		test.setOpaque(true);
//    		test.setForeground(Color.WHITE);
//    		test.setBorder(new LineBorder(new Color(0, 0, 0)));
//    		test.setRequestFocusEnabled(false);
//
//        	hello.addActionListener(new ActionListener() {
//
//    			@Override
//    			public void actionPerformed(ActionEvent e) {
//    				// TODO Auto-generated method stub
//    				JOptionPane.showMessageDialog(null, "test");
//    			}
//
//    		});
//                this.add(hello);
//                this.add(test);
//                return this;
//        }
//	
////	class ButtonRenderer extends JButton implements TableCellRenderer{
////
////		public ButtonRenderer() {
////			setOpaque(true);
////		}
////		@Override
////		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
////				int row, int column) {
////			ImageIcon imgedit = new ImageIcon(new ImageIcon(this.getClass().getResource("/edit.png")).getImage());
////			ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/delete.png")).getImage());
////			
////			JPanel panel = new JPanel();
////			
////			GroupLayout groupLayout = new GroupLayout(this);
////			groupLayout.setHorizontalGroup(
////				groupLayout.createParallelGroup(Alignment.LEADING)
////					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
////			);
////			groupLayout.setVerticalGroup(
////				groupLayout.createParallelGroup(Alignment.LEADING)
////					.addGroup(groupLayout.createSequentialGroup()
////						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
////						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
////			);
////			
////			JButton btnedit = new JButton("");
////			btnedit.setHorizontalAlignment(SwingConstants.LEADING);
////			btnedit.setIcon(imgedit);
////			btnedit.setContentAreaFilled(false);
////			btnedit.setBorder(new EmptyBorder(5, 5, 5, 5));
////			btnedit.setBackground(Color.red);
////			
////			JButton btndelete = new JButton("");
////			btndelete.setIcon(img);
////			btndelete.setContentAreaFilled(false);
////			btndelete.setBorder(new EmptyBorder(5, 5, 5, 5));
////			btndelete.setBackground(Color.red);
////			GroupLayout gl_panel = new GroupLayout(panel);
////			gl_panel.setHorizontalGroup(
////				gl_panel.createParallelGroup(Alignment.LEADING)
////					.addGroup(gl_panel.createSequentialGroup()
////						.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
////						.addGap(10)
////						.addComponent(btndelete, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
////						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
////			);
////			gl_panel.setVerticalGroup(
////				gl_panel.createParallelGroup(Alignment.LEADING)
////					.addGroup(gl_panel.createSequentialGroup()
////						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
////							.addComponent(btnedit)
////							.addComponent(btndelete))
////						.addContainerGap(62, Short.MAX_VALUE))
////			);
////			panel.setLayout(gl_panel);
////			setLayout(groupLayout);
////			btnedit.addActionListener(new ActionListener() {
////
////				@Override
////				public void actionPerformed(ActionEvent e) {
////					// TODO Auto-generated method stub
////					String id = table.getValueAt(table.getSelectedRow(), 1).toString();
////					book = bookService.findById(id);
////					book.setId(id);
////					jloader.jPanelLoader(panel, new BookForm(book));
////					
////				}
////				
////			});
////			
////			btndelete.addActionListener(new ActionListener() {
////
////				@Override
////				public void actionPerformed(ActionEvent e) {
////					// TODO Auto-generated method stub
////					String id = table.getValueAt(table.getSelectedRow(), 1).toString();
////					book = bookService.findById(id);
////					book.setId(id);
////					jloader.jPanelLoader(panel, new BookForm(book));
////					
////				}
////				
////			});
////			return this;
////		}
////		
////	}
//	
////	class ButtonEditor extends DefaultCellEditor{
////
////		protected JButton btn,btndelete;
////		private String lbl;
////		private Boolean clicked;
////		
////		ImageIcon imgedit = new ImageIcon(new ImageIcon(this.getClass().getResource("/edit.png")).getImage());
////		ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/delete.png")).getImage());
////		
////		JPanel panel = new JPanel();
////		
////		GroupLayout groupLayout = new GroupLayout(this);
////		groupLayout.setHorizontalGroup(
////			groupLayout.createParallelGroup(Alignment.LEADING)
////				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
////		);
////		groupLayout.setVerticalGroup(
////			groupLayout.createParallelGroup(Alignment.LEADING)
////				.addGroup(groupLayout.createSequentialGroup()
////					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
////					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
////		);
////		
////		JButton btnedit = new JButton("");
////		btnedit.setHorizontalAlignment(SwingConstants.LEADING);
////		btnedit.setIcon(imgedit);
////		btnedit.setContentAreaFilled(false);
////		btnedit.setBorder(new EmptyBorder(5, 5, 5, 5));
////		btnedit.setBackground(Color.red);
////		
////		JButton btndelete = new JButton("");
////		btndelete.setIcon(img);
////		btndelete.setContentAreaFilled(false);
////		btndelete.setBorder(new EmptyBorder(5, 5, 5, 5));
////		btndelete.setBackground(Color.red);
////		GroupLayout gl_panel = new GroupLayout(panel);
////		gl_panel.setHorizontalGroup(
////			gl_panel.createParallelGroup(Alignment.LEADING)
////				.addGroup(gl_panel.createSequentialGroup()
////					.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
////					.addGap(10)
////					.addComponent(btndelete, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
////					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
////		);
////		gl_panel.setVerticalGroup(
////			gl_panel.createParallelGroup(Alignment.LEADING)
////				.addGroup(gl_panel.createSequentialGroup()
////					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
////						.addComponent(btnedit)
////						.addComponent(btndelete))
////					.addContainerGap(62, Short.MAX_VALUE))
////		);
////		panel.setLayout(gl_panel);
////		setLayout(groupLayout);
////		btnedit.addActionListener(new ActionListener() {
////
////			@Override
////			public void actionPerformed(ActionEvent e) {
////				// TODO Auto-generated method stub
////				String id = table.getValueAt(table.getSelectedRow(), 1).toString();
////				book = bookService.findById(id);
////				book.setId(id);
////				jloader.jPanelLoader(panel, new BookForm(book));
////				
////			}
////			
////		});
////		
////		btndelete.addActionListener(new ActionListener() {
////
////			@Override
////			public void actionPerformed(ActionEvent e) {
////				// TODO Auto-generated method stub
////				String id = table.getValueAt(table.getSelectedRow(), 1).toString();
////				book = bookService.findById(id);
////				book.setId(id);
////				jloader.jPanelLoader(panel, new BookForm(book));
////				
////			}
////			
////		});
////		return this;
////	}
//
//			
//		}
//
//		@Override
//		public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row,
//				int column) {
//			// TODO Auto-generated method stub
//			
//			//set text to button , set clicked to true
//			lbl = (obj==null) ? "":obj.toString();
//			System.out.println("lbl "+lbl);
//			btn.setText(lbl);
//			
//			clicked=true;
//			return btn;
////			return super.getTableCellEditorComponent(table, obj, isSelected, row, column);
//		}

//		@Override
//		public Object getCellEditorValue() {
//			// TODO Auto-generated method stub
//			
//			if(clicked) {
//				JOptionPane.showMessageDialog(btn, lbl + "Clicked");
//			}
//			
//			//set it to false now that its click
//			clicked = false;
//			return new String(lbl);
//		}
//		
//		@Override
//		public boolean stopCellEditing() {
//			// TODO Auto-generated method stub
//			//set clicked to false first
//			return super.stopCellEditing();
//		}
//		
//		@Override
//		protected void fireEditingStopped() {
//			// TODO Auto-generated method stub
//			super.fireEditingStopped();
//		}
//		
//		
//	}
//	private class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
//	    WordWrapCellRenderer() {
//	        setLineWrap(true);
//	        setWrapStyleWord(true);
//	        setAlignmentX(CENTER_ALIGNMENT);
//	          setAlignmentY(CENTER_ALIGNMENT);
//	          setFont(new Font("Tahoma", Font.BOLD, 12));
//	          
//	        
//	        
//	    }

//	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//	    	  setText( (value == null) ? "" : value.toString() );
//	          setSize(160, 120);
//	      
//
//	          //  Recalculate the preferred height now that the text and renderer width have been set.
//
////	          int preferredHeight = getPreferredSize().height;
////
////	          if (table.getRowHeight(row) != preferredHeight)
////	          {
////	              table.setRowHeight(row, preferredHeight);
////	          }
//
//	          return this;
//	      }
	
	    
	}
//}



//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.SQLException;
//import javax.swing.ImageIcon;
//
//public class CellAction extends TableCustomCell {
//
//    public CellAction() {
//        initComponents();
//    }
//
//    private void addEvent(TableCustom table, TableRowData data, int row) {
//        cmdEdit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                if (data.isEditing()) {
//                    table.cancelEditRowAt(row);
//                    cmdEdit.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/edit.png")));
//                } else {
//                    table.editRowAt(row);
//                    cmdEdit.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/cancel.png")));
//                }
//            }
//        });
//        cmdDelete.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                int staffID = ((ModelStaff) data).getStaffID();
//                if (staffID != 0) {
//                    try {
//                        new ServiceStaff().deleteStaff(staffID);
//                        table.deleteRowAt(getRow(), true);
//                    } catch (SQLException e) {
//                        System.err.println(e);
//                    }
//                } else {
//                    table.deleteRowAt(getRow(), true);
//                }
//            }
//        });
//    }
//
//    private void checkIcon(TableRowData data) {
//        if (data.isEditing()) {
//            cmdEdit.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/cancel.png")));
//        } else {
//            cmdEdit.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/edit.png")));
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
//    private void initComponents() {
//
//        cmdEdit = new com.raven.swing.Button();
//        cmdDelete = new com.raven.swing.Button();
//
//        cmdEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/edit.png"))); // NOI18N
//
//        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/delete.png"))); // NOI18N
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(cmdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addComponent(cmdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
//                    .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//    }// </editor-fold>//GEN-END:initComponents
//
//    @Override
//    public void setData(Object o) {
//
//    }
//
//    @Override
//    public Object getData() {
//        return null;
//    }
//
//    @Override
//    public Component createComponentCellRender(TableCustom table, TableRowData data, int row, int column) {
//        CellAction cell = new CellAction();
//        cell.checkIcon(data);
//        cell.addEvent(table, data, row);
//        return cell;
//    }
//
//    @Override
//    public Component createComponentCellRenderOnEditor(TableCustom table, TableRowData data, int row, int column) {
//        return null;
//    }
//
//    @Override
//    public TableCustomCell createComponentCellEditor(TableCustom table, TableRowData data, Object o, int row, int column) {
//        CellAction cell = new CellAction();
//        cell.checkIcon(data);
//        cell.addEvent(table, data, row);
//        return cell;
//    }
//
//
//    // Variables declaration - do not modify//GEN-BEGIN:variables
//    private com.raven.swing.Button cmdDelete;
//    private com.raven.swing.Button cmdEdit;
//    // End of variables declaration//GEN-END:variables
//}

